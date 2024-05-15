package com.entity.eclipse.modules.combat;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.packet.PacketEvents;
import com.entity.eclipse.utils.events.render.RenderEvent;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class Knockback extends Module {
    public Knockback() {
        super("Knockback", "Increases knockback applied to shit you hit.", ModuleType.COMBAT);

        Events.Packet.register(PacketEvents.SEND, event -> {
            if(Eclipse.client.player == null) return;
            if(Eclipse.client.getNetworkHandler() == null) return;

            if(!(event.getPacket() instanceof PlayerInteractEntityC2SPacket)) return;
            if(!this.isEnabled()) return;

            Vec3d newPos = Eclipse.client.player.getPos().subtract(0, 1e-10, 0);

            Eclipse.client.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(
                    Eclipse.client.player,
                    ClientCommandC2SPacket.Mode.START_SPRINTING
            ));

            Eclipse.client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                    newPos.getX(),
                    newPos.getY(),
                    newPos.getZ(),
                    true
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
