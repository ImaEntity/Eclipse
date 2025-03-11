package com.entity.eclipse.modules.combat;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.packet.PacketEvents;
import com.entity.eclipse.utils.events.render.Render2DEvent;
import com.entity.eclipse.utils.events.render.Render3DEvent;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class Criticals extends Module {
    public Criticals() {
        super("Criticals", "Always get a critical hit.", ModuleType.COMBAT);

        Events.Packet.register(PacketEvents.SEND, event -> {
            if(Eclipse.client.player == null) return;
            if(Eclipse.client.getNetworkHandler() == null) return;

            if(!this.isEnabled()) return;
            if(!(event.getPacket() instanceof PlayerInteractEntityC2SPacket)) return;

            if(
                    !Eclipse.client.player.isOnGround() ||
                    Eclipse.client.player.isSubmergedInWater() ||
                    Eclipse.client.player.isInLava() ||
                    Eclipse.client.player.isClimbing()
            ) return;

            Vec3d pos = Eclipse.client.player.getPos();

            Eclipse.client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                    pos.x,
                    pos.y + 0.11,
                    pos.z,
                    false,
                    Eclipse.client.player.horizontalCollision
            ));

            Eclipse.client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                    pos.x,
                    pos.y + 0.110001,
                    pos.z,
                    false,
                    Eclipse.client.player.horizontalCollision
            ));

            Eclipse.client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                    pos.x,
                    pos.y + 0.000001,
                    pos.z,
                    false,
                    Eclipse.client.player.horizontalCollision
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
    public void renderWorld(Render3DEvent event) {

    }

    @Override
    public void renderScreen(Render2DEvent event) {

    }
}
