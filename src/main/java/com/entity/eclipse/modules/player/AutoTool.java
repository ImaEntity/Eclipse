package com.entity.eclipse.modules.player;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.Slots;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.block.BlockEvents;
import com.entity.eclipse.utils.events.render.RenderEvent;
import com.entity.eclipse.utils.types.BooleanValue;
import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Optional;

public class AutoTool extends Module {
    private int prevSlot = Slots.INVALID_SLOT;

    public AutoTool() {
        super("AutoTool", "Automatically selects the best tool.", ModuleType.PLAYER);

        this.config.create("UseSwords", new BooleanValue(true));
        this.config.create("UseHands", new BooleanValue(true));
        this.config.create("SwitchBack", new BooleanValue(false));
        this.config.create("PreventToolBreaking", new BooleanValue(true));
        this.config.create("AllowInventory", new BooleanValue(false));

        Events.Block.register(BlockEvents.DAMAGE, event -> {
            if(!this.isEnabled()) return;

            if(Eclipse.client.player == null) return;
            if(event.getState().getBlock() instanceof AirBlock) return;

            int bestSlot = findBestTool(event.getState());
            if(bestSlot == Slots.INVALID_SLOT) return;

            if((boolean) this.config.get("SwitchBack") && this.prevSlot == Slots.INVALID_SLOT)
                this.prevSlot = Slots.getSelectedID();

            if(!Slots.HOTBAR.contains(bestSlot)) {
                Slots.swap(
                        Slots.indexToID(bestSlot),
                        Slots.getSelectedID()
                );
            } else {
                Eclipse.client.player.getInventory().selectedSlot = bestSlot;
            }
        });

        Events.Block.register(BlockEvents.BREAK, event -> {
            if(!this.isEnabled()) return;

            if(Eclipse.client.player == null) return;

            if(!(boolean) this.config.get("SwitchBack")) return;
            if(this.prevSlot == Slots.INVALID_SLOT) return;

            Eclipse.client.player.getInventory().selectedSlot = this.prevSlot;
            this.prevSlot = Slots.INVALID_SLOT;
        });
    }

    private float getMiningSpeed(ItemStack stack, BlockState state) {
        if(Eclipse.client.world == null) return 1f;

        float mlt = stack.getMiningSpeedMultiplier(state);
        if(mlt <= 1f) return Float.NaN;

        DynamicRegistryManager drm = Eclipse.client.world.getRegistryManager();
        Registry<Enchantment> registry = drm.get(RegistryKeys.ENCHANTMENT);
        Optional<RegistryEntry.Reference<Enchantment>> efficiency = registry.getEntry(Enchantments.EFFICIENCY);
        int effLvl = efficiency
                .map(entry -> EnchantmentHelper.getLevel(entry, stack))
                .orElse(0);

        if(effLvl > 0 && !stack.isEmpty())
            return mlt + effLvl * effLvl + 1;

        return mlt;
    }

    private float calculateScore(ItemStack stack, BlockState state) {
        float score = 0;
        float speed = getMiningSpeed(stack, state);
        float percent = stack.isDamageable() ?
                1f - (float) stack.getDamage() / stack.getMaxDamage() :
                1f;

        if(percent <= 0.05f && (boolean) this.config.get("PreventToolBreaking"))
            return Float.NaN;

        if(Float.isNaN(speed))
            return Float.NaN;

        score += percent;
        score += 10f * speed;

        return score;
    }

    private int getHandSlot() {
        if(!(boolean) this.config.get("UseHands")) return Slots.INVALID_SLOT;

        int endSlot = this.config.get("AllowInventory") ?
                Slots.MAIN.end() :
                Slots.HOTBAR.end();

        return Slots.findBest(
                new Slots.Range(Slots.HOTBAR.start(), endSlot),
                stack -> slotIdx -> {
                    if(stack.isDamageable())
                        return Double.NaN;

                    float score = 0f;

                    score += stack.isEmpty() ? 5f : 0f;
                    if(!stack.isEmpty() && Slots.MAIN.contains(slotIdx))
                        score -= 10f;

                    return (double) score;
                }
        );
    }

    private int findBestTool(BlockState state) {
        if(Eclipse.client.player == null) return Slots.getSelectedID();

        if(Eclipse.client.player.getAbilities().creativeMode) return Slots.getSelectedID();
        if(Eclipse.client.player.isCreative()) return Slots.getSelectedID();

        int endSlot = this.config.get("AllowInventory") ?
                Slots.MAIN.end() :
                Slots.HOTBAR.end();

        int slot = Slots.findBest(
                new Slots.Range(Slots.HOTBAR.start(), endSlot),
                stack -> slotIdx -> {
                    float score = calculateScore(stack, state);
                    if(Float.isNaN(score)) return (double) Float.NaN;

                    if(
                            !(boolean) this.config.get("UseSwords") &&
                            stack.getItem() instanceof SwordItem
                    ) return (double) Float.NaN;

                    return (double) score;
                }
        );

        return slot == Slots.INVALID_SLOT ?
                getHandSlot() :
                slot;
    }

    @Override
    public void tick() {

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
