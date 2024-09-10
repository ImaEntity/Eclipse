package com.entity.eclipse.modules.player;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.mixin.IPlayerMoveC2SPacketMixin;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.packet.PacketEvents;
import com.entity.eclipse.utils.events.render.RenderEvent;
import com.entity.eclipse.utils.types.BooleanValue;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class AntiHunger extends Module {
    private boolean ignorePacket;
    private boolean lastOnGround;

    public AntiHunger() {
        super("AntiHunger", "spoofs some shit to REDUCE hunger consumption", ModuleType.PLAYER);

        this.config.create("SpoofSprinting", new BooleanValue(true));
        this.config.create("SpoofOnGround", new BooleanValue(true));

        Events.Packet.register(PacketEvents.SEND, event -> {
            if(!this.isEnabled()) return;
            if(Eclipse.client.player == null) return;
            if(Eclipse.client.interactionManager == null) return;

            if(this.ignorePacket && event.getPacket() instanceof PlayerMoveC2SPacket) {
                this.ignorePacket = false;
                return;
            }

            if(Eclipse.client.player.hasVehicle() ||
                    Eclipse.client.player.isTouchingWater() ||
                    Eclipse.client.player.isSubmergedInWater()) return;

            if(event.getPacket() instanceof ClientCommandC2SPacket packet && (boolean) this.config.get("SpoofSprinting"))
                if(packet.getMode() == ClientCommandC2SPacket.Mode.START_SPRINTING) event.setCancelled(true);

            if(event.getPacket() instanceof PlayerMoveC2SPacket packet && (boolean) this.config.get("SpoofOnGround")) {
                if(
                        Eclipse.client.player.isOnGround() &&
                        Eclipse.client.player.fallDistance <= 0 &&
                        !Eclipse.client.interactionManager.isBreakingBlock()
                ) {
                    ((IPlayerMoveC2SPacketMixin) packet).setOnGround(false);
                }
            }
        });
    }

    @Override
    public void tick() {
        if(Eclipse.client.player == null) return;

        if(Eclipse.client.player.isOnGround() && !this.lastOnGround)
            this.ignorePacket = true;

        this.lastOnGround = Eclipse.client.player.isOnGround();
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
