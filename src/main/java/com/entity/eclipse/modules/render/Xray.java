package com.entity.eclipse.modules.render;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.Render2DEvent;
import com.entity.eclipse.utils.events.render.Render3DEvent;
import com.entity.eclipse.utils.types.BlockValue;
import com.entity.eclipse.utils.types.BooleanValue;
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

        this.config.create("OnlyShowExposed", new BooleanValue(false));
    }

    public boolean shouldRenderBlock(boolean original, BlockState state, BlockView view, BlockPos pos, Direction facing, BlockPos blockPos) {
        if(Eclipse.client.world == null) return original;

        boolean isExposed =
                Eclipse.client.world.getBlockState(pos.up()   ).getOutlineShape(view, pos.up()   ) != VoxelShapes.fullCube() ||
                Eclipse.client.world.getBlockState(pos.down() ).getOutlineShape(view, pos.down() ) != VoxelShapes.fullCube() ||
                Eclipse.client.world.getBlockState(pos.north()).getOutlineShape(view, pos.north()) != VoxelShapes.fullCube() ||
                Eclipse.client.world.getBlockState(pos.east() ).getOutlineShape(view, pos.east() ) != VoxelShapes.fullCube() ||
                Eclipse.client.world.getBlockState(pos.south()).getOutlineShape(view, pos.south()) != VoxelShapes.fullCube() ||
                Eclipse.client.world.getBlockState(pos.west() ).getOutlineShape(view, pos.west() ) != VoxelShapes.fullCube();

        boolean shouldShow = ((ListValue) this.config.get("BlockIds")).contains(state.getBlock());

        if(!shouldShow) return false;
        return !((boolean) this.config.get("OnlyShowExposed")) || isExposed;
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
    public void renderWorld(Render3DEvent event) {

    }

    @Override
    public void renderScreen(Render2DEvent event) {

    }
}
