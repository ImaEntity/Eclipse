package com.entity.eclipse.mixin;

import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.misc.ResourceSpoof;
import net.minecraft.client.resource.server.ServerResourcePackLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerResourcePackLoader.class)
public class ServerResourcePackLoaderMixin {
	@Inject(method = "onReloadSuccess", at = @At("TAIL"))
	private void resetPacketCancelling(CallbackInfo info) {
		ResourceSpoof resourceSpoof = (ResourceSpoof) ModuleManager.getByClass(ResourceSpoof.class);
		if(resourceSpoof == null) return;

		resourceSpoof.cancelStatusPackets = false;
	}
}