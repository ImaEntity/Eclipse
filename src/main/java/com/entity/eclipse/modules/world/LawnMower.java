package com.entity.eclipse.modules.world;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.Render2DEvent;
import com.entity.eclipse.utils.events.render.Render3DEvent;
import com.entity.eclipse.utils.types.BooleanValue;
import com.entity.eclipse.utils.types.DoubleValue;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;

public class LawnMower extends Module {
    private final ArrayList<Block> blocksToBreak = new ArrayList<>();

    public LawnMower() {
        super("LawnMower", "vrrrrrrr", ModuleType.WORLD);

        this.config.create("ClientSide", new BooleanValue(true));
        this.config.create("Range", new DoubleValue(8.0));

        this.blocksToBreak.add(Blocks.SHORT_GRASS);
        this.blocksToBreak.add(Blocks.TALL_GRASS);
        this.blocksToBreak.add(Blocks.POPPY);
        this.blocksToBreak.add(Blocks.DANDELION);
        this.blocksToBreak.add(Blocks.BLUE_ORCHID);
        this.blocksToBreak.add(Blocks.ALLIUM);
        this.blocksToBreak.add(Blocks.AZURE_BLUET);
        this.blocksToBreak.add(Blocks.RED_TULIP);
        this.blocksToBreak.add(Blocks.ORANGE_TULIP);
        this.blocksToBreak.add(Blocks.PINK_TULIP);
        this.blocksToBreak.add(Blocks.WHITE_TULIP);
        this.blocksToBreak.add(Blocks.OXEYE_DAISY);
        this.blocksToBreak.add(Blocks.CORNFLOWER);
        this.blocksToBreak.add(Blocks.LILY_OF_THE_VALLEY);
        this.blocksToBreak.add(Blocks.TORCHFLOWER);
        this.blocksToBreak.add(Blocks.BROWN_MUSHROOM);
        this.blocksToBreak.add(Blocks.RED_MUSHROOM);
        this.blocksToBreak.add(Blocks.DEAD_BUSH);
        this.blocksToBreak.add(Blocks.SUNFLOWER);
        this.blocksToBreak.add(Blocks.FERN);
        this.blocksToBreak.add(Blocks.LARGE_FERN);
    }

    @Override
    public void tick() {
        if(Eclipse.client.player == null) return;
        if(Eclipse.client.interactionManager == null) return;
        if(Eclipse.client.world == null) return;

        BlockPos player = Eclipse.client.player.getBlockPos();
        double range = this.config.get("Range");

        for(int yOff = (int) -Math.ceil(range); yOff <= Math.ceil(range); yOff++) {
            for(int zOff = (int) -Math.ceil(range); zOff <= Math.ceil(range); zOff++) {
                for(int xOff = (int) -Math.ceil(range); xOff <= Math.ceil(range); xOff++) {
                    BlockPos pos = player.add(xOff, yOff, zOff);
                    int dist = xOff * xOff + yOff * yOff + zOff * zOff;
                    if(dist > range * range) continue;

                    try {
                        Block block = Eclipse.client.world.getBlockState(pos).getBlock();

                        if(!this.blocksToBreak.contains(block)) continue;

                        if((boolean) this.config.get("ClientSide"))
                            Eclipse.client.interactionManager.breakBlock(pos);
                        else
                            Eclipse.client.interactionManager.attackBlock(pos, Direction.UP);
                    } catch(Exception e) {
                        // Outside render distance
                        Eclipse.log("Cannot reach block (" + pos.toShortString() + "); outside render distance");
                    }
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
    public void renderWorld(Render3DEvent event) {

    }

    @Override
    public void renderScreen(Render2DEvent event) {

    }
}
