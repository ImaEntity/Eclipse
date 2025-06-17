package com.entity.eclipse.mixin;

import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.world.FastUse;
import com.entity.eclipse.utils.Ticker;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.tick.TickEvent;
import com.entity.eclipse.utils.events.tick.TickEvents;
import net.minecraft.client.MinecraftClient;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Shadow
	private int itemUseCooldown;

	@Redirect(
			method = "handleInputEvents",
			at = @At(
					value = "FIELD",
					opcode = Opcodes.GETFIELD,
					target = "Lnet/minecraft/client/MinecraftClient;itemUseCooldown:I"
			)
	)
	public int resetItemUseCooldown(MinecraftClient client) {
		Module fastUse =  ModuleManager.getByClass(FastUse.class);
		if(fastUse == null) return this.itemUseCooldown;

		return fastUse.isEnabled() ? 0 : this.itemUseCooldown;
	}

	@Inject(at = @At("HEAD"), method = "tick", cancellable = true)
	private void onTickStart(CallbackInfo info) {
		Ticker.tick();

		if(Events.Tick.fireEvent(TickEvents.START, new TickEvent()))
			info.cancel();
	}

	@Inject(at = @At("TAIL"), method = "tick", cancellable = true)
	private void onTickEnd(CallbackInfo info) {
		if(Events.Tick.fireEvent(TickEvents.END, new TickEvent()))
			info.cancel();
	}
}