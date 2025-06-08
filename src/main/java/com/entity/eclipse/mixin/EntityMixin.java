package com.entity.eclipse.mixin;

import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.player.NoEntityPush;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Entity.class)
public class EntityMixin {
    @ModifyArgs(
            method = "pushAwayFrom(Lnet/minecraft/entity/Entity;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"
            )
    )
    public void preventPush(Args args, Entity entity) {
        Module noEntityPush = ModuleManager.getByClass(NoEntityPush.class);
        if(noEntityPush == null) return;

        if(!noEntityPush.isEnabled())
            return;

        args.set(0, 0.0);
        args.set(2, 0.0);
    }
}