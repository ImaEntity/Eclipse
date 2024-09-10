package com.entity.eclipse.modules.render;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.RenderEvent;
import com.entity.eclipse.utils.types.BlockValue;
import com.entity.eclipse.utils.types.ListValue;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class Xray extends Module {
    public Xray() {
        super("Xray", "makes you better at mining", ModuleType.RENDER);

        this.config.create("BlockIds", new ListValue(
                BlockValue.class,
                "diamond_ore",
                "deepslate_diamond_ore",
                "ancient_debris"
        ));
    }

    public boolean shouldRenderBlock(boolean original, BlockState state, BlockView view, BlockPos pos, Direction facing, BlockPos blockPos) {
        if(Eclipse.client.world == null) return original;

        boolean shouldShow = ((ListValue) this.config.get("BlockIds")).contains(state.getBlock());
        boolean isFullCube = state.getOutlineShape(view, pos) == VoxelShapes.fullCube();

        if(!shouldShow && !isFullCube)
            return false;

        return shouldShow;
    }

    @Override
    public void tick() {

    }

    @Override
    public void onEnable() {
        if(Eclipse.client.worldRenderer == null) return;
        Eclipse.client.worldRenderer.reload();
    }

    @Override
    public void onDisable() {
        if(Eclipse.client.worldRenderer == null) return;
        Eclipse.client.worldRenderer.reload();
    }

    @Override
    public void renderWorld(RenderEvent event) {

    }

    @Override
    public void renderScreen(RenderEvent event) {

    }
}
