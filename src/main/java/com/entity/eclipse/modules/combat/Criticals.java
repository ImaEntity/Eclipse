package com.entity.eclipse.modules.combat;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.packet.PacketEvents;
import com.entity.eclipse.utils.events.render.RenderEvent;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class Criticals extends Module {
    public Criticals() {
        super("Criticals", "Always get a critical hit.", ModuleType.COMBAT);

        Events.Packet.register(PacketEvents.SEND, event -> {
            if(!this.isEnabled()) return;
            if(Eclipse.client.player == null) return;
            if(Eclipse.client.getNetworkHandler() == null) return;
            if(!(event.getPacket() instanceof PlayerInteractEntityC2SPacket)) return;

            Vec3d pos = Eclipse.client.player.getPos();

            Eclipse.client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                    pos.x,
                    pos.y + 0.02,
                    pos.z,
                    false
            ));

            Eclipse.client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                    pos.x,
                    pos.y + 0.01,
                    pos.z,
                    false
            ));
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
    public void renderWorld(RenderEvent event) {

    }

    @Override
    public void renderScreen(RenderEvent event) {

    }
}
