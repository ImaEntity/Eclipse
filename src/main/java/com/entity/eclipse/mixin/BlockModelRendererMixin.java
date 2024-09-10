package com.entity.eclipse.mixin;

import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.render.Xray;
import com.entity.eclipse.utils.types.BlockValue;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(BlockModelRenderer.class)
public class BlockModelRendererMixin {
    @SuppressWarnings("unchecked")
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRender(
            BlockRenderView world, BakedModel model, BlockState state,
            BlockPos pos, MatrixStack matrices, VertexConsumer vertexConsumer,
            boolean cull, Random random, long seed,
            int overlay, CallbackInfo info
    ) {
        Module xray = ModuleManager.getByClass(Xray.class);
        if(xray == null) return;

        if(!xray.isEnabled()) return;

        boolean foundBlock = false;
        for(BlockValue value : (ArrayList<BlockValue>) xray.config.get("BlockIds"))
            if(state.getBlock() == value.getValue()) foundBlock = true;

        if(!foundBlock) info.cancel();
    }
}
