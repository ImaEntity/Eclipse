package com.entity.eclipse.modules.render;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.packet.PacketEvents;
import com.entity.eclipse.utils.events.render.RenderEvent;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class Freecam extends Module {
    public Freecam() {
        super("Freecam", "ascend your soul", ModuleType.RENDER);

        Events.Packet.register(PacketEvents.SEND, event -> {
            if(!this.isEnabled()) return;

            if(!(event.getPacket() instanceof PlayerMoveC2SPacket)) return;

            event.setCancelled(true);
        });
    }

    @Override
    public void tick() {

    }

    @Override
    public void onEnable() {
        if(Eclipse.client.cameraEntity == null) return;
        if(Eclipse.client.player == null) return;

        Eclipse.client.cameraEntity.noClip = true;
        Eclipse.client.player.noClip = true;
    }

    @Override
    public void onDisable() {
        if(Eclipse.client.cameraEntity == null) return;
        if(Eclipse.client.player == null) return;

        Eclipse.client.cameraEntity.noClip = false;
        Eclipse.client.player.noClip = false;
    }

    @Override
    public void renderWorld(RenderEvent event) {

    }

    @Override
    public void renderScreen(RenderEvent event) {

    }
}
