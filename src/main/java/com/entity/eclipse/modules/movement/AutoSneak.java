package com.entity.eclipse.modules.movement;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.RenderEvent;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityPose;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

public class AutoSneak extends Module {
    public AutoSneak() {
        super("AutoSneak", "Sneaks on the edges of blocks.", ModuleType.MOVEMENT);
    }

    private boolean allowedToSneak() {
        if(Eclipse.client.player == null) return false;

        return Eclipse.client.player.isOnGround() &&
                !Eclipse.client.player.isSubmergedInWater() &&
                !Eclipse.client.player.isInLava() &&
                !Eclipse.client.player.isClimbing();
    }

    @Override
    public void tick() {
        if(Eclipse.client.player == null) return;
        if(Eclipse.client.world == null) return;

        boolean shouldSneak = false;
        Box bounding = Eclipse.client.player.getBoundingBox()
                .offset(0, -1, 0)
                .expand(0.5);

        for(int z = -1; z <= 1; z++) {
            for(int x = -1; x <= 1; x++) {
                double scaledX = x / 3.0 + 0.5;
                double scaledZ = z / 3.0 + 0.5;

                BlockPos pos = Eclipse.client.player.getBlockPos().add(x, -1, z);
                BlockState state = Eclipse.client.world.getBlockState(pos);
                Vec3d scaledPos = new Vec3d(pos.getX() + scaledX, pos.getY() + 1, pos.getZ() + scaledZ);

                if(state.isAir() && bounding.contains(scaledPos)) {
                    shouldSneak = true;
                    break;
                }
            }
        }

        Eclipse.client.options.sneakKey.setPressed(false);

        if(this.allowedToSneak() && shouldSneak)
            Eclipse.client.options.sneakKey.setPressed(true);
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
