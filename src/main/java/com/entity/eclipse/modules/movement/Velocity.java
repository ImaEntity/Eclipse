package com.entity.eclipse.modules.movement;

import com.entity.eclipse.mixin.IEntityVelocityUpdateS2CPacketMixin;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.packet.PacketEvents;
import com.entity.eclipse.utils.events.render.RenderEvent;
import com.entity.eclipse.utils.types.DoubleValue;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;

public class Velocity extends Module {
    public Velocity() {
        super("Velocity", "Modifies velocity packets.", ModuleType.MOVEMENT);

        this.config.create("HorizontalMultiplier", new DoubleValue(0.0));
        this.config.create("VerticalMultiplier", new DoubleValue(0.0));

        Events.Packet.register(PacketEvents.RECEIVE, event -> {
            if(!this.isEnabled()) return;
            if(!(event.getPacket() instanceof EntityVelocityUpdateS2CPacket packet)) return;

            double velX = packet.getVelocityX() / 8000.0;
            double velY = packet.getVelocityY() / 8000.0;
            double velZ = packet.getVelocityZ() / 8000.0;

            // These aren't redundant casts you fucking dipshit
            velX *= (double) this.config.get("HorizontalMultiplier");
            velY *= (double) this.config.get("VerticalMultiplier");
            velZ *= (double) this.config.get("HorizontalMultiplier");

            ((IEntityVelocityUpdateS2CPacketMixin) packet).setVelocityX((int) (velX * 8000));
            ((IEntityVelocityUpdateS2CPacketMixin) packet).setVelocityY((int) (velY * 8000));
            ((IEntityVelocityUpdateS2CPacketMixin) packet).setVelocityZ((int) (velZ * 8000));
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
