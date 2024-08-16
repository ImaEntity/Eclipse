package com.entity.eclipse.modules.world;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.RenderEvent;
import com.entity.eclipse.utils.types.DoubleValue;
import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AutoFarm extends Module {
    public AutoFarm() {
        super("AutoFarm", "Automatically harvests and replants crops.", ModuleType.WORLD);

        this.config.create("Range", new DoubleValue(4.0));
    }

    @Override
    public void tick() {
        if(Eclipse.client.player == null) return;
        if(Eclipse.client.world == null) return;
        if(Eclipse.client.interactionManager == null) return;

        double range = this.config.get("Range");

        for(int yOff = -3; yOff <= 3; yOff++) {
            for(int zOff = (int) -Math.ceil(range); zOff <= Math.ceil(range); zOff++) {
                for(int xOff = (int) -Math.ceil(range); xOff <= Math.ceil(range); xOff++) {
                    int dist = xOff * xOff + zOff * zOff;
                    if(dist > range * range) continue;

                    BlockPos pos = Eclipse.client.player.getBlockPos()
                            .add(xOff, yOff, zOff);

                    BlockState state = Eclipse.client.world.getBlockState(pos);

                    if(
                            state.getBlock() != Blocks.WHEAT &&
                            state.getBlock() != Blocks.FARMLAND
                    ) continue;

                    if(state.getBlock() == Blocks.FARMLAND) {
                        BlockState potentialPlant = Eclipse.client.world.getBlockState(pos.up());
                        if(!(potentialPlant.getBlock() instanceof AirBlock))
                             continue;

                        Eclipse.client.interactionManager.interactBlock(
                                Eclipse.client.player,
                                Hand.OFF_HAND,
                                new BlockHitResult(
                                        pos.toCenterPos(),
                                        Direction.UP,
                                        pos,
                                        true
                                )
                        );

                        return;
                    }

                    CropBlock block = (CropBlock) state.getBlock();
                    if(!block.isMature(state)) continue;

                    Eclipse.client.interactionManager.attackBlock(
                            pos,
                            Direction.UP
                    );

                    Eclipse.client.interactionManager.interactBlock(
                            Eclipse.client.player,
                            Hand.OFF_HAND,
                            new BlockHitResult(
                                    pos.toCenterPos(),
                                    Direction.UP,
                                    pos,
                                    true
                            )
                    );
                }
            }
        }
    }

    @Override
    public void onEnable() {

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
