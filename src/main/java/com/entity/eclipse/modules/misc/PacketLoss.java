package com.entity.eclipse.modules.misc;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.packet.PacketEvents;
import com.entity.eclipse.utils.events.render.Render2DEvent;
import com.entity.eclipse.utils.events.render.Render3DEvent;
import net.minecraft.network.packet.s2c.common.KeepAliveS2CPacket;
import net.minecraft.network.packet.s2c.play.*;

public class PacketLoss extends Module {
    public PacketLoss() {
        super("PacketLoss", "Ignores incoming packets.", ModuleType.MISC);

        Events.Packet.register(PacketEvents.RECEIVE, event -> {
            if(Eclipse.client.player == null) return;

            if(!this.isEnabled()) return;

            if(event.getPacket() instanceof KeepAliveS2CPacket) return;

            if(event.getPacket() instanceof ChunkDataS2CPacket) return;
            if(event.getPacket() instanceof ChunkSentS2CPacket) return;
            if(event.getPacket() instanceof ChunkBiomeDataS2CPacket) return;
            if(event.getPacket() instanceof ChunkDeltaUpdateS2CPacket) return;
            if(event.getPacket() instanceof ChunkLoadDistanceS2CPacket) return;
            if(event.getPacket() instanceof ChunkRenderDistanceCenterS2CPacket) return;
            if(event.getPacket() instanceof UnloadChunkS2CPacket) return;
            if(event.getPacket() instanceof StartChunkSendS2CPacket) return;

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

    }

    @Override
    public void renderWorld(Render3DEvent event) {

    }

    @Override
    public void renderScreen(Render2DEvent event) {

    }
}
