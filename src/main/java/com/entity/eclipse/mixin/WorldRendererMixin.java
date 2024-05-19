package com.entity.eclipse.mixin;

import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.render.AntiBlind;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
	@Inject(at = @At("HEAD"), method = "hasBlindnessOrDarkness", cancellable = true)
	private void removeDark(Camera camera, CallbackInfoReturnable<Boolean> info) {
		if(ModuleManager.getByClass(AntiBlind.class).isEnabled())
			info.setReturnValue(false);
	}
}