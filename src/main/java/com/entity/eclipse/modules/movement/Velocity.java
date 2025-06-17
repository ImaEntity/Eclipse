package com.entity.eclipse.modules.movement;

import com.entity.eclipse.mixin.IEntityVelocityUpdateS2CPacketMixin;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.packet.PacketEvents;
import com.entity.eclipse.utils.events.render.Render2DEvent;
import com.entity.eclipse.utils.events.render.Render3DEvent;
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

            double velX = packet.getVelocityX();
            double velY = packet.getVelocityY();
            double velZ = packet.getVelocityZ();

            // These aren't redundant casts you fucking dipshit
            velX *= (double) this.config.get("HorizontalMultiplier");
            velY *= (double) this.config.get("VerticalMultiplier");
            velZ *= (double) this.config.get("HorizontalMultiplier");

            ((IEntityVelocityUpdateS2CPacketMixin) packet).setVelocityX((int) (velX * 8000.0));
            ((IEntityVelocityUpdateS2CPacketMixin) packet).setVelocityY((int) (velY * 8000.0));
            ((IEntityVelocityUpdateS2CPacketMixin) packet).setVelocityZ((int) (velZ * 8000.0));
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
