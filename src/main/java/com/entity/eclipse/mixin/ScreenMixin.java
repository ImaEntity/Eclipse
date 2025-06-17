package com.entity.eclipse.mixin;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.gui.ClickGUI;
import com.entity.eclipse.gui.ClientSettingsGUI;
import com.entity.eclipse.gui.ModuleSettingsGUI;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.movement.InventoryMove;
import com.entity.eclipse.utils.RunnableClickEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Style;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public class ScreenMixin {
	@Shadow
	public int width;

	@Shadow
	public int height;

	int lastMouseX = -1;
	int lastMouseY = -1;

	@Inject(method = "handleTextClick", at = @At(value = "HEAD"), cancellable = true)
	private void handleRunnableEvents(@Nullable Style style, CallbackInfoReturnable<Boolean> info) {
		if(style == null) return;
		if(!(style.getClickEvent() instanceof RunnableClickEvent event)) return;

		event.getRunnable().run();
		info.setReturnValue(true);
	}

	// render method gets mouse coords and is called every tick
	@Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
	private void handleLook(DrawContext context, int mouseX, int mouseY, float tickDelta, CallbackInfo info) {
		if(Eclipse.client.player == null) return;
		if(this.lastMouseX == -1 || this.lastMouseY == -1) {
			this.lastMouseX = mouseX;
			this.lastMouseY = mouseY;

			return;
		}

		switch(Eclipse.client.currentScreen) {
			case null                -> { return; }
			case ChatScreen c        -> { return; }
			case ClickGUI c          -> { return; }
			case ClientSettingsGUI c -> { return; }
			case ModuleSettingsGUI m -> { return; }
			default -> {}
		}

		Module invMove = ModuleManager.getByClass(InventoryMove.class);
		if(invMove == null) return;

		if(!invMove.isEnabled()) return;
		if(!(boolean) invMove.config.get("UseMouse")) return;

		float yawOff = (float) (mouseX - this.lastMouseX) / (5f / (float) invMove.config.get("MouseSensitivity"));
		float pitchOff = (float) (mouseY - this.lastMouseY) / (5f / (float) invMove.config.get("MouseSensitivity"));

		Eclipse.client.player.setYaw(Eclipse.client.player.getYaw() + yawOff);
		Eclipse.client.player.setPitch(Eclipse.client.player.getPitch() + pitchOff);

		this.lastMouseX = mouseX;
		this.lastMouseY = mouseY;
	}
}