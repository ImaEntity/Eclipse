package com.entity.eclipse.mixin.world;

import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.world.FastUse;
import net.minecraft.client.MinecraftClient;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    private int itemUseCooldown;

    @Redirect(method = "handleInputEvents", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/MinecraftClient;itemUseCooldown:I"))
    public int resetItemUseCooldown(MinecraftClient client) {
        return ModuleManager.getByClass(FastUse.class).isEnabled() ? 0 : this.itemUseCooldown;
    }
}
