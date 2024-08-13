package com.entity.eclipse.modules.world;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.Slots;
import com.entity.eclipse.utils.Ticker;
import com.entity.eclipse.utils.events.render.RenderEvent;
import com.entity.eclipse.utils.types.BooleanValue;
import com.entity.eclipse.utils.types.ItemValue;
import com.entity.eclipse.utils.types.ListValue;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class BridgeBuilder extends Module {
    private ArrayList<BlockPos> breakingPos = new ArrayList<>();
    private ArrayList<Direction> breakingDir = new ArrayList<>();
    private int breakingQueueLength = 0;

    public BridgeBuilder() {
        super("BridgeBuilder", "make bridge", ModuleType.WORLD);

        this.config.create("AllowInventory", new BooleanValue(true));
        this.config.create("MakeWalls", new BooleanValue(true));
        this.config.create("BreakObstacles", new BooleanValue(true));
        this.config.create("AllowedBlocks", new ListValue(
                ItemValue.class,
                "dirt",
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

        if(this.breakingQueueLength > 0) {
            boolean keepAttacking = Eclipse.client.interactionManager.updateBlockBreakingProgress(
                    this.breakingPos.getFirst(),
                    this.breakingDir.getFirst()
            );

            if(!keepAttacking) {
                this.breakingPos.removeFirst();
                this.breakingDir.removeFirst();

                this.breakingQueueLength--;
            }

            return;
        }

        if(!Ticker.isQueueEmpty())
            return;

        ItemStack offStack = Eclipse.client.player.getOffHandStack();

        boolean isBlock = offStack.getItem() instanceof BlockItem;
        boolean isEmpty = offStack.isEmpty();

        if(!isBlock && !isEmpty)
            return;

        Eclipse.client.player.setPitch(0f);

        Direction facing = Direction.getFacing(Eclipse.client.player.getRotationVec(0));
        Direction toLeft = facing.rotateYCounterclockwise();

        Eclipse.client.player.setPitch(Eclipse.client.player.prevPitch);

        if(!isBlock) {
            int endSlot = this.config.get("AllowInventory") ?
                    Slots.MAIN.end() :
                    Slots.HOTBAR.end();

            int slot = Slots.findFirst(
                    new Slots.Range(Slots.HOTBAR.start(), endSlot),
                    stack -> slotIdx ->
                            ((ListValue) this.config.getRaw("AllowedBlocks")).contains(stack.getItem())
            );

            if(slot == Slots.INVALID_SLOT)
                return;

            Slots.swap(
                    Slots.indexToID(slot),
                    Slots.indexToID(Slots.OFFHAND)
            );

            Eclipse.client.player.setYaw(facing.asRotation());
            Eclipse.client.player.setPosition(
                    Vec3d.of(
                            Eclipse.client.player
                                    .getBlockPos()
                                    .add(facing.getOpposite().getVector().multiply(3))
                    ).add(0.5f, 0, 0.5f)
            );

            Eclipse.client.player.setYaw(Eclipse.client.player.prevYaw);
        }

        Hand hand = Hand.OFF_HAND;
        BlockPos pos = Eclipse.client.player.getBlockPos();

        int start = (boolean) this.config.get("MakeWalls") ? -2 : -1;
        int end = (boolean) this.config.get("MakeWalls") ? 2 : 1;

        for(int i = start; i <= end; i++) {
            int downAmount = 1;
            if(i == -2 || i == 2)
                downAmount = 0;

            BlockPos refPos = pos
                    .down(downAmount)
                    .add(facing.getVector().multiply(2))
                    .add(toLeft.getVector().multiply(i));

            BlockPos targetPos = refPos
                    .add(facing.getVector());

            Block block = Eclipse.client.world
                    .getBlockState(targetPos)
                    .getBlock();

            if(!(block instanceof AirBlock))
                continue;

            Ticker.queue(() -> {
                Eclipse.client.interactionManager.interactBlock(
                        Eclipse.client.player,
                        hand,
                        new BlockHitResult(
                                Vec3d.of(refPos),
                                facing,
                                refPos,
                                true
                        )
                );
            });
        }

        for(int y = -1; y <= 1; y++) {
            for(int i = -1; i <= 1; i++) {
                if(!(boolean) this.config.get("BreakObstacles"))
                    continue;

                BlockPos targetPos = pos
                        .down(y - 1)
                        .add(facing.getVector().multiply(2))
                        .add(toLeft.getVector().multiply(i));

                Block block = Eclipse.client.world
                        .getBlockState(targetPos)
                        .getBlock();

                if(block instanceof AirBlock)
                    continue;


                Ticker.queue(() -> {
                    this.breakingPos.add(targetPos);
                    this.breakingDir.add(facing);

                    this.breakingQueueLength++;
                });
            }
        }

        Ticker.queue(() -> {
            Eclipse.client.player.setYaw(facing.asRotation());
            Eclipse.client.player.setPosition(
                    Vec3d.of(
                            Eclipse.client.player
                                    .getBlockPos()
                                    .add(facing.getVector())
                    ).add(0.5f, 0, 0.5f)
            );

            Eclipse.client.player.setYaw(Eclipse.client.player.prevYaw);
        });
    }

    @Override
    public void onEnable() {
        this.breakingPos = new ArrayList<>();
        this.breakingDir = new ArrayList<>();
        this.breakingQueueLength = 0;
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void renderWorld(RenderEvent event) {

    }

    @Override
    public void renderScreen(RenderEvent event) {

    }
}
