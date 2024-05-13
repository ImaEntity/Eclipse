package com.entity.eclipse.utils.events.packet;

import net.minecraft.network.packet.Packet;

public class PacketEvent {
    private final Packet<?> packet;
    private boolean cancelled = false;

    public PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    public Packet<?> getPacket() {
        return this.packet;
    }
}
