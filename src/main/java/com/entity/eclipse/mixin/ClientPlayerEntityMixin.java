package com.entity.eclipse.mixin;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.combat.Reach;
import com.entity.eclipse.utils.Ticker;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.tick.TickEvent;
import com.entity.eclipse.utils.events.tick.TickEvents;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
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