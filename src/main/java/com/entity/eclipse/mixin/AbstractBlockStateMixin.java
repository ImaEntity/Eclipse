package com.entity.eclipse.mixin;

import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.render.Xray;
import net.minecraft.block.AbstractBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class AbstractBlockStateMixin {
    @Inject(method = "getLuminance", at = @At("HEAD"), cancellable = true)
    public void forceLuminance(CallbackInfoReturnable<Integer> info) {
        if(ModuleManager.getByClass(Xray.class).isEnabled())
            info.setReturnValue(15);
    }
}
