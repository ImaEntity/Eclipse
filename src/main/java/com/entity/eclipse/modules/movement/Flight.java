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

    public Flight() {
        super("Flight", "fly", ModuleType.MOVEMENT);

        this.config.create("Speed", new DoubleValue(1.0));
        this.config.create("SprintMultiplier", new DoubleValue(2.0));
        this.config.create("BypassInterval", new IntegerValue(10));
    }

    @Override
    public void tick() {
        if(Eclipse.client.player == null) return;
        if(Eclipse.client.getNetworkHandler() == null) return;

        GameOptions options = Eclipse.client.options;
        float yaw = Eclipse.client.player.getYaw();
        int back = 0;
        int y = 0;
        int right = 0;

        if(options.jumpKey.isPressed()) y++;
        if(options.sneakKey.isPressed()) y--;
        if(options.backKey.isPressed()) back++;
        if(options.forwardKey.isPressed()) back--;
        if(options.rightKey.isPressed()) right++;
        if(options.leftKey.isPressed()) right--;

        double halfSpeed = options.sprintKey.isPressed() ?
                ((double) this.config.get("Speed") * (double) this.config.get("SprintMultiplier")) / 2 :
                (double) this.config.get("Speed") / 2;

        double sin = Math.sin(Math.toRadians(yaw));
        double cos = Math.cos(Math.toRadians(yaw));
        double newX = halfSpeed * back * sin;
        double newZ = halfSpeed * back * -cos;
        double newY = halfSpeed * y;

        newX += halfSpeed * right * -cos;
        newZ += halfSpeed * right * -sin;

        Vec3d newVel = new Vec3d(newX, newY, newZ);
        Eclipse.client.player.setVelocity(newVel);

        this.bypassTimer++;

        if(this.bypassTimer < (int) this.config.get("BypassInterval")) return;

        Vec3d p = Eclipse.client.player.getPos();

        Eclipse.client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(p.x, p.y - 0.2, p.z, false));
        Eclipse.client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(p.x, p.y + 0.2, p.z, false));

        this.bypassTimer = 0;
    }

    @Override
    public void onEnable() {
        this.bypassTimer = 0;
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
