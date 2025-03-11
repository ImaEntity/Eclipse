package com.entity.eclipse.modules.render;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.packet.PacketEvents;
import com.entity.eclipse.utils.events.render.Render2DEvent;
import com.entity.eclipse.utils.events.render.Render3DEvent;
import com.entity.eclipse.utils.types.BooleanValue;
import com.entity.eclipse.utils.types.FloatValue;
import net.minecraft.entity.EntityPose;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class Freecam extends Module {
    private Vec3d startPos;
    private float pitch;
    private float yaw;
    private boolean wasFlying;
    private float health;

    public Freecam() {
        super("Freecam", "ascend your soul", ModuleType.RENDER);

        this.config.create("Speed", new FloatValue(2f));
        this.config.create("DisableOnDamage", new BooleanValue(true));

        Events.Packet.register(PacketEvents.SEND, event -> {
            if(!this.isEnabled()) return;

            if(event.getPacket() instanceof PlayerMoveC2SPacket) event.setCancelled(true);
            if(event.getPacket() instanceof PlayerInputC2SPacket) event.setCancelled(true);
        });
    }

    @Override
    public void tick() {
        if(Eclipse.client.player == null) return;

        Eclipse.client.player.getAbilities().setFlySpeed((float) this.config.get("Speed") / 20f);
        Eclipse.client.player.getAbilities().flying = true;

        Eclipse.client.player.setSwimming(false);
        Eclipse.client.player.setPose(EntityPose.STANDING);

        if(!(boolean) this.config.get("DisableOnDamage")) return;
        if(Eclipse.client.player.getHealth() < this.health)
            ModuleManager.queueDisable(this);
    }

    @Override
    public void onEnable() {
        if(Eclipse.client.player == null) return;

        this.startPos = Eclipse.client.player.getPos();
        this.pitch = Eclipse.client.player.getPitch();
        this.yaw = Eclipse.client.player.getYaw();
        this.wasFlying = Eclipse.client.player.getAbilities().flying;

        Eclipse.client.gameRenderer.setRenderHand(false);
        Eclipse.client.player.setOnGround(false);

        this.health = Eclipse.client.player.getHealth();
    }

    @Override
    public void onDisable() {
        if(Eclipse.client.player == null) return;

        Eclipse.client.player.getAbilities().setFlySpeed(0.05f);
        Eclipse.client.player.updatePosition(this.startPos.getX(), this.startPos.getY(), this.startPos.getZ());
        Eclipse.client.player.setPitch(this.pitch);
        Eclipse.client.player.setYaw(this.yaw);
        Eclipse.client.player.getAbilities().flying = this.wasFlying;
        Eclipse.client.gameRenderer.setRenderHand(true);
        Eclipse.client.player.setVelocity(Vec3d.ZERO);
    }

    @Override
    public void renderWorld(Render3DEvent event) {

    }

    @Override
    public void renderScreen(Render2DEvent event) {

    }
}
