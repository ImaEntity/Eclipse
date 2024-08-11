package com.entity.eclipse.modules.render;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.lore.LoreEvents;
import com.entity.eclipse.utils.events.render.RenderEvent;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;

public class DamagePerSecond extends Module {
    public DamagePerSecond() {
        super("DamagePerSecond", "Shows the DPS of items on their tooltip.", ModuleType.RENDER);

        // I'm gonna fucking go insane!
        Events.Lore.register(LoreEvents.REQUEST, event -> {
            if(!this.isEnabled()) return;

            if(Eclipse.client.player == null) return;

            Text attackSpeedText = null;
            Text attackDamageText = null;
            double attackSpeed = Eclipse.client.player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_SPEED);
            double attackDamage = Eclipse.client.player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) +
                    0f; // This is supposed to take enchantments into account.

            AttributeModifiersComponent component = event.getParent().getOrDefault(
                    DataComponentTypes.ATTRIBUTE_MODIFIERS,
                    AttributeModifiersComponent.DEFAULT
            );

            if(component == AttributeModifiersComponent.DEFAULT) return;

            for(AttributeModifiersComponent.Entry entry : component.modifiers()) {
                EntityAttributeModifier modifier = entry.modifier();

                if(modifier.id() == Item.BASE_ATTACK_DAMAGE_MODIFIER_ID) {
                    attackDamage += modifier.value();
                    attackDamageText = Text.translatable(
                            "attribute.modifier.equals." + modifier.operation().getId(),
                            AttributeModifiersComponent.DECIMAL_FORMAT.format(attackDamage),
                            Text.translatable(entry.attribute().value().getTranslationKey())
                    );
                }

                if(modifier.id() == Item.BASE_ATTACK_SPEED_MODIFIER_ID) {
                    attackSpeed += modifier.value();
                    attackSpeedText = Text.translatable(
                            "attribute.modifier.equals." + modifier.operation().getId(),
                            AttributeModifiersComponent.DECIMAL_FORMAT.format(attackSpeed),
                            Text.translatable(entry.attribute().value().getTranslationKey())
                    );
                }
            }

            if(attackDamageText == null) return;
            if(attackSpeedText == null) return;

            double dps = attackDamage * attackSpeed;
            int lineNumber = -1;

            for(Text line : event.getLore()) {
                if(line.contains(attackSpeedText))
                    lineNumber = event.findLoreLine(line);
            }

            if(lineNumber == -1) return;

            event.insertLoreLine(lineNumber + 1, Text.of(
                    " §2" +
                    AttributeModifiersComponent.DECIMAL_FORMAT.format(dps) +
                    " Damage Per Second§r"
            ));
        });
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
