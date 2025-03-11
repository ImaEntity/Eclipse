package com.entity.eclipse.modules.render;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.Render2DEvent;
import com.entity.eclipse.utils.events.render.Render3DEvent;
import com.entity.eclipse.utils.types.BooleanValue;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;

public class ItemInfo extends Module {
    public ItemInfo() {
        super("ItemInfo", "Shows info about your held item in the bottom left.", ModuleType.RENDER);

        this.config.create("ShowOffhand", new BooleanValue(false));
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
        if(Eclipse.client.player == null) return;

        DrawContext context = event.getContext();
        TextRenderer textRenderer = Eclipse.client.textRenderer;
        int height = context.getScaledWindowHeight();

        int fontHeight = textRenderer.fontHeight;
        int padding = 3;

        ItemStack stack = Eclipse.client.player.getMainHandStack();

        if((boolean) this.config.get("ShowOffhand"))
            stack = Eclipse.client.player.getOffHandStack();

        if(stack.isEmpty()) return;

        int durability = stack.getMaxDamage() - stack.getDamage();
        float percent = 100f * (float) durability / (float) stack.getMaxDamage();

        String[] text = {
                String.format("%.2f%% (%d / %d)", percent, durability, stack.getMaxDamage()),
                stack.getName().getString()
        };

        int longestText = 0;
        for(int i = 0; i < text.length; i++) {
            if(i == 0 && !stack.isDamageable()) continue;

            if(textRenderer.getWidth(text[i]) > longestText)
                longestText = textRenderer.getWidth(text[i]);
        }

        context.fill(
                0,
                height - text.length * (fontHeight + padding) - padding,
                padding + 16 + padding + padding + longestText + padding,
                height,
                0xEE000000
        );

        int itemY = height - text.length * (fontHeight + padding) / 2 - 8;

        context.drawItem(
                stack,
                padding,
                itemY
        );

        context.drawStackOverlay(
                textRenderer,
                stack,
                padding,
                itemY
        );

        for(int i = 0; i < text.length; i++) {
            if(i == 0 && !stack.isDamageable()) continue;

            context.drawTextWithShadow(
                    textRenderer,
                    text[i],
                    padding + 16 + padding + padding,
                    height - (i + 1) * (padding + fontHeight),
                    0xFFAA00
            );
        }
    }
}
