package com.entity.eclipse.commands;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.commands.base.Command;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;

public class Dupe extends Command {
    public Dupe() {
        super("Dupe", "Dupes held stack with frame dupe", "dupe");
    }

    @Override
    public void onExecute(String[] args) {
        if(Eclipse.client.world == null) return;
        if(Eclipse.client.player == null) return;
        if(Eclipse.client.interactionManager == null) return;


        boolean bothHands;
        boolean hand; // False = Main-hand, True = Off-hand

        if(args.length == 0) {
            Eclipse.notifyUser("Syntax: dupe (main|off|both)");
            return;
        }

        switch(args[0].toLowerCase()) {
            case "main" -> {
                bothHands = false;
                hand = false;
            }

            case "off" -> {
                bothHands = false;
                hand = true;
            }

            case "both" -> {
                bothHands = true;
                hand = false; // Doesn't matter
            }

            default -> {
                Eclipse.notifyUser("Invalid option: '" + args[0].toLowerCase() + "'");
                return;
            }
        }

        ItemFrameEntity frame = null;
        Iterable<Entity> entities = Eclipse.client.world.getEntities();

        for(Entity entity : entities)
            if(entity.getType() == EntityType.ITEM_FRAME && entity.getPos().distanceTo(Eclipse.client.player.getPos()) < 3.5)
                frame = (ItemFrameEntity) entity;

        if(frame == null) return;
        if(Eclipse.client.player.getMainHandStack().isEmpty()) return;

        // "Condition 'bothHands' is always 'false' when reached"
        // - IntelliJ
        if(!hand) {
            for(int i = 0; i < Eclipse.client.player.getInventory().getMainHandStack().getCount(); i++) {
                Eclipse.client.interactionManager.interactEntity(Eclipse.client.player, frame, Hand.MAIN_HAND);
                Eclipse.client.interactionManager.attackEntity(Eclipse.client.player, frame);
            }
        }

        if(hand || bothHands) {
            for(int i = 0; i < Eclipse.client.player.getInventory().offHand.get(0).getCount(); i++) {
                Eclipse.client.interactionManager.interactEntity(Eclipse.client.player, frame, Hand.OFF_HAND);
                Eclipse.client.interactionManager.attackEntity(Eclipse.client.player, frame);
            }
        }
    }
}
