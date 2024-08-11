package com.entity.eclipse.modules.world;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.RenderEvent;
import com.entity.eclipse.utils.types.BooleanValue;
import com.entity.eclipse.utils.types.IntegerValue;
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
        this.config.create("Range", new IntegerValue(8));

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
        int range = this.config.get("Range");

        for(int y = -range; y < range; y++) {
            for(int z = -range; z < range; z++) {
                for(int x = -range; x < range; x++) {
                    BlockPos pos = player.add(x, y, z);

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
    public void renderWorld(RenderEvent event) {

    }

    @Override
    public void renderScreen(RenderEvent event) {

    }
}
