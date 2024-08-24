package com.entity.eclipse.mixin;

import com.entity.eclipse.commands.Panic;
import com.entity.eclipse.commands.base.CommandManager;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.misc.BrandSpoof;
import net.minecraft.client.ClientBrandRetriever;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientBrandRetriever.class)
public class ClientBrandRetrieverMixin {
    @Inject(method = "getClientModName", at = @At("HEAD"), cancellable = true, remap = false)
    private static void replaceModName(CallbackInfoReturnable<String> info) {
        Module brandSpoof = ModuleManager.getByClass(BrandSpoof.class);
        Panic panic = (Panic) CommandManager.getByClass(Panic.class);

        if(brandSpoof != null && brandSpoof.isEnabled())
            info.setReturnValue(brandSpoof.config.get("NewBrand"));

        if(panic != null && panic.isHardPanicked())
            info.setReturnValue("vanilla");
    }
}
