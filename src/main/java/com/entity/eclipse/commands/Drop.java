package com.entity.eclipse.commands;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.commands.base.Command;
import com.entity.eclipse.utils.Slots;
import com.entity.eclipse.utils.types.ItemValue;
import net.minecraft.item.Item;

public class Drop extends Command {
    public Drop() {
        super("Drop", "drops some shit", "drop");
    }

    @Override
    public void onExecute(String[] args) {
        if(Eclipse.client.player == null) return;

        if(Eclipse.client.player.isSpectator()) {
            Eclipse.notifyUser("You can't drop items in spectator!");
            return;
        }

        if(args.length == 0) {
            Slots.drop(Slots.indexToID(Eclipse.client.player.getInventory().selectedSlot), true);
            return;
        }

        switch(args[0].toLowerCase()) {
            case "hand" -> Slots.drop(Slots.indexToID(Eclipse.client.player.getInventory().selectedSlot), true);
            case "offhand" -> Slots.drop(Slots.indexToID(Slots.OFFHAND), true);
            case "hotbar" -> {
                for(int i = 0; i < Slots.HOTBAR.size(); i++)
                    Slots.drop(Slots.indexToID(Slots.HOTBAR.start() + i), true);
            }

            case "inventory" -> {
                for(int i = 0; i < Slots.MAIN.size(); i++)
                    Slots.drop(Slots.indexToID(Slots.MAIN.start() + i), true);
            }

            case "armor" -> {
                for(int i = 0; i < Slots.ARMOR.size(); i++)
                    Slots.drop(Slots.indexToID(Slots.ARMOR.start() + i), true);
            }

            case "all" -> {
                for(int i = 0; i < Slots.ALL.size(); i++)
                    Slots.drop(Slots.indexToID(Slots.ALL.start() + i), true);
            }

            case "item" -> {
                if(args.length < 2) {
                    Eclipse.notifyUser("Provide an item to drop!");
                    break;
                }

                Item item;

                try {
                    item = new ItemValue().fromString(args[1]).getValue();
                } catch(NullPointerException e) {
                    Eclipse.notifyUser("Invalid item name!");
                    break;
                }

                for(int i = 0; i < Slots.ALL.size(); i++) {
                    if(!Eclipse.client.player.getInventory().getStack(i).isOf(item))
                        continue;

                    Slots.drop(Slots.indexToID(Slots.ALL.start() + i), true);
                }
            }

            default -> Eclipse.notifyUser("Invalid parameter: '" + args[0].toLowerCase() + "'");
        }
    }
}
