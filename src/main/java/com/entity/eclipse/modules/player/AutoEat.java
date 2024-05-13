package com.entity.eclipse.modules.player;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.Slots;
import com.entity.eclipse.utils.events.render.RenderEvent;
import com.entity.eclipse.utils.types.BooleanValue;
import com.entity.eclipse.utils.types.FloatValue;
import com.entity.eclipse.utils.types.ItemValue;
import com.entity.eclipse.utils.types.ListValue;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;

public class AutoEat extends Module {
    private boolean eating = false;
    private int prevSlot = Slots.INVALID_SLOT;
    private int slot = Slots.INVALID_SLOT;

    public AutoEat() {
        super("AutoEat", "Automatically eats food.", ModuleType.PLAYER);

        this.config.create("AllowInventory", new BooleanValue(true));
        this.config.create("HealthThreshold", new FloatValue(10f));
        this.config.create("HungerThreshold", new FloatValue(15f));
        this.config.create("BlacklistedItems", new ListValue(
                ItemValue.class,
                "enchanted_golden_apple",
                "golden_apple",
                "chorus_fruit",
                "poisonous_potato",
                "pufferfish",
                "chicken",
                "rotten_flesh",
                "spider_eye",
                "suspicious_stew"
        ));
    }

    private int findFood() {
        if(Eclipse.client.player == null) return Slots.INVALID_SLOT;

        int startSlot = this.config.get("AllowInventory") ?
                Slots.MAIN.end() :
                Slots.HOTBAR.end();

        int slot = Slots.INVALID_SLOT;
        int bestNutrition = -1;

        // This works only because the hotbar and main inventory
        // just happen to be stored right next to each other
        for(int i = startSlot; i >= Slots.HOTBAR.start(); i--) {
            Item item = Eclipse.client.player.getInventory().getStack(i).getItem();
            FoodComponent food = item.getComponents().get(DataComponentTypes.FOOD);

            if(food == null) continue;
            if(food.nutrition() < bestNutrition) continue;
            if(((ListValue) this.config.getRaw("BlacklistedItems")).contains(item)) continue;

            slot = i;
            bestNutrition = food.nutrition();
        }

        Item offhand = Eclipse.client.player.getOffHandStack().getItem();
        FoodComponent food = offhand.getComponents().get(DataComponentTypes.FOOD);

        if(food == null) return slot;
        if(food.nutrition() < bestNutrition) return slot;
        if(((ListValue) this.config.getRaw("BlacklistedItems")).contains(offhand)) return slot;

        return Slots.OFFHAND;
    }

    private void stopEating() {
        if(Eclipse.client.player == null) return;

        this.eating = false;
        Eclipse.client.options.useKey.setPressed(false);

        if(!Slots.HOTBAR.contains(this.slot)) {
            Slots.swap(
                    Slots.getSelectedID(),
                    Slots.indexToID(this.slot)
            );
        } else
            Eclipse.client.player.getInventory().selectedSlot = this.prevSlot;

        this.slot = Slots.INVALID_SLOT;
        this.prevSlot = Slots.INVALID_SLOT;
    }

    private void startEating() {
        if(Eclipse.client.player == null) return;

        this.slot = findFood();
        if(this.slot == Slots.INVALID_SLOT) return;

        this.eating = true;
        this.prevSlot = Eclipse.client.player.getInventory().selectedSlot;

        if(!Slots.HOTBAR.contains(this.slot)) {
            Slots.swap(
                    Slots.indexToID(this.slot),
                    Slots.getSelectedID()
            );
        } else
            Eclipse.client.player.getInventory().selectedSlot = this.slot;

        Eclipse.client.options.useKey.setPressed(true);
    }

    @Override
    public void tick() {
        if(Eclipse.client.player == null) return;

        boolean health = Eclipse.client.player.getHealth() <= (float) this.config.get("HealthThreshold");
        boolean hunger = Eclipse.client.player.getHungerManager().getFoodLevel() <= (float) this.config.get("HungerThreshold");
        boolean shouldEat = health || hunger;

        if(this.eating && !shouldEat) this.stopEating();
        if(!this.eating && shouldEat) this.startEating();

        if(this.eating && shouldEat) {
            if(Eclipse.client.player.getMainHandStack().get(DataComponentTypes.FOOD) != null) {
                if(Eclipse.client.player.isUsingItem()) return;

                Eclipse.client.options.useKey.setPressed(false);
                Eclipse.client.options.useKey.setPressed(true);
            }

            this.stopEating();
            this.startEating();
        }
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
