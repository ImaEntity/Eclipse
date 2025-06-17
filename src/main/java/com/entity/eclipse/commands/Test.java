package com.entity.eclipse.commands;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.commands.base.Command;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;

public class Test extends Command {
    private HashMap<String, Object> data = new HashMap<>();

    public Test() {
        super("Test", "idfk", "test");
    }

    @Override
    public void onExecute(String[] args) {
        if(Eclipse.client.player == null) return;

        if(args.length < 1) {
            Eclipse.log("Provide an option");
            return;
        }

        switch(args[0].toLowerCase()) {
            case "pos1", "pos2" -> {
                if(args.length - 1 < 3) {
                    this.data.put(args[0].toLowerCase(), Eclipse.client.player.getBlockPos());

                    Eclipse.notifyUser("Set " + args[0].toLowerCase() + " to current position");
                    break;
                }

                int x;
                int y;
                int z;

                try {
                    x = Integer.parseInt(args[1]);
                    y = Integer.parseInt(args[2]);
                    z = Integer.parseInt(args[3]);
                } catch(NumberFormatException e) {
                    Eclipse.log("Provide coordinates in the form: X Y Z");
                    break;
                }

                this.data.put(args[0].toLowerCase(), new BlockPos(x, y, z));
                Eclipse.notifyUser("Set " + args[0].toLowerCase() + " to");
            }

            case "debug" -> {
                if(args.length - 1 < 2) {
                    Eclipse.notifyUser(String.format(
                            "Player={pos=(%s),angles=%s}",
                            Eclipse.client.player.getBlockPos().toShortString(),
                            new Vector2f(
                                    Eclipse.client.player.getYaw(),
                                    Eclipse.client.player.getPitch()
                            )
                    ));

                    break;
                }

                if(args.length - 1 < 3) {
                    float yaw;
                    float pitch;

                    try {
                        yaw = Float.parseFloat(args[1]);
                        pitch = Float.parseFloat(args[2]);
                    } catch(NumberFormatException e) {
                        Eclipse.log("Provide angles in the form: YAW PITCH");
                        break;
                    }

                    Eclipse.client.player.setYaw(yaw);
                    Eclipse.client.player.setPitch(pitch);

                    Eclipse.notifyUser("Angles set!");
                } else if(args.length - 1 < 5) {
                    double x;
                    double y;
                    double z;

                    try {
                        x = Double.parseDouble(args[1]);
                        y = Double.parseDouble(args[2]);
                        z = Double.parseDouble(args[3]);
                    } catch(NumberFormatException e) {
                        Eclipse.log("Provide coordinates in the form: X Y Z");
                        break;
                    }

                    Eclipse.client.player.setPos(x, y, z);
                    Eclipse.notifyUser("Position set!");
                } else {
                    double x;
                    double y;
                    double z;
                    float yaw;
                    float pitch;

                    try {
                        x = Double.parseDouble(args[1]);
                        y = Double.parseDouble(args[2]);
                        z = Double.parseDouble(args[3]);
                        yaw = Float.parseFloat(args[4]);
                        pitch = Float.parseFloat(args[5]);
                    } catch(NumberFormatException e) {
                        Eclipse.log("Provide data in the form: X Y Z YAW PITCH");
                        break;
                    }

                    Eclipse.client.player.setYaw(yaw);
                    Eclipse.client.player.setPitch(pitch);
                    Eclipse.client.player.setPos(x, y, z);

                    Eclipse.notifyUser("Position and angles set!");
                }
            }
        }
    }
}
