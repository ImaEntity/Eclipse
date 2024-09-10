package com.entity.eclipse.modules.world;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.RenderEvent;
import com.entity.eclipse.utils.types.BooleanValue;
import com.entity.eclipse.utils.types.DoubleValue;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class Extinguisher extends Module {
    public Extinguisher() {
        super("Extinguisher", "Puts out fire around you.", ModuleType.WORLD);

        this.config.create("ClientSide", new BooleanValue(false));
        this.config.create("Range", new DoubleValue(4.0));
    }

    @Override
    public void tick() {
        if(Eclipse.client.world == null) return;
        if(Eclipse.client.player == null) return;
        if(Eclipse.client.interactionManager == null) return;

        double range = this.config.get("Range");

        for(int yOff = (int) -Math.ceil(range); yOff <= Math.ceil(range); yOff++) {
            for(int zOff = (int) -Math.ceil(range); zOff <= Math.ceil(range); zOff++) {
                for(int xOff = (int) -Math.ceil(range); xOff <= Math.ceil(range); xOff++) {
                    int dist = xOff * xOff + yOff * yOff + zOff * zOff;
                    if(dist > range * range) continue;

                    BlockPos pos = Eclipse.client.player.getBlockPos()
                            .add(xOff, yOff, zOff);

                    try {
                        Block block = Eclipse.client.world.getBlockState(pos).getBlock();
                        if(!(block instanceof AbstractFireBlock)) continue;

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
