package com.entity.eclipse.mixin;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.block.BlockEvent;
import com.entity.eclipse.utils.events.block.BlockEvents;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Inject(method = "breakBlock", at = @At("HEAD"), cancellable = true)
    private void onBlockBroken(BlockPos blockPos, CallbackInfoReturnable<Boolean> info) {
        if(Eclipse.client.world == null) return;
        if(Eclipse.client.player == null) return;

        Direction dir = Direction.getFacing(
                Eclipse.client.player.getRotationVec(0)
        ).getOpposite();

        BlockEvent event = new BlockEvent(blockPos, dir, Eclipse.client.world.getBlockState(blockPos));
        boolean cancelled = Events.Block.fireEvent(BlockEvents.BREAK, event);

        Eclipse.client.world.setBlockState(blockPos, event.getState());

        if(cancelled)
            info.setReturnValue(false);
    }

    @Inject(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;getId()I",
                    ordinal = 0
            ),
            method = "updateBlockBreakingProgress(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z"
    )
    private void onBlockDamage(BlockPos pos, Direction dir, CallbackInfoReturnable<Boolean> info) {
        if(Eclipse.client.world == null) return;

        BlockEvent event = new BlockEvent(pos, dir, Eclipse.client.world.getBlockState(pos));
        boolean cancelled = Events.Block.fireEvent(BlockEvents.DAMAGE, event);

        Eclipse.client.world.setBlockState(pos, event.getState());

        if(cancelled)
            info.setReturnValue(false);
    }
}
