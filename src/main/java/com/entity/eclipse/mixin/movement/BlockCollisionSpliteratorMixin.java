package com.entity.eclipse.mixin.movement;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.movement.Jesus;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockCollisionSpliterator;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockCollisionSpliterator.class)
public abstract class BlockCollisionSpliteratorMixin {
    @Redirect(method = "computeNext", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;"))
    private VoxelShape onNextCollisionBox(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = state.getCollisionShape(world, pos, context);

        if(world != MinecraftClient.getInstance().world) return shape;
        if(Eclipse.client.player == null) return shape;

        Jesus jesus = (Jesus) ModuleManager.getByClass(Jesus.class);
        if(!jesus.isEnabled()) return shape;

        if(state.getFluidState().isEmpty()) return shape;

        if(
                (state.getBlock() == Blocks.WATER || state.getFluidState().getFluid() == Fluids.WATER) &&
                !Eclipse.client.player.isTouchingWater() &&
                jesus.waterIsSolid() &&
                pos.getY() <= Eclipse.client.player.getY() - 1
        ) return VoxelShapes.fullCube();

        if(
                (state.getBlock() == Blocks.LAVA || state.getFluidState().getFluid() == Fluids.LAVA) &&
                !Eclipse.client.player.isInLava() &&
                jesus.lavaIsSolid() &&
                pos.getY() <= Eclipse.client.player.getY() - 1
        ) return VoxelShapes.fullCube();

        return shape;
    }
}