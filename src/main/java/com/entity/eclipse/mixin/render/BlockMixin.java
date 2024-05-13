package com.entity.eclipse.mixin.render;

import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.render.Xray;
import com.entity.eclipse.utils.types.BlockValue;
import com.entity.eclipse.utils.types.StringValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

@Mixin(Block.class)
public class BlockMixin {
    @SuppressWarnings("unchecked")
    @Inject(method = "shouldDrawSide", at = @At("HEAD"), cancellable = true)
    private static void shouldDrawSide(BlockState state, BlockView world, BlockPos pos, Direction side, BlockPos blockPos, CallbackInfoReturnable<Boolean> info) {
        Module xray = ModuleManager.getByClass(Xray.class);
        if(!xray.isEnabled()) return;

        boolean foundBlock = false;
        for(BlockValue value : (ArrayList<BlockValue>) xray.config.get("BlockIds"))
            if(state.getBlock() == value.getValue()) foundBlock = true;

        info.setReturnValue(foundBlock);
    }
}
