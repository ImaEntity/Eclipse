package com.entity.eclipse.mixin;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.movement.Jesus;
import com.entity.eclipse.utils.PlayerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "onAttacking", at = @At("HEAD"))
    public void setLastAttacking(Entity entity, CallbackInfo info) {
        if(!this.equals(Eclipse.client.player)) return;

        Eclipse.notifyUser("Acking");
        PlayerUtils.setLastAttacked(entity);
    }

    @Inject(method = "canWalkOnFluid", at = @At("HEAD"), cancellable = true)
    public void canWalkOnFluid(FluidState fluid, CallbackInfoReturnable<Boolean> info) {
        if(Eclipse.client.player == null) return;

        Jesus jesus = (Jesus) ModuleManager.getByClass(Jesus.class);

        if(!jesus.isEnabled()) return;
        if(Eclipse.client.player.isInSwimmingPose()) return;

        if(
                (fluid.getFluid() == Fluids.WATER || fluid.getFluid() == Fluids.FLOWING_WATER) &&
                jesus.waterIsSolid()
        ) info.setReturnValue(true);

        if(
                (fluid.getFluid() == Fluids.LAVA || fluid.getFluid() == Fluids.FLOWING_LAVA) &&
                jesus.lavaIsSolid()
        ) info.setReturnValue(true);
    }
}