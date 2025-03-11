package com.entity.eclipse.modules.render;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.Strings;
import com.entity.eclipse.utils.events.render.Render2DEvent;
import com.entity.eclipse.utils.events.render.Render3DEvent;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public class ModuleList extends Module {
    public ModuleList() {
        super("ModuleList", "Shows a module list in the bottom right.", ModuleType.RENDER);
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
        DrawContext context = event.getContext();
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
