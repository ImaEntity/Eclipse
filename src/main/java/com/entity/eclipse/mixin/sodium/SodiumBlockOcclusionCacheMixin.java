package com.entity.eclipse.mixin.sodium;

import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.render.Xray;
import com.entity.eclipse.utils.types.BlockValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockOcclusionCache;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;

@Mixin(BlockOcclusionCache.class)
public class SodiumBlockOcclusionCacheMixin {
    @SuppressWarnings("unchecked")
    @ModifyReturnValue(method = "shouldDrawSide", at = @At("RETURN"))
    private static boolean shouldDrawSide(boolean original, BlockState state, BlockView view, BlockPos pos, Direction facing) {
        Module xray = ModuleManager.getByClass(Xray.class);
        if(xray == null) return original;

        if(!xray.isEnabled()) return original;

        boolean foundBlock = false;
        for(BlockValue value : (ArrayList<BlockValue>) xray.config.get("BlockIds"))
            if(state.getBlock() == value.getValue()) foundBlock = true;

        return foundBlock;
    }
}
