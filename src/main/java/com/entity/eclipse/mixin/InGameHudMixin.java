package com.entity.eclipse.mixin;

import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.render.Render2DEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "render", at = @At("TAIL"), cancellable = true)
    private void render2DHandler(DrawContext context, RenderTickCounter counter, CallbackInfo info) {
        boolean isCancelled = Events.Render2D.fireEvent(new Render2DEvent(
                context,
                counter
        ));

        if(isCancelled)
            info.cancel();
    }
}
