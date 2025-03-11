package com.entity.eclipse.mixin.sodium;

import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.render.Xray;
import com.entity.eclipse.utils.types.BlockValue;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import net.caffeinemc.mods.sodium.client.render.chunk.translucent_sorting.TranslucentGeometryCollector;
import net.caffeinemc.mods.sodium.client.world.LevelSlice;
import net.caffeinemc.mods.sodium.fabric.render.FluidRendererImpl;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(FluidRendererImpl.class)
public class SodiumFluidRendererImplMixin {
    @SuppressWarnings("unchecked")
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRender(
            LevelSlice level, BlockState blockState, FluidState fluidState,
            BlockPos blockPos, BlockPos offset, TranslucentGeometryCollector collector,
            ChunkBuildBuffers buffers, CallbackInfo info
    ) {
        Module xray = ModuleManager.getByClass(Xray.class);
        if(xray == null) return;

        if(!xray.isEnabled()) return;

        boolean foundBlock = false;
        for(BlockValue value : (ArrayList<BlockValue>) xray.config.get("BlockIds"))
            if(blockState.getBlock() == value.getValue()) foundBlock = true;

        if(!foundBlock) info.cancel();
    }
}
