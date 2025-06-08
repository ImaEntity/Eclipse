package com.entity.eclipse.mixin.sodium;

import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.render.Xray;
import com.entity.eclipse.utils.types.BlockValue;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(BlockRenderer.class)
public class SodiumBlockRendererMixin {
    @SuppressWarnings("unchecked")
    @Inject(
            method = "renderModel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/caffeinemc/mods/sodium/client/model/color/ColorProviderRegistry;getColorProvider(Lnet/minecraft/block/Block;)Lnet/caffeinemc/mods/sodium/client/model/color/ColorProvider;",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    private void onRenderModel(BlockStateModel model, BlockState state, BlockPos pos, BlockPos origin, CallbackInfo info) {
        Module xray = ModuleManager.getByClass(Xray.class);
        if(xray == null) return;

        if(!xray.isEnabled()) return;

        boolean foundBlock = false;
        for(BlockValue value : (ArrayList<BlockValue>) xray.config.get("BlockIds"))
            if(state.getBlock() == value.getValue()) foundBlock = true;

        if(!foundBlock) info.cancel();
    }
}
