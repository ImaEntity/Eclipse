package com.entity.eclipse.mixin;

import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.render.Xray;
import com.entity.eclipse.utils.types.BlockValue;
import com.entity.eclipse.utils.types.StringValue;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(FluidRenderer.class)
public class FluidRendererMixin {
    @SuppressWarnings("unchecked")
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRender(
            BlockRenderView world, BlockPos pos, VertexConsumer vertexConsumer,
            BlockState blockState, FluidState fluidState, CallbackInfo info
    ) {
        Module xray = ModuleManager.getByClass(Xray.class);
        if(!xray.isEnabled()) return;

        boolean foundBlock = false;
        for(BlockValue value : (ArrayList<BlockValue>) xray.config.get("BlockIds"))
            if(blockState.getBlock() == value.getValue()) foundBlock = true;

        if(!foundBlock) info.cancel();
    }
}
