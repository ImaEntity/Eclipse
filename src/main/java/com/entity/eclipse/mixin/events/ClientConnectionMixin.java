package com.entity.eclipse.mixin.events;

import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.network.AntiPacketKick;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.packet.PacketEvent;
import com.entity.eclipse.utils.events.packet.PacketEvents;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true)
    private static <T extends PacketListener> void receivePacket(Packet<T> packet, PacketListener listener, CallbackInfo info) {
        if(Events.Packet.fireEvent(PacketEvents.RECEIVE, new PacketEvent(packet)))
            info.cancel();
    }

    @Inject(method = "exceptionCaught", at = @At("HEAD"), cancellable = true)
    public void preventPacketKick(ChannelHandlerContext context, Throwable exception, CallbackInfo info) {
        exception.printStackTrace();

        if(ModuleManager.getByClass(AntiPacketKick.class).isEnabled())
            info.cancel();
    }

    @Inject(method = "send", at = @At("HEAD"), cancellable = true)
    public void sendPacket(Packet<?> packet, CallbackInfo info) {
        if(Events.Packet.fireEvent(PacketEvents.SEND, new PacketEvent(packet)))
            info.cancel();
    }
}
