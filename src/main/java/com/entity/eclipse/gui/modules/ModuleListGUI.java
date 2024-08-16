package com.entity.eclipse.gui.modules;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.utils.Strings;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public class ModuleListGUI {
    public static void render(DrawContext context, RenderTickCounter counter) {
        TextRenderer textRenderer = Eclipse.client.textRenderer;
        int width = context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();

        int fontHeight = textRenderer.fontHeight;
        int offY = 0;
        int padding = 3;

        for(Module module : ModuleManager.getActiveModules()) {
            String name = Strings.camelToReadable(module.getName());
            int nameWidth = textRenderer.getWidth(name);

            context.fill(
                    width,
                    height - offY,
                    width - padding - nameWidth - padding,
                    height - offY - padding - fontHeight - padding,
                    0xEE000000
            );

            context.drawTextWithShadow(
                    textRenderer,
                    name,
                    width - padding - nameWidth,
                    height - padding - offY - fontHeight,
                    0xFFAA00
            );

            offY += padding;
            offY += fontHeight;
            offY += padding;
        }
    }
}
