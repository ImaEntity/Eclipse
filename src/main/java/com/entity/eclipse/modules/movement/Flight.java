package com.entity.eclipse.modules.movement;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.Render2DEvent;
import com.entity.eclipse.utils.events.render.Render3DEvent;
import com.entity.eclipse.utils.types.DoubleValue;
import com.entity.eclipse.utils.types.EnumValue;
import com.entity.eclipse.utils.types.IntegerValue;
import net.minecraft.client.option.GameOptions;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class Flight extends Module {
    private int bypassTimer = 0;
    private double prevSpeed = 0;
    private boolean wasFlying;

    private enum Mode {
        Vanilla,
        Static
    }

    public Flight() {
        super("Flight", "fly", ModuleType.MOVEMENT);

        this.config.create("Mode", new EnumValue<>(Mode.Static));
        this.config.create("Speed", new DoubleValue(1.0));
        this.config.create("BypassInterval", new IntegerValue(20));

        this.config.create("SprintMultiplier", new DoubleValue(2.0)).visibleIf(() -> this.config.get("Mode") == Mode.Static);
        this.config.create("Acceleration", new DoubleValue(0.25)).visibleIf(() -> this.config.get("Mode") == Mode.Static);
    }

    private void staticFly() {
        if(Eclipse.client.player == null) return;

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
    }

    private void vanillaFly() {
        if(Eclipse.client.player == null) return;

        Eclipse.client.player.getAbilities().flying = true;
        Eclipse.client.player.getAbilities().setFlySpeed((float) (double) this.config.get("Speed") / 20f);
    }

    private void tickBypass() {
        if(Eclipse.client.player == null) return;

        if(this.bypassTimer > (int) this.config.get("BypassInterval"))
            this.bypassTimer = 0;

        switch(this.bypassTimer) {
            case 0 -> Eclipse.client.player.addVelocity(0, -0.1, 0);
            case 1 -> Eclipse.client.player.addVelocity(0, 0.1, 0);
        }

        this.bypassTimer++;
    }

    @Override
    public void tick() {
        switch((Mode) this.config.get("Mode")) {
            case Static -> staticFly();
            case Vanilla -> vanillaFly();
        }

        tickBypass();
    }

    @Override
    public void onEnable() {
        if(Eclipse.client.player == null) return;

        this.wasFlying = Eclipse.client.player.getAbilities().flying;
        this.bypassTimer = 0;
    }

    @Override
    public void onDisable() {
        if(Eclipse.client.player == null) return;

        Eclipse.client.player.getAbilities().flying = this.wasFlying;
        Eclipse.client.player.getAbilities().setFlySpeed(0.05f);
    }

    @Override
    public void renderWorld(Render3DEvent event) {

    }

    @Override
    public void renderScreen(Render2DEvent event) {

    }
}
