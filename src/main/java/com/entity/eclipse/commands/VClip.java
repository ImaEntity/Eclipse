package com.entity.eclipse.commands;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.commands.base.Command;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.utils.Keybind;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;

public class VClip extends Command {
    public VClip() {
        super("VClip", "Vertical teleportation.", "vclip");
    }

    @Override
    public void onExecute(String[] args) {
        if(Eclipse.client.player == null) return;
        if(Eclipse.client.getNetworkHandler() == null) return;

        if(args.length == 0) {
            Eclipse.notifyUser("Syntax: vclip <height>");
            return;
        }

        double height;

        try {
            height = Double.parseDouble(args[0]);
        } catch(NumberFormatException | NullPointerException e) {
            e.printStackTrace();
            Eclipse.notifyUser("Invalid height!");

            return;
        }

        int grounds = (int) Math.ceil(Math.abs(height) / 10);
        if(grounds > 20) grounds = 1;

        if(Eclipse.client.player.hasVehicle()) {
            for(int i = 0; i < grounds - 1; i++)
                Eclipse.client.getNetworkHandler().sendPacket(new VehicleMoveC2SPacket(Eclipse.client.player.getVehicle()));

            Eclipse.client.player.getVehicle().setPosition(
                    Eclipse.client.player.getVehicle().getX(),
                    Eclipse.client.player.getVehicle().getY() + height,
                    Eclipse.client.player.getVehicle().getZ()
            );

            Eclipse.client.getNetworkHandler().sendPacket(new VehicleMoveC2SPacket(Eclipse.client.player.getVehicle()));
            return;
        }

        for(int i = 0; i < grounds - 1; i++)
            Eclipse.client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));

        Eclipse.client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                Eclipse.client.player.getX(),
                Eclipse.client.player.getY() + height,
                Eclipse.client.player.getZ(),
                true
        ));

        Eclipse.client.player.setPosition(
                Eclipse.client.player.getX(),
                Eclipse.client.player.getY() + height,
                Eclipse.client.player.getZ()
        );
    }
}