package com.entity.eclipse.modules.misc;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.packet.PacketEvents;
import com.entity.eclipse.utils.events.render.Render2DEvent;
import com.entity.eclipse.utils.events.render.Render3DEvent;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket
        ;

import java.util.HashMap;

public class Test extends Module {
    private HashMap<String, Object> data = new HashMap<>();

    public Test() {
        super("Test", "makes my life easier", ModuleType.MISC);

        Events.Packet.register(PacketEvents.SEND, event -> {
            if(!this.isEnabled()) return;
            if(!(event.getPacket() instanceof PlayerInteractBlockC2SPacket)) return;

            if((int) this.data.get("ignorePacket") > 0) {
                this.data.put("ignorePacket", (int) this.data.get("ignorePacket") - 1);
                if((int) this.data.get("ignorePacket") == 0)
                    this.data.put("packet", null);

                return;
            }

            this.data.put("packet", event.getPacket());

            this.data.put("ignorePacket", 4);
            event.setCancelled(true);


//            if(!(event.getPacket() instanceof ClickSlotC2SPacket pack)) return;

//            this.notifyUser(String.format(
//                    "%d (button %d) -> %s",
//                    pack.slot(),
//                    pack.button(),
//                    pack.actionType().name()
//            ));
        });

        Events.Packet.register(PacketEvents.RECEIVE, event -> {
            if(!this.isEnabled()) return;
        });
    }

    @Override
    public void tick() {
        if(Eclipse.client.world == null) return;
        if(Eclipse.client.player == null) return;
        if(Eclipse.client.interactionManager == null) return;
        if(Eclipse.client.getNetworkHandler() == null) return;

        if(this.data.get("packet") == null) return;
        Eclipse.client.getNetworkHandler().sendPacket((Packet<?>) this.data.get("packet"));

//        Eclipse.client.interactionManager.interactBlock(
//                Eclipse.client.player,
//                Hand.MAIN_HAND,
//                HitResultBuilders.createBlock(new BlockPos(1709, 80, -3839), Direction.UP)
//        );

//        if(Eclipse.client.player.getOffHandStack().isOf(Items.EXPERIENCE_BOTTLE)) {
//            PlayerUtils.serverLookAt(EntityAnchorArgumentType.EntityAnchor.FEET, Eclipse.client.player.getPos().add(0, -10, 0));
//            Eclipse.client.interactionManager.interactItem(Eclipse.client.player, Hand.OFF_HAND);
//            return;
//        }
//
//        for(int i = 0; i <= Slots.MAIN.end(); i++) {
//            if(!Eclipse.client.player.getInventory().getStack(i).isOf(Items.EXPERIENCE_BOTTLE))
//                continue;
//
//            Eclipse.client.interactionManager.clickSlot(
//                    Eclipse.client.player.currentScreenHandler.syncId,
//                    Slots.indexToID(i),
//                    Slots.OFFHAND,
//                    SlotActionType.SWAP,
//                    Eclipse.client.player
//            );
//
//            return;
//        }

//        if((int) data.get("timer") > 20) {
//            Eclipse.client.getNetworkHandler().sendChatCommand("sellall inventory");
//            data.put("timer", 0);
//            return;
//        }
//
//        data.put("timer", (int) data.get("timer") + 1);

//        ItemStack stack = Eclipse.client.player.getMainHandStack();
//        if(stack.getMaxDamage() - stack.getDamage() < 10) return;
//
//        int breakTimer = 20; // 30 ticks to break with haste II eff V
//        double range = 4.5;
//
//        if(this.data.get("blockPos") != null) {
//            BlockPos pos = (BlockPos) this.data.get("blockPos");
//
//            Eclipse.client.player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, pos.toCenterPos().add(0, 0.5, 0));
//            if((int) this.data.get("timer") % 4 == 0)
//                Eclipse.client.player.swingHand(Hand.MAIN_HAND);
//
//            if((int) this.data.get("timer") == 0) {
//                Eclipse.client.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(
//                        PlayerActionC2SPacket.Action.START_DESTROY_BLOCK,
//                        pos,
//                        Direction.UP
//                ));
//            } else if((int) this.data.get("timer") >= breakTimer) {
//                Eclipse.client.interactionManager.breakBlock(pos);
//                Eclipse.client.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(
//                        PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK,
//                        pos,
//                        Direction.UP
//                ));
//
//                this.data.put("timer", -1);
//                this.data.put("blockPos", null);
//            }
//
//            this.data.put("timer", (int) this.data.get("timer") + 1);
//            return;
//        }
//
//        if((int) this.data.get("cycleIdx") != -1) {
//            BlockPos playerPos = (BlockPos) this.data.get("playerPos");
//            boolean tookAction = false;
//
//            int i = -1;
//            for(int zOff = (int) -Math.ceil(range); zOff <= Math.ceil(range); zOff++) {
//                for(int xOff = (int) -Math.ceil(range); xOff <= Math.ceil(range); xOff++) {
//                    if(xOff * xOff + zOff * zOff > range * range)
//                        continue;
//
//                    BlockPos pos = playerPos.add(xOff, 0, zOff);
//
//                    if(!Eclipse.client.world.getBlockState(pos).isOf(Blocks.AIR)) continue;
//                    if(!Eclipse.client.world.getBlockState(pos.down()).isOf(Blocks.OBSIDIAN)) continue;
//
//                    i++;
//                    if(i != (int) this.data.get("cycleIdx")) continue;
//
//                    Eclipse.client.player.setPosition(pos.toBottomCenterPos());
//                    tookAction = true;
//                }
//            }
//
//            this.data.put("cycleIdx", (int) this.data.get("cycleIdx") + 1);
//
//            if(!tookAction) {
//                this.data.put("cycleIdx", -1);
//                this.data.put("blockPos", playerPos.down());
//
//                Eclipse.client.player.setPosition(playerPos.toBottomCenterPos());
//            }
//
//            return;
//        }
//
//        for(int yOff = 0; yOff <= 0; yOff++) {
//            for(int zOff = (int) -Math.ceil(range); zOff <= Math.ceil(range); zOff++) {
//                for(int xOff = (int) -Math.ceil(range); xOff <= Math.ceil(range); xOff++) {
//                    if(xOff * xOff + yOff * yOff + zOff * zOff > range * range)
//                        continue;
//
//                    BlockPos pos = Eclipse.client.player.getBlockPos()
//                            .add(xOff, yOff, zOff);
//
//                    BlockState state = Eclipse.client.world.getBlockState(pos);
//                    if(!state.isOf(Blocks.OBSIDIAN)) continue;
//
//                    if(stack.getMaxDamage() - stack.getDamage() < 10) return;
//
//                    this.data.put("blockPos", pos);
//                    return;
//                }
//            }
//        }
//
//        this.data.put("cycleIdx", 0);
//        this.data.put("playerPos", Eclipse.client.player.getBlockPos());
//
//        ItemStack stack = Eclipse.client.player.getMainHandStack();
//        if(!stack.isOf(Items.SPRUCE_PLANKS)) return;

//        ItemStack stack = Eclipse.client.player.getMainHandStack();
//        if(stack.getMaxDamage() - stack.getDamage() < 10) return;

//        int actions = 0;
//        double range = 3;
//
//        for(int yOff = -1; yOff <= (int) Math.ceil(range); yOff++) {
//            for(int zOff = (int) -Math.ceil(range); zOff <= Math.ceil(range); zOff++) {
//                for(int xOff = (int) -Math.ceil(range); xOff <= Math.ceil(range); xOff++) {
//                    if(xOff * xOff + yOff * yOff + zOff * zOff > range * range)
//                        continue;
//
//                    BlockPos pos = Eclipse.client.player.getBlockPos()
//                            .add(xOff, yOff, zOff);
//
//                    BlockState state = Eclipse.client.world.getBlockState(pos);
////                    if(!state.isIn(BlockTags.PICKAXE_MINEABLE)) continue;
//                    if(!state.isOf(Blocks.DEEPSLATE_LAPIS_ORE)) continue;
//
//                    if(actions >= 10) return;
//                    actions++;
//
//                    Eclipse.client.interactionManager.attackBlock(
//                            pos,
//                            Direction.UP
//                    );
//                }
//            }
//        }
    }

    @Override
    public void onEnable() {
//        this.data.put("blockPos", null);
//        this.data.put("cycleIdx", -1);
//        this.data.put("playerPos", null);
        this.data.put("timer", 0);
        this.data.put("ignorePacket", 0);

//        MutableInput input = new MutableInput();
//        this.data.put("prevInput", Eclipse.client.player.input);
//        this.data.put("input", input);
//        Eclipse.client.player.input = input;
//
//        this.data.put("forwardDir", Direction.WEST);
//        this.data.put("rightDir", ((Direction) this.data.get("forwardDir")).rotateYClockwise());
    }

    @Override
    public void onDisable() {
//        Eclipse.client.player.input = (Input) this.data.get("prevInput");
//        this.data.put("input", null);
    }

    @Override
    public void renderWorld(Render3DEvent event) {

    }

    @Override
    public void renderScreen(Render2DEvent event) {

    }
}
