package com.entity.eclipse.modules.player;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.Slots;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.block.BlockEvents;
import com.entity.eclipse.utils.events.render.Render2DEvent;
import com.entity.eclipse.utils.events.render.Render3DEvent;
import com.entity.eclipse.utils.types.BooleanValue;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.registry.entry.RegistryEntry;

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

        int effLvl = 0;
        for(Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : stack.getEnchantments().getEnchantmentEntries())
            if(entry.getKey().matchesKey(Enchantments.EFFICIENCY)) effLvl = entry.getIntValue();

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

        if(stack.getMaxDamage() - stack.getDamage() <= 1 && (boolean) this.config.get("PreventToolBreaking"))
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
    public void renderWorld(Render3DEvent event) {

    }

    @Override
    public void renderScreen(Render2DEvent event) {

    }
}
