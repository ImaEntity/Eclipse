package com.entity.eclipse.mixin;

import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.render.NegativeGraphics;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.render.model.BakedQuad;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BakedQuad.class)
public class BakedQuadMixin {
    @ModifyReturnValue(method = "hasShade", at = @At("RETURN"))
    private boolean noShading(boolean original) {
        Module ng = ModuleManager.getByClass(NegativeGraphics.class);
        if(ng == null) return original;

        if(!ng.isEnabled())
            return original;

        return ((NegativeGraphics) ng).hasShading(original);
    }
}
