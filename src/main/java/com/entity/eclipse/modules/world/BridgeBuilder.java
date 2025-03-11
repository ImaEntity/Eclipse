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
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class BridgeBuilder extends Module {
    private BlockPos breakPos = null;
    private Direction breakDir = Direction.UP;
    private Direction forwardDir = Direction.UP;
    private Direction rightDir = Direction.UP;
    private BlockPos goalBlock = null;
    private Input prevInput = null;
    private MutableInput input = null;
    private float prevYaw = 0f;
    private boolean isCentering = false;

    public BridgeBuilder() {
        super("BridgeBuilder", "make bridge", ModuleType.WORLD);

        this.config.create("AllowInventory", new BooleanValue(true));
        this.config.create("MakeWalls", new BooleanValue(true));
        this.config.create("BreakObstacles", new BooleanValue(true));
        this.config.create("ReplaceNonMatching", new BooleanValue(true));
        this.config.create("EnableRotations", new BooleanValue(false));
        this.config.create("MaxPlacementsPerTick", new IntegerValue(3))
                .visibleIf(() -> !(boolean) this.config.get("EnableRotations"));

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

    @Override
    public void tick() {
        if(Eclipse.client.player == null) return;
        if(Eclipse.client.interactionManager == null) return;
        if(Eclipse.client.world == null) return;

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
            boolean keepAttacking = Eclipse.client.interactionManager.updateBlockBreakingProgress(
                    this.breakPos,
                    this.breakDir
            );

            if(!keepAttacking)
                this.breakPos = null;

            return;
        }

        BlockPos pos = Eclipse.client.player.getBlockPos();

        for(int y = -1; y <= 1; y++) {
            for(int i = -1; i <= 1; i++) {
                if(!(boolean) this.config.get("BreakObstacles"))
                    continue;

                BlockPos targetPos = pos
                        .down(y - 1)
                        .add(this.forwardDir.getVector().multiply(2))
                        .add(this.rightDir.getVector().multiply(i));

                Block block = Eclipse.client.world
                        .getBlockState(targetPos)
                        .getBlock();

                if(block instanceof AirBlock)
                    continue;

                this.breakPos = targetPos;
                this.breakDir = this.forwardDir;

                return;
            }
        }

        int start = (boolean) this.config.get("MakeWalls") ? -2 : -1;
        int end = (boolean) this.config.get("MakeWalls") ? 2 : 1;

        int placements = 0;
        for(int i = start; i <= end; i++) {
            int downAmount = 1;
            if(i == -2 || i == 2)
                downAmount = 0;

            BlockPos refPos = pos
                    .down(downAmount)
                    .add(this.forwardDir.getVector())
                    .add(this.rightDir.getVector().multiply(i));

            BlockPos targetPos = refPos
                    .add(this.forwardDir.getVector());

            Block block = Eclipse.client.world
                    .getBlockState(targetPos)
                    .getBlock();

            boolean isAir = block instanceof AirBlock;
            boolean isLiquid = block == Blocks.WATER || block == Blocks.LAVA;
            boolean isSolid = block.getDefaultState().isSolidBlock(null, targetPos);

            if(!isAir && !isLiquid && (isSolid && !(boolean) this.config.get("ReplaceNonMatching")))
                continue;

            boolean matches = ((ListValue) this.config.getRaw("AllowedBlocks")).contains(block.asItem());

            if((!isSolid || (boolean) this.config.get("ReplaceNonMatching") && !matches) && !isAir && !isLiquid) {
                this.breakPos = targetPos;
                this.breakDir = this.forwardDir;

                return;
            }

            float yaw = Eclipse.client.player.getYaw();
            float pitch = Eclipse.client.player.getPitch();

            if((boolean) this.config.get("EnableRotations")) {
                Eclipse.client.player.lookAt(
                        EntityAnchorArgumentType.EntityAnchor.FEET,
                        Vec3d.ofCenter(refPos.add(this.forwardDir.getVector()))
                );
            }

            if(placements >= (int) this.config.get("MaxPlacementsPerTick")) return;

            int endSlot = this.config.get("AllowInventory") ?
                    Slots.MAIN.end() :
                    Slots.HOTBAR.end();

            int slot = Slots.findFirst(
                    new Slots.Range(Slots.HOTBAR.start(), endSlot),
                    stack -> slotIdx ->
                            ((ListValue) this.config.getRaw("AllowedBlocks")).contains(stack.getItem())
            );

            if(slot == Slots.INVALID_SLOT) {
                this.notifyUser("Ran out of blocks!");

                ModuleManager.queueDisable(this);
                return;
            }

            int prevSlot = Eclipse.client.player.getInventory().selectedSlot;
            if(Slots.HOTBAR.contains(slot))
                Eclipse.client.player.getInventory().selectedSlot = slot;
            else {
                Slots.swap(
                        Slots.indexToID(slot),
                        Slots.indexToID(Slots.OFFHAND)
                );
            }

            Hand hand = Slots.HOTBAR.contains(slot) ? Hand.MAIN_HAND : Hand.OFF_HAND;
            Eclipse.client.interactionManager.interactBlock(
                    Eclipse.client.player,
                    hand,
                    HitResultBuilders.createBlock(refPos, this.forwardDir)
            );

            if(Slots.HOTBAR.contains(slot))
                Eclipse.client.player.getInventory().selectedSlot = prevSlot;
            else {
                Slots.swap(
                        Slots.indexToID(slot),
                        Slots.indexToID(Slots.OFFHAND)
                );
            }

            placements++;

            if((boolean) this.config.get("EnableRotations")) {
                Eclipse.client.player.setAngles(yaw, pitch);
                return;
            }
        }

        this.goalBlock = Eclipse.client.player.getBlockPos().add(this.forwardDir.getVector());
        this.prevYaw = Eclipse.client.player.getYaw();
    }

    @Override
    public void onEnable() {
        if(Eclipse.client.player == null) return;

        Eclipse.client.player.setPitch(0f);

        this.forwardDir = Eclipse.client.player.getFacing();
        this.rightDir = this.forwardDir.rotateYClockwise();

        Eclipse.client.player.setPitch(Eclipse.client.player.prevPitch);

        this.prevYaw = Eclipse.client.player.getYaw();
        this.isCentering = true;
        this.breakPos = null;
        this.input = new MutableInput();
        this.prevInput = Eclipse.client.player.input;
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
