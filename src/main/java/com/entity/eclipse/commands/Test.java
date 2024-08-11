package com.entity.eclipse.commands;

import com.entity.eclipse.commands.base.Command;

public class Test extends Command {
    public Test() {
        super("Test", "idfk", "test");
    }

    @Override
    public void onExecute(String[] args) {
//        if(Eclipse.client.player == null) return;
//        if(Eclipse.client.interactionManager == null) return;
//        if(Eclipse.client.world == null) return;
//
//        ItemStack heldItem = Eclipse.client.player.getMainHandStack();
//        if(!(heldItem.getItem() instanceof ArmorStandItem)) {
//            Eclipse.notifyUser("You must be holding armour stands");
//            return;
//        }
//
//        int range = 1;
//        int offX = -range;
//        int offZ = -range;
//
//        for(int i = 0; i < heldItem.getCount(); i++) {
//            int finalOffX = offX;
//            int finalOffZ = offZ;
//
//            Ticker.queue(() -> {
//                Eclipse.client.interactionManager.interactBlock(
//                        Eclipse.client.player,
//                        Hand.MAIN_HAND,
//                        new BlockHitResult(
//                                Eclipse.client.player.getPos().add(finalOffX, 0, finalOffZ),
//                                Direction.UP,
//                                Eclipse.client.player.getBlockPos().add(finalOffX, 0, finalOffZ),
//                                true
//                        )
//                );
//            });
//
//            offX++;
//
//            if(offX > range) {
//                offX = -range;
//                offZ++;
//            }
//
//            if(offZ > range)
//                break;
//        }
//
//        int finalRange = 4;
//
//        Ticker.queue(() -> {
//            Iterable<Entity> entities = Eclipse.client.world.getEntities();
//
//            for(Entity entity : entities) {
//                if(Eclipse.client.player.distanceTo(entity) > finalRange) continue;
//                if(!(entity instanceof ArmorStandEntity)) continue;
//
//                Ticker.queue(() -> {
//                    Eclipse.client.interactionManager.attackEntity(
//                            Eclipse.client.player,
//                            entity
//                    );
//
//                    Eclipse.client.interactionManager.attackEntity(
//                            Eclipse.client.player,
//                            entity
//                    );
//                });
//            }
//        });
    }
}
