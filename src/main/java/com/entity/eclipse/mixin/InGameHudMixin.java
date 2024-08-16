package com.entity.eclipse.mixin;

import com.entity.eclipse.gui.modules.ItemInfoGUI;
import com.entity.eclipse.gui.modules.ModuleListGUI;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.render.ItemInfo;
import com.entity.eclipse.modules.render.ModuleList;
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
        Module moduleList = ModuleManager.getByClass(ModuleList.class);
        Module itemInfo = ModuleManager.getByClass(ItemInfo.class);

        if(moduleList == null) return;
        if(itemInfo == null) return;

        if(moduleList.isEnabled())
            ModuleListGUI.render(context, counter);

        if(itemInfo.isEnabled())
            ItemInfoGUI.render(context, counter);
    }
}
