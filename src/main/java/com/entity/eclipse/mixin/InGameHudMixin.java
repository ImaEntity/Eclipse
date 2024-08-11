package com.entity.eclipse.mixin;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.render.ModuleList;
import com.entity.eclipse.utils.Strings;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "render", at = @At("TAIL"))
    private void overlayModules(DrawContext context, RenderTickCounter counter, CallbackInfo info) {
        if(!ModuleManager.getByClass(ModuleList.class).isEnabled()) return;

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
