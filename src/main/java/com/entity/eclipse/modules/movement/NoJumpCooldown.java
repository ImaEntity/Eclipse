package com.entity.eclipse.modules.movement;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.mixin.ILivingEntityMixin;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.RenderEvent;

public class NoJumpCooldown extends Module {
    public NoJumpCooldown() {
        super("NoJumpCooldown", "infinite jump", ModuleType.MOVEMENT);
    }

    @Override
    public void tick() {
        if(Eclipse.client.player == null) return;
        if(Eclipse.client.getNetworkHandler() == null) return;

        ((ILivingEntityMixin) Eclipse.client.player).setJumpingCooldown(0);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void renderWorld(RenderEvent event) {

    }

    @Override
    public void renderScreen(RenderEvent event) {

    }
}
