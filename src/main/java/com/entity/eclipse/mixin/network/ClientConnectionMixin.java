package com.entity.eclipse.mixin.network;

import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.network.AntiPacketKick;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(method = "exceptionCaught", at = @At("HEAD"), cancellable = true)
    public void preventPacketKick(ChannelHandlerContext context, Throwable exception, CallbackInfo info) {
        exception.printStackTrace();

        if(ModuleManager.getByClass(AntiPacketKick.class).isEnabled())
            info.cancel();
    }
}
