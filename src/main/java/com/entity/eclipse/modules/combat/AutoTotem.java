package com.entity.eclipse.modules.combat;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.Slots;
import com.entity.eclipse.utils.events.render.RenderEvent;
import com.entity.eclipse.utils.types.BooleanValue;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class AutoTotem extends Module {
    public AutoTotem() {
        super("AutoTotem", "Automatically puts totems in one of your hands", ModuleType.COMBAT);

        this.config.create("HoldInMainHand", new BooleanValue(false));
    }

    @Override
    public void tick() {
        if(Eclipse.client.player == null) return;
        if(Eclipse.client.interactionManager == null) return;

        boolean isMainHand = this.config.get("HoldInMainHand");

        ItemStack mainStack = Eclipse.client.player.getMainHandStack();
        ItemStack offStack = Eclipse.client.player.getOffHandStack();
        ItemStack destination = isMainHand ? mainStack : offStack;

        if(destination.getItem() == Items.TOTEM_OF_UNDYING) return;

        int sourceIndex = -1;

        for(int i = 0; i < Eclipse.client.player.getInventory().size(); i++) {
            ItemStack stack = Eclipse.client.player.getInventory().getStack(i);

            if(stack.getItem() == Items.TOTEM_OF_UNDYING) {
                sourceIndex = i;
                break;
            }
        }

        if(sourceIndex == -1) return;

        int sourceSlot = Slots.indexToID(sourceIndex);
        int destSlot = isMainHand ? Eclipse.client.player.getInventory().selectedSlot + 36 : Slots.OFFHAND;

        Slots.swap(sourceSlot, destSlot);
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