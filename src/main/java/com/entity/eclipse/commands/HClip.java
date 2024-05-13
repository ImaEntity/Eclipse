package com.entity.eclipse.commands;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.commands.base.Command;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class HClip extends Command {
    public HClip() {
        super("HClip", "Horizontal teleportation.", "hclip");
    }

    @Override
    public void onExecute(String[] args) {
        if(Eclipse.client.player == null) return;
        if(args.length == 0) {
            Eclipse.notifyUser("Syntax: hclip <distance>");
            return;
        }

        double distance;

        try {
            distance = Double.parseDouble(args[0]);
        } catch(NumberFormatException | NullPointerException e) {
            e.printStackTrace();
            Eclipse.notifyUser("Invalid distance!");

            return;
        }

        Vec3d forward = Vec3d.fromPolar(0, Eclipse.client.player.getYaw());

        if(Eclipse.client.player.hasVehicle()) {
            Entity vehicle = Eclipse.client.player.getVehicle();

            vehicle.setPosition(
                    vehicle.getX() + forward.x * distance,
                    vehicle.getY(),
                    vehicle.getZ() + forward.z * distance
            );
        }


        Eclipse.client.player.setPosition(
                Eclipse.client.player.getX() + forward.x * distance,
                Eclipse.client.player.getY(),
                Eclipse.client.player.getZ() + forward.z * distance
        );
    }
}
