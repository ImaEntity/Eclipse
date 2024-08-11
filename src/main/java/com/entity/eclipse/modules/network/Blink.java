package com.entity.eclipse.modules.network;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.packet.PacketEvents;
import com.entity.eclipse.utils.events.render.RenderEvent;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.KeepAliveC2SPacket;

import java.util.ArrayList;

public class Blink extends Module {
    private final ArrayList<Packet<?>> queue = new ArrayList<>();

    public Blink() {
        super("Blink", "Delays outgoing packets.", ModuleType.NETWORK);

        Events.Packet.register(PacketEvents.SEND, event -> {
            if(Eclipse.client.player == null) return;

            if(!this.isEnabled()) return;

            if(event.getPacket() instanceof KeepAliveC2SPacket) return;

            this.queue.add(event.getPacket());
            event.setCancelled(true);
        });
    }

    @Override
    public void tick() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        if(Eclipse.client.getNetworkHandler() == null) {
            this.queue.clear();
            return;
        }

        for(Packet<?> packet : this.queue.toArray(new Packet<?>[0]))
            Eclipse.client.getNetworkHandler().sendPacket(packet);

        this.queue.clear();
    }

    @Override
    public void renderWorld(RenderEvent event) {

    }

    @Override
    public void renderScreen(RenderEvent event) {

    }
}
