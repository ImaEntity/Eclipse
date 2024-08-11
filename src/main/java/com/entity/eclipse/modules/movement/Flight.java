package com.entity.eclipse.modules.movement;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.RenderEvent;
import com.entity.eclipse.utils.types.DoubleValue;
import com.entity.eclipse.utils.types.IntegerValue;
import net.minecraft.client.option.GameOptions;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class Flight extends Module {
    private int bypassTimer = 0;
    private double prevSpeed = 0;

    public Flight() {
        super("Flight", "fly", ModuleType.MOVEMENT);

        this.config.create("Speed", new DoubleValue(1.0));
        this.config.create("SprintMultiplier", new DoubleValue(2.0));
        this.config.create("BypassInterval", new IntegerValue(10));
        this.config.create("Acceleration", new DoubleValue(0.25));
    }

    @Override
    public void tick() {
        if(Eclipse.client.player == null) return;
        if(Eclipse.client.getNetworkHandler() == null) return;

        GameOptions options = Eclipse.client.options;
        int forward = 0;
        int up = 0;
        int right = 0;

        if(options.jumpKey.isPressed()) up++;
        if(options.sneakKey.isPressed()) up--;
        if(options.forwardKey.isPressed()) forward++;
        if(options.backKey.isPressed()) forward--;
        if(options.rightKey.isPressed()) right++;
        if(options.leftKey.isPressed()) right--;

        double maxSpeed = options.sprintKey.isPressed() ?
                (double) this.config.get("Speed") * (double) this.config.get("SprintMultiplier") :
                (double) this.config.get("Speed");

        double speed = Math.min(this.prevSpeed + (double) this.config.get("Acceleration"), maxSpeed);
        this.prevSpeed = speed;

        double forwardYaw = Math.toRadians(Eclipse.client.player.getYaw() + 90);
        double rightYaw = Math.toRadians(Eclipse.client.player.getYaw() + 180);

        double xMove = speed / 2 * forward * Math.cos(forwardYaw) + speed / 2 * right * Math.cos(rightYaw);
        double zMove = speed / 2 * forward * Math.sin(forwardYaw) + speed / 2 * right * Math.sin(rightYaw);

        Eclipse.client.player.setVelocity(
                speed * xMove,
                speed * up,
                speed * zMove
        );

        this.bypassTimer++;

        if(this.bypassTimer < (int) this.config.get("BypassInterval")) return;

        Vec3d p = Eclipse.client.player.getPos();

        Eclipse.client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(p.x, p.y - 0.2, p.z, false));
        Eclipse.client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(p.x, p.y + 0.2, p.z, false));

        this.bypassTimer = 0;
    }

    @Override
    public void onEnable() {
        this.bypassTimer =  0;
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
