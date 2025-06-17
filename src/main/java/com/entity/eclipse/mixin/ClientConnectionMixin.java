package com.entity.eclipse.mixin;

import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.misc.AntiPacketKick;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.packet.PacketEvent;
import com.entity.eclipse.utils.events.packet.PacketEvents;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BundleS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(method = "exceptionCaught", at = @At("HEAD"), cancellable = true)
    public void preventPacketKick(ChannelHandlerContext context, Throwable exception, CallbackInfo info) {
        exception.printStackTrace();

        if(ModuleManager.getByClass(AntiPacketKick.class).isEnabled())
            info.cancel();
    }

    @Inject(
            method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/ClientConnection;handlePacket(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;)V",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    private void receivePacket(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo info) {
        if(packet instanceof BundleS2CPacket packetBundle) {
            Iterator<Packet<? super ClientPlayPacketListener>> iterator = packetBundle.getPackets().iterator();
            while(iterator.hasNext()) {
                Packet<?> p = iterator.next();
                if(Events.Packet.fireEvent(PacketEvents.RECEIVE, new PacketEvent(p, (ClientConnection) (Object) this)))
                    iterator.remove();
            }

            return;
        }

        if(Events.Packet.fireEvent(PacketEvents.RECEIVE, new PacketEvent(packet, (ClientConnection) (Object) this)))
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
        PacketEvent event = new PacketEvent(packet, (ClientConnection) (Object) this);
        boolean cancelled = Events.Packet.fireEvent(PacketEvents.SEND, event);

        if(cancelled) return;
        instance.send(event.getPacket(), callbacks);
    }
}
