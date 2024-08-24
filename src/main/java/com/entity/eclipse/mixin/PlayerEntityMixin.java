package com.entity.eclipse.mixin;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.combat.Reach;
import com.entity.eclipse.modules.movement.SafeWalk;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "clipAtLedge", at = @At("HEAD"), cancellable = true)
    protected void shouldSafeWalk(CallbackInfoReturnable<Boolean> info) {
        if(Eclipse.client.player == null) return;

        Module safeWalk = ModuleManager.getByClass(SafeWalk.class);
        if(safeWalk == null) return;

        if(
                !(boolean) safeWalk.config.get("SafeWalkInLiquid") &&
                (Eclipse.client.player.isTouchingWater() || Eclipse.client.player.isInLava())
        ) return;

        if(safeWalk.isEnabled())
            info.setReturnValue(true);
    }

    @Inject(at = @At("HEAD"), method = "getBlockInteractionRange", cancellable = true)
    public void blockReachOverride(CallbackInfoReturnable<Double> info) {
        Module reach = ModuleManager.getByClass(Reach.class);
        if(reach == null) return;

        if(!reach.isEnabled())
            return;

        info.setReturnValue(reach.config.get("Distance"));
    }

    @Inject(at = @At("HEAD"), method = "getEntityInteractionRange", cancellable = true)
    public void entityReachOverride(CallbackInfoReturnable<Double> info) {
        Reach reach = (Reach) ModuleManager.getByClass(Reach.class);
        if(reach == null) return;

        if(!reach.isEnabled())
            return;

        info.setReturnValue(reach.config.get("Distance"));
    }
}