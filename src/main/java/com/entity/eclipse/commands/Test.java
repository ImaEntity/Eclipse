package com.entity.eclipse.commands;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.commands.base.Command;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class Test extends Command {
    public Test() {
        super("Test", "idfk", "test");
    }

    @Override
    public void onExecute(String[] args) {
        if(Eclipse.client.player == null) return;
        if(Eclipse.client.getNetworkHandler() == null) return;

        if(args.length < 3) {
            Eclipse.notifyUser("Provide a position!");
            return;
        }

        float yaw = Eclipse.client.player.getYaw();
        float pitch = Eclipse.client.player.getPitch();

        if(args.length >= 5) {
            yaw = Float.parseFloat(args[3]);
            pitch = Float.parseFloat(args[4]);
        }

        double x = Double.parseDouble(args[0]);
        double y = Double.parseDouble(args[1]);
        double z = Double.parseDouble(args[2]);

        Eclipse.client.player.setPosition(x, y, z);
        Eclipse.client.player.setAngles(yaw, pitch);
        Eclipse.client.player.setBodyYaw(yaw);

        Eclipse.client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.Full(
                x, y, z,
                yaw, pitch,
                Eclipse.client.player.isOnGround(),
                Eclipse.client.player.horizontalCollision
        ));
    }
}
