package com.entity.eclipse.utils;

import com.entity.eclipse.Eclipse;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class PlayerUtils {
    private static Entity lastAttacked = null;
    private static long attackedLastSet = 0;

    public static void tickSmoothLook(Vec3d target, double laziness) {
        if(Eclipse.client.player == null) return;

        double difX = target.x - Eclipse.client.player.getX();
        double difY = target.y - (Eclipse.client.player.getY() + Eclipse.client.player.getEyeHeight(Eclipse.client.player.getPose()));
        double difZ = target.z - Eclipse.client.player.getZ();

        double required = Math.toDegrees(Math.atan2(difZ, difX)) - 90;
        double sqrt1 = Math.sqrt(difX * difX + difZ * difZ);
        double degTan = -Math.toDegrees(Math.atan2(difY, sqrt1));

        double delta;
        double speed;
        double add;

        // yaw
        delta = MathHelper.wrapDegrees(required - Eclipse.client.player.getYaw());
        speed = Math.abs(delta / laziness);
        add = speed * (delta >= 0 ? 1 : -1);

        if(add >= 0 && add > delta || add < 0 && add < delta)
            add = delta;

        Eclipse.client.player.setYaw(Eclipse.client.player.getYaw() + (float) add);

        // pitch
        required = degTan;
        delta = MathHelper.wrapDegrees(required - Eclipse.client.player.getPitch());
        speed = Math.abs(delta / laziness);
        add = speed * (delta >= 0 ? 1 : -1);

        if(add >= 0 && add > delta || add < 0 && add < delta)
            add = delta;

        Eclipse.client.player.setPitch(Eclipse.client.player.getPitch() + (float) add);
    }

    public static void setLastAttacked(Entity entity) {
        if(entity == Eclipse.client.player) return;
        if(!(entity instanceof PlayerEntity)) return;

        lastAttacked = entity;
        attackedLastSet = System.currentTimeMillis();
    }

    public static Entity getLastAttacked(long lastSetTimeout) {
        if(Eclipse.client.player == null) return null;

        if(lastAttacked == null) return null;

        if(
                !Eclipse.client.player.isAlive() ||
                !lastAttacked.isAlive() ||
                lastAttacked.distanceTo(Eclipse.client.player) > 8
        ) {
            lastAttacked = null;
            return null;
        }

        if(System.currentTimeMillis() - attackedLastSet > lastSetTimeout) return null;

        if(lastAttacked.distanceTo(Eclipse.client.player) > 16) {
            lastAttacked = null;
            return null;
        }

        return lastAttacked;
    }
}
