package com.entity.eclipse.modules.player;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.Render2DEvent;
import com.entity.eclipse.utils.events.render.Render3DEvent;
import com.entity.eclipse.utils.types.BooleanValue;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ElytraTweaks extends Module {
    public ElytraTweaks() {
        super("ElytraTweaks", "Tweaks the movement of elytras.", ModuleType.PLAYER);

        this.config.create("SpeedControl", new BooleanValue(true));
        this.config.create("HeightControl", new BooleanValue(true));
    }

    private void heightControl() {
        if(Eclipse.client.player == null) return;

        Vec3d vel = Eclipse.client.player.getVelocity();

        if(Eclipse.client.options.jumpKey.isPressed())
            Eclipse.client.player.setVelocity(vel.add(0, 0.08, 0));
        else if(Eclipse.client.options.sneakKey.isPressed())
            Eclipse.client.player.setVelocity(vel.add(0, -0.04, 0));
    }

    private void speedControl() {
        if(Eclipse.client.player == null) return;

        float yaw = (float) Math.toRadians(Eclipse.client.player.getYaw());
        Vec3d forward = new Vec3d(
                -MathHelper.sin(yaw) * 0.05,
                0,
                MathHelper.cos(yaw) * 0.05
        );

        Vec3d vel = Eclipse.client.player.getVelocity();

        if(Eclipse.client.options.forwardKey.isPressed())
            Eclipse.client.player.setVelocity(vel.add(forward));
        else if(Eclipse.client.options.backKey.isPressed())
            Eclipse.client.player.setVelocity(vel.subtract(forward));
    }

    @Override
    public void tick() {
        if(Eclipse.client.player == null) return;

        ItemStack chest = Eclipse.client.player.getEquippedStack(EquipmentSlot.CHEST);
        if(chest.getItem() != Items.ELYTRA) return;

        if(!Eclipse.client.player.isGliding())
            return;

        if(this.config.get("SpeedControl")) speedControl();
        if(this.config.get("HeightControl")) heightControl();
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
