package com.entity.eclipse.gui.modules;

import com.entity.eclipse.Eclipse;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.item.ItemStack;

public class ItemInfoGUI {
    public static void render(DrawContext context, RenderTickCounter counter) {
        if(Eclipse.client.player == null) return;

        TextRenderer textRenderer = Eclipse.client.textRenderer;
        int width = context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();

        int fontHeight = textRenderer.fontHeight;
        int padding = 3;

        ItemStack stack = Eclipse.client.player.getMainHandStack();
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

        context.drawItemInSlot(
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
