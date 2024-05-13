package com.entity.eclipse.gui;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ModuleList extends Screen {
    public ModuleList() {
        super(Text.of("Module List"));
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float tickDelta) {
        int fontHeight = this.textRenderer.fontHeight;
        int offY = (int) (fontHeight / 1.5);
        int padding = Math.ceilDiv(fontHeight, 2);
        int bottomY = this.height - padding;
        int rightX = this.width - padding;

        for(Module module : ModuleManager.getActiveModules()) {
            int nameWidth = this.textRenderer.getWidth(module.getName());
            // int descWidth = this.textRenderer.getWidth(module.getDescription());

            context.fill(
                    rightX + padding,
                    bottomY - padding - offY,
                    rightX - nameWidth - /* 50 + descWidth + */ padding,
                    bottomY + padding - offY + (int) (fontHeight / 1.5),
                    0xEE000000
            );

            context.drawTextWithShadow(
                    this.textRenderer,
                    module.getName(),
                    rightX - nameWidth,
                    bottomY - offY,
                    0xFFFFCC66
            );

            /*
            context.drawTextWithShadow(
                    this.textRenderer,
                    module.getDescription(),
                    padding + nameWidth + 50,
                    bottomY - offY,
                    0xFFFFCC66
            );
            */

            // +3 ??? I think it's cause int div errors
            offY += fontHeight * 1.5 + 3;
        }
    }
}
