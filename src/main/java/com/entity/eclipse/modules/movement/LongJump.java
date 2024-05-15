package com.entity.eclipse.modules.movement;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.RenderEvent;
import com.entity.eclipse.utils.types.DoubleValue;
import net.minecraft.util.math.Vec3d;

public class LongJump extends Module {
    public LongJump() {
        super("LongJump", "big jump", ModuleType.MOVEMENT);

        this.config.create("Multiplier", new DoubleValue(1.0));
    }

    @Override
    public void tick() {
        if(Eclipse.client.player == null) return;

        if(Eclipse.client.player.isOnGround()) return;
        if(Eclipse.client.player.isTouchingWater()) return;
        if(Eclipse.client.player.isInLava()) return;
        if(Eclipse.client.player.isClimbing()) return;
        if(Eclipse.client.player.getVelocity().horizontalLength() < 0.1) return;

        Vec3d lookDir = Vec3d.fromPolar(0, Eclipse.client.player.getYaw());
        double speed = this.config.get("Multiplier");

        Eclipse.client.player.setVelocity(
                lookDir.getX() * speed,
                Eclipse.client.player.getVelocity().getY(),
                lookDir.getZ() * speed
        );
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
