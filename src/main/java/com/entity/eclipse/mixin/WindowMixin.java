package com.entity.eclipse.mixin;

import com.entity.eclipse.Eclipse;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public class WindowMixin {
	@Inject(at = @At("HEAD"), method = "setTitle", cancellable = true)
	public void setTitle(String title, CallbackInfo info) {
		GLFW.glfwSetWindowTitle(
				MinecraftClient.getInstance().getWindow().getHandle(),
				title + " - Eclipse " + Eclipse.VERSION
		);

		info.cancel();
	}
}