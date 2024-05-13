package com.entity.eclipse.mixin.events;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.block.BlockEvent;
import com.entity.eclipse.utils.events.block.BlockEvents;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Inject(method = "breakBlock", at = @At("HEAD"), cancellable = true)
    private void onBlockBroken(BlockPos blockPos, CallbackInfoReturnable<Boolean> info) {
        if(Eclipse.client.world == null) return;

        BlockEvent event = new BlockEvent(blockPos, Eclipse.client.world.getBlockState(blockPos));
        boolean cancelled = Events.Block.fireEvent(BlockEvents.BREAK, event);

        Eclipse.client.world.setBlockState(blockPos, event.getState());

        if(cancelled)
            info.setReturnValue(false);
    }
}
