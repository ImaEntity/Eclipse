package com.entity.eclipse.utils;

import com.entity.eclipse.Eclipse;
import net.minecraft.screen.slot.SlotActionType;
import org.lwjgl.glfw.GLFW;

public class Slots {
    public record Range(int start, int end) {
        public boolean contains(int slot) {
            return slot >= this.start && slot <= this.end;
        }
    }

    public static final int INVALID_SLOT = -1;
    public static final Range HOTBAR = new Range(0, 8);
    public static final Range MAIN = new Range(9, 35);
    public static final Range ARMOR = new Range(36, 39);
    public static final int OFFHAND = 40;

    public static int indexToID(int slotIndex) {
        return HOTBAR.contains(slotIndex) ? slotIndex + 36 :
                MAIN.contains(slotIndex) ? slotIndex :
                ARMOR.contains(slotIndex) ? 5 + slotIndex - 36 :
                slotIndex == OFFHAND ? 45 :
                INVALID_SLOT;
    }

    public static int getSelectedID() {
        if(Eclipse.client.player == null) return INVALID_SLOT;
        if(Eclipse.client.player.getInventory() == null) return INVALID_SLOT;

        return indexToID(Eclipse.client.player.getInventory().selectedSlot);
    }

    public static void swap(int source, int destination) {
        if(Eclipse.client.player == null) return;
        if(Eclipse.client.interactionManager == null) return;

        if(source == -1) return;
        if(destination == -1) return;

        Eclipse.client.interactionManager.clickSlot(
                Eclipse.client.player.playerScreenHandler.syncId,
                source,
                GLFW.GLFW_MOUSE_BUTTON_LEFT,
                SlotActionType.PICKUP,
                Eclipse.client.player
        );

        Eclipse.client.interactionManager.clickSlot(
                Eclipse.client.player.playerScreenHandler.syncId,
                destination,
                GLFW.GLFW_MOUSE_BUTTON_LEFT,
                SlotActionType.PICKUP,
                Eclipse.client.player
        );

        if(Eclipse.client.player.currentScreenHandler.getCursorStack().isEmpty()) return;

        Eclipse.client.interactionManager.clickSlot(
                Eclipse.client.player.playerScreenHandler.syncId,
                source,
                GLFW.GLFW_MOUSE_BUTTON_LEFT,
                SlotActionType.PICKUP,
                Eclipse.client.player
        );
    }
}
