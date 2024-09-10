package com.entity.eclipse.mixin;

import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.movement.Slippy;
import com.entity.eclipse.modules.render.Xray;
import com.entity.eclipse.utils.types.BlockValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;

@Mixin(Block.class)
public class BlockMixin {
    @SuppressWarnings("unchecked")
    @ModifyReturnValue(method = "shouldDrawSide", at = @At("RETURN"))
    private static boolean shouldDrawSide(boolean original, BlockState state, BlockView view, BlockPos pos, Direction facing, BlockPos blockPos) {
        Module xray = ModuleManager.getByClass(Xray.class);
        if(xray == null) return original;

        if(!xray.isEnabled()) return original;

        boolean foundBlock = false;
        for(BlockValue value : (ArrayList<BlockValue>) xray.config.get("BlockIds"))
            if(state.getBlock() == value.getValue()) foundBlock = true;

        return foundBlock;
    }

    @SuppressWarnings("unchecked")
    @ModifyReturnValue(method = "getSlipperiness", at = @At("RETURN"))
    public float getSlipperiness(float original) {
        Module slippy = ModuleManager.getByClass(Slippy.class);
        if(slippy == null) return original;

        if(!slippy.isEnabled()) return original;

        Block block = (Block) (Object) this;

        boolean foundBlock = false;
        for(BlockValue value : (ArrayList<BlockValue>) slippy.config.get("BlacklistedBlocks"))
            if(block == value.getValue()) foundBlock = true;

        return foundBlock ? original : slippy.config.get("Slipperiness");
    }
}
