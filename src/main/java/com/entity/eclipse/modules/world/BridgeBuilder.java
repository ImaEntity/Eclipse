package com.entity.eclipse.modules.world;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.HitResultBuilders;
import com.entity.eclipse.utils.MutableInput;
import com.entity.eclipse.utils.Slots;
import com.entity.eclipse.utils.events.render.Render2DEvent;
import com.entity.eclipse.utils.events.render.Render3DEvent;
import com.entity.eclipse.utils.types.BooleanValue;
import com.entity.eclipse.utils.types.IntegerValue;
import com.entity.eclipse.utils.types.ItemValue;
import com.entity.eclipse.utils.types.ListValue;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.input.Input;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class BridgeBuilder extends Module {
    private BlockPos breakPos = null;
    private int breakTimer = 0;
    private Direction forwardDir = Direction.DOWN;
    private Direction rightDir = Direction.DOWN;
    private BlockPos goalBlock = null;
    private Input prevInput = null;
    private MutableInput input = null;
    private float prevYaw = 0f;
    private boolean isCentering = false;

    public BridgeBuilder() {
        super("BridgeBuilder", "make bridge", ModuleType.WORLD);

        this.config.create("AllowInventory", new BooleanValue(true));
        this.config.create("BridgeWidth", new IntegerValue(3));
        this.config.create("MakeWalls", new BooleanValue(true));
        this.config.create("BreakObstacles", new BooleanValue(true));
        this.config.create("BreakAboveWalls", new BooleanValue(false))
                .visibleIf(() -> (boolean) this.config.get("MakeWalls") && (boolean) this.config.get("BreakObstacles"));

        this.config.create("ReplaceNonMatching", new BooleanValue(true));
        this.config.create("EnableRotations", new BooleanValue(false));

        this.config.create("MaxInstaBreaksPerTick", new IntegerValue(5));
        this.config.create("MaxPlacementsPerTick", new IntegerValue(3))
                .visibleIf(() -> !(boolean) this.config.get("EnableRotations"));

        this.config.create("CeilingHeight", new IntegerValue(3));
        this.config.create("CreateCeiling", new BooleanValue(false));
        this.config.create("AllowedCeilingBlocks", new ListValue(
                ItemValue.class,
                "dirt",
                "cobbled_deepslate",
                "cobblestone",
                "netherrack",
                "end_stone",
                "obsidian"
        )).visibleIf(() -> this.config.get("CreateCeiling"));

        this.config.create("AllowedBlocks", new ListValue(
                ItemValue.class,
                "dirt",
                "cobbled_deepslate",
                "cobblestone",
                "netherrack",
                "end_stone",
                "obsidian"
        ));
    }

    private int[] getLeftRightPair() {
        return switch((int) this.config.get("BridgeWidth")) {
            case 1 -> new int[]{0, 0};
            case 2 -> new int[]{-1, 0};
            case 4 -> new int[]{-2, 1};
            case 5 -> new int[]{-2, 2};
            default -> new int[]{-1, 1};
        };
    }

    private int ticksToBreak(BlockPos pos) {
        if(Eclipse.client.world == null) return 1;

        return (int) Math.ceil(1.0 / Eclipse.client.world.getBlockState(pos).calcBlockBreakingDelta(
                Eclipse.client.player,
                Eclipse.client.world,
                pos
        ));
    }

    // breaks block instantly if possible,
    // returns whether this should be the last action of the tick
    private boolean breakBlock(BlockPos pos, int brokenThisTick) {
        if(Eclipse.client.interactionManager == null) return true;
        if(Eclipse.client.player == null) return true;

        int ticks = this.ticksToBreak(pos);
        if(ticks > 1) {
            this.breakPos = pos;
            return true;
        }

        if(brokenThisTick >= (int) this.config.get("MaxInstaBreaksPerTick"))
            return true;

        if((boolean) this.config.get("EnableRotations")) {
            Eclipse.client.player.lookAt(
                    EntityAnchorArgumentType.EntityAnchor.EYES,
                    pos.toCenterPos().offset(this.forwardDir.getOpposite(), 0.5)
            );
        }

        Eclipse.client.interactionManager.attackBlock(
                pos,
                this.forwardDir.getOpposite()
        );

        return false;
    }

    @Override
    public void tick() {
        if(Eclipse.client.world == null) return;
        if(Eclipse.client.player == null) return;
        if(Eclipse.client.interactionManager == null) return;
        if(Eclipse.client.getNetworkHandler() == null) return;

        if((int) this.config.get("BridgeWidth") < 1)
            this.config.set("BridgeWidth", 1);

        if((int) this.config.get("BridgeWidth") > 5)
            this.config.set("BridgeWidth", 5);

        ItemStack heldStack = Eclipse.client.player.getMainHandStack();
        if(
                (
                        (boolean) this.config.get("BreakObstacles") ||
                        (boolean) this.config.get("ReplaceNonMatching")
                ) &&
                heldStack.getMaxDamage() - heldStack.getDamage() <= 5
        ) {
            this.notifyUser("Tool durability too low!");

            ModuleManager.queueDisable(this);
            return;
        }

        if(this.isCentering) {
            // dist from center of block
            double blockOffX = Math.abs(Eclipse.client.player.getX() - (int) Eclipse.client.player.getX()) - 0.5;
            double blockOffZ = Math.abs(Eclipse.client.player.getZ() - (int) Eclipse.client.player.getZ()) - 0.5;

            boolean isWithinX = Math.abs(blockOffX) <= 0.1;
            boolean isWithinZ = Math.abs(blockOffZ) <= 0.1;

            if(isWithinX && isWithinZ) {
                Eclipse.client.player.setYaw(this.prevYaw);
                this.isCentering = false;
                this.input.stop();

                return;
            }

            Eclipse.client.player.setYaw(0);
            this.input.sneak(true);

            if(!isWithinX) {
                this.input.right(Eclipse.client.player.getX() < 0 ? blockOffX < 0 : blockOffX > 0);
                this.input.left(Eclipse.client.player.getX() < 0 ? blockOffX > 0 : blockOffX < 0);
            } else {
                this.input.right(false);
                this.input.left(false);
            }

            if(!isWithinZ) {
                this.input.forward(Eclipse.client.player.getZ() < 0 ? blockOffZ > 0 : blockOffZ < 0);
                this.input.backward(Eclipse.client.player.getZ() < 0 ? blockOffZ < 0 : blockOffZ > 0);
            } else {
                this.input.forward(false);
                this.input.backward(false);
            }

            return;
        }

        if(this.goalBlock != null) {
            double current = this.goalBlock.getSquaredDistance(Eclipse.client.player.getBlockPos());
            double next = this.goalBlock.getSquaredDistance(Eclipse.client.player.getBlockPos().offset(this.forwardDir));

            if(next > current) {
                this.goalBlock = null;
                this.input.stop();

                return;
            }

            Eclipse.client.player.setYaw(this.forwardDir.getPositiveHorizontalDegrees());

//            this.input.sprint(true);
            this.input.forward(true);

            return;
        }

        if(this.breakPos != null) {
            if((boolean) this.config.get("EnableRotations")) {
                Eclipse.client.player.lookAt(
                        EntityAnchorArgumentType.EntityAnchor.EYES,
                        this.breakPos.toCenterPos().offset(this.forwardDir.getOpposite(), 0.5)
                );
            }

            int ticks = this.ticksToBreak(this.breakPos);

            Eclipse.client.player.swingHand(Hand.MAIN_HAND);

            if(this.breakTimer == 0) {
                Eclipse.client.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(
                        PlayerActionC2SPacket.Action.START_DESTROY_BLOCK,
                        this.breakPos,
                        this.forwardDir.getOpposite()
                ));
            } else if(this.breakTimer >= ticks) {
                Eclipse.client.world.breakBlock(this.breakPos, false);
                Eclipse.client.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(
                        PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK,
                        this.breakPos,
                        this.forwardDir.getOpposite()
                ));

                this.breakPos = null;
                this.breakTimer = -1;
            }

            this.breakTimer++;
            return;
        }

        BlockPos pos = Eclipse.client.player.getBlockPos();

        int startY = -(int) this.config.get("CeilingHeight") + 2;
        int startX = this.getLeftRightPair()[0];
        int endX = this.getLeftRightPair()[1];

        int start = (boolean) this.config.get("MakeWalls") ? startX - 1 : startX;
        int end = (boolean) this.config.get("MakeWalls") ? endX + 1 : endX;

        int blocksBroken = 0;
        for(int y = startY; y <= 1; y++) {
            for(int i = start; i <= end; i++) {
                if(!(boolean) this.config.get("BreakObstacles"))
                    continue;

                if(
                        (i == startX - 1 || i == endX + 1) &&
                        !(boolean) this.config.get("BreakAboveWalls")
                ) continue;

                if(
                        (i == startX - 1 || i == endX + 1) &&
                        y == 1
                ) continue;

                BlockPos targetPos = pos
                        .down(y - 1)
                        .add(this.forwardDir.getVector().multiply(2))
                        .add(this.rightDir.getVector().multiply(i));

                Block block = Eclipse.client.world
                        .getBlockState(targetPos)
                        .getBlock();

                if(block instanceof AirBlock)
                    continue;

                if(this.breakBlock(targetPos, blocksBroken))
                    return;

                blocksBroken++;
            }
        }

        if(blocksBroken > 0) return;

        int endSlot = this.config.get("AllowInventory") ?
                Slots.MAIN.end() :
                Slots.HOTBAR.end();

        int slot = Slots.findFirst(
                new Slots.Range(Slots.HOTBAR.start(), endSlot),
                (stack, slotIdx) ->
                        ((ListValue) this.config.getRaw("AllowedBlocks")).contains(stack.getItem())
        );

        if(slot == Slots.INVALID_SLOT) {
            this.notifyUser("Ran out of blocks!");

            ModuleManager.queueDisable(this);
            return;
        }

        int prevSlot = Eclipse.client.player.getInventory().getSelectedSlot();
        if(Slots.HOTBAR.contains(slot))
            Eclipse.client.player.getInventory().setSelectedSlot(slot);
        else {
            Eclipse.client.player.getInventory().setSelectedSlot(Slots.HOTBAR.end());
            Slots.swap(
                    Slots.indexToID(Slots.HOTBAR.end()),
                    Slots.indexToID(slot)
            );
        }

        int placements = 0;
        blocksBroken = 0;
        for(int i = start; i <= end; i++) {
            int downAmount = 1;
            if(i == startX - 1 || i == endX + 1)
                downAmount = 0;

            BlockPos refPos = pos
                    .down(downAmount)
//                    .add(this.forwardDir.getVector())
                    .add(this.rightDir.getVector().multiply(i));

            BlockPos targetPos = refPos
                    .add(this.forwardDir.getVector());

            Block block = Eclipse.client.world
                    .getBlockState(targetPos)
                    .getBlock();

            boolean isAir = block instanceof AirBlock;
            boolean isLiquid = block == Blocks.WATER || block == Blocks.LAVA;
            boolean isSolid = !block.getDefaultState().getOutlineShape(Eclipse.client.world, targetPos).isEmpty();
            boolean matches = ((ListValue) this.config.getRaw("AllowedBlocks")).contains(block.asItem());

            if(
                    !isAir &&
                    !isLiquid &&
                    (
                            (!(boolean) this.config.get("ReplaceNonMatching") && isSolid) ||
                            ((boolean) this.config.get("ReplaceNonMatching") && matches)
                    )
            ) continue;

            if(
                    (
                            !isSolid ||
                            (
                                    (boolean) this.config.get("ReplaceNonMatching") &&
                                    !matches
                            )
                    ) &&
                    !isAir &&
                    !isLiquid
            ) {
                Eclipse.client.player.getInventory().setSelectedSlot(prevSlot);
                if(this.breakBlock(targetPos, blocksBroken))
                    return;

                blocksBroken++;
            }

            if((boolean) this.config.get("EnableRotations")) {
                Eclipse.client.player.lookAt(
                        EntityAnchorArgumentType.EntityAnchor.EYES,
                        Vec3d.ofCenter(refPos)
                );
            }

            if(placements >= (int) this.config.get("MaxPlacementsPerTick")) {
                Eclipse.client.player.getInventory().setSelectedSlot(prevSlot);
                return;
            }

            if(blocksBroken > 0) return;
            Eclipse.client.interactionManager.interactBlock(
                    Eclipse.client.player,
                    Hand.MAIN_HAND,
                    HitResultBuilders.createBlock(refPos, this.forwardDir)
            );

            placements++;

            if((boolean) this.config.get("EnableRotations")) {
                Eclipse.client.player.getInventory().setSelectedSlot(prevSlot);
                return;
            }
        }

        Eclipse.client.player.getInventory().setSelectedSlot(prevSlot);
        if(placements > 0) return;

        if((boolean) this.config.get("CreateCeiling")) {
            slot = Slots.findFirst(
                    new Slots.Range(Slots.HOTBAR.start(), endSlot),
                    (stack, slotIdx) ->
                            ((ListValue) this.config.getRaw("AllowedCeilingBlocks")).contains(stack.getItem())
            );

            if(slot == Slots.INVALID_SLOT) {
                this.notifyUser("Ran out of ceiling blocks!");

                ModuleManager.queueDisable(this);
                return;
            }

            prevSlot = Eclipse.client.player.getInventory().getSelectedSlot();
            if(Slots.HOTBAR.contains(slot))
                Eclipse.client.player.getInventory().setSelectedSlot(slot);
            else {
                Eclipse.client.player.getInventory().setSelectedSlot(Slots.HOTBAR.end());
                Slots.swap(
                        Slots.indexToID(slot),
                        Slots.indexToID(Slots.HOTBAR.end())
                );
            }

            placements = 0;

            for(int i = startX; i <= endX; i++) {
                BlockPos refPos = pos
                        .up(this.config.get("CeilingHeight"))
//                        .add(this.forwardDir.getVector())
                        .add(this.rightDir.getVector().multiply(i));

                BlockPos targetPos = refPos
                        .add(this.forwardDir.getVector());

                Block block = Eclipse.client.world
                        .getBlockState(targetPos)
                        .getBlock();

                boolean isAir = block instanceof AirBlock;
                boolean isLiquid = block == Blocks.WATER || block == Blocks.LAVA;
                boolean isSolid = block.getDefaultState().isSolidBlock(null, targetPos);
                boolean matches = ((ListValue) this.config.getRaw("AllowedCeilingBlocks")).contains(block.asItem());

                if(
                        !isAir && !isLiquid &&
                        (
                                (!(boolean) this.config.get("ReplaceNonMatching") && isSolid) ||
                                ((boolean) this.config.get("ReplaceNonMatching") && matches)
                        )
                ) continue;

                if((!isSolid || (boolean) this.config.get("ReplaceNonMatching") && !matches) && !isAir && !isLiquid) {
                    Eclipse.client.player.getInventory().setSelectedSlot(prevSlot);
                    if(this.breakBlock(targetPos, blocksBroken))
                        return;

                    blocksBroken++;
                }

                if((boolean) this.config.get("EnableRotations")) {
                    Eclipse.client.player.lookAt(
                            EntityAnchorArgumentType.EntityAnchor.EYES,
                            Vec3d.ofCenter(refPos)
                    );
                }

                if(placements >= (int) this.config.get("MaxPlacementsPerTick")) {
                    Eclipse.client.player.getInventory().setSelectedSlot(prevSlot);
                    return;
                }

                if(blocksBroken > 0) return;
                Eclipse.client.interactionManager.interactBlock(
                        Eclipse.client.player,
                        Hand.MAIN_HAND,
                        HitResultBuilders.createBlock(refPos, this.forwardDir)
                );

                placements++;

                if((boolean) this.config.get("EnableRotations")) {
                    Eclipse.client.player.getInventory().setSelectedSlot(prevSlot);
                    return;
                }
            }

            Eclipse.client.player.getInventory().setSelectedSlot(prevSlot);
            if(placements > 0) return;
        }

        this.goalBlock = Eclipse.client.player.getBlockPos().add(this.forwardDir.getVector());
        this.prevYaw = Eclipse.client.player.getYaw();
    }

    @Override
    public void onEnable() {
        if(Eclipse.client.player == null) return;

        float pitch = Eclipse.client.player.getPitch();
        Eclipse.client.player.setPitch(0f);

        this.forwardDir = Eclipse.client.player.getFacing();
        this.rightDir = this.forwardDir.rotateYClockwise();

        Eclipse.client.player.setPitch(pitch);

        this.breakTimer = 0;
        this.prevYaw = Eclipse.client.player.getYaw();
        this.isCentering = true;
        this.breakPos = null;
        this.input = new MutableInput();
        this.prevInput = Eclipse.client.player.input;
        this.goalBlock = null;
        Eclipse.client.player.input = this.input;
    }

    @Override
    public void onDisable() {
        if(Eclipse.client.player == null) return;

        Eclipse.client.player.input = this.prevInput;
    }

    @Override
    public void renderWorld(Render3DEvent event) {

    }

    @Override
    public void renderScreen(Render2DEvent event) {

    }
}
