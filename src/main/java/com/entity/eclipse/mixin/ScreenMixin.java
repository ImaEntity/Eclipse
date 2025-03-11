package com.entity.eclipse.mixin;

import com.entity.eclipse.utils.RunnableClickEvent;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Style;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public class ScreenMixin {
	@Inject(method = "handleTextClick", at = @At(value = "HEAD"), cancellable = true)
	private void handleRunableEvents(@Nullable Style style, CallbackInfoReturnable<Boolean> info) {
		if(style == null) return;
		if(!(style.getClickEvent() instanceof RunnableClickEvent event)) return;

		event.getRunnable().run();
		info.setReturnValue(true);
	}
}