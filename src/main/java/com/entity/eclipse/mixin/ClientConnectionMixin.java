package com.entity.eclipse.mixin;

import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.misc.AntiPacketKick;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.packet.PacketEvent;
import com.entity.eclipse.utils.events.packet.PacketEvents;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(method = "exceptionCaught", at = @At("HEAD"), cancellable = true)
    public void preventPacketKick(ChannelHandlerContext context, Throwable exception, CallbackInfo info) {
        exception.printStackTrace();

        if(ModuleManager.getByClass(AntiPacketKick.class).isEnabled())
            info.cancel();
    }

    @Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true)
    private static <T extends PacketListener> void receivePacket(Packet<T> packet, PacketListener listener, CallbackInfo info) {
        if(Events.Packet.fireEvent(PacketEvents.RECEIVE, new PacketEvent(packet)))
            info.cancel();
    }

    @Redirect(
            method = "send(Lnet/minecraft/network/packet/Packet;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/ClientConnection;send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;)V"
            )
    )
    private void sendPacket(ClientConnection instance, Packet<?> packet, PacketCallbacks callbacks) {
        PacketEvent event = new PacketEvent(packet);
        boolean cancelled = Events.Packet.fireEvent(PacketEvents.SEND, event);

        if(cancelled) return;
        instance.send(event.getPacket(), callbacks);
    }
}
