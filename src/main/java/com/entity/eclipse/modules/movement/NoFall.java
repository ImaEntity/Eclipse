package com.entity.eclipse.modules.movement;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.mixin.IPlayerMoveC2SPacketMixin;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.packet.PacketEvents;
import com.entity.eclipse.utils.events.render.RenderEvent;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NoFall extends Module {
    public NoFall() {
        super("NoFall", "Removes fall damage.", ModuleType.MOVEMENT);

        Events.Packet.register(PacketEvents.SEND, event -> {
            if(!this.isEnabled()) return;

            if(Eclipse.client.player == null) return;
            if(Eclipse.client.getNetworkHandler() == null) return;

            // Forgetting this if statement leads to the SERVER sending invalid packets.
            // And also several network protocol errors upon rejoining.
            // Exploit potential?
            if(!(event.getPacket() instanceof PlayerMoveC2SPacket)) return;

            Module flight = ModuleManager.getByClass(Flight.class);
            if(flight == null) return; // How do you even get here?

            if(flight.isEnabled()) {
                ((IPlayerMoveC2SPacketMixin) event.getPacket()).setOnGround(true);
                return;
            }

            if(Eclipse.client.player.isFallFlying()) return;

            ((IPlayerMoveC2SPacketMixin) event.getPacket()).setOnGround(true);
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
