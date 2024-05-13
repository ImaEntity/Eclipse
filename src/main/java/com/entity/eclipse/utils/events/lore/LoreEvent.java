package com.entity.eclipse.utils.events.lore;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

public class LoreEvent {
    private final ItemStack parent;
    private final List<Text> lore;
    private boolean cancelled = false;

    public LoreEvent(ItemStack parent, List<Text> lore) {
        this.parent = parent;
        this.lore = lore;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    public ItemStack getParent() {
        return this.parent;
    }
    public List<Text> getLore() {
        return this.lore;
    }

    public int loreLineCount() {
        return this.lore.size();
    }
    public Text getLoreLine(int line) {
        return this.lore.get(line);
    }
    public void setLoreLine(int line, Text lore) {
        this.lore.set(line, lore);
    }
    public void addLoreLine(Text lore) {
        this.lore.add(lore);
    }
    public void insertLoreLine(int line, Text lore) {
        this.lore.add(line, lore);
    }
    public Text removeLoreLine(int line) {
        return this.lore.remove(line);
    }
    public int findLoreLine(Text lore) {
        return this.lore.indexOf(lore);
    }
}
