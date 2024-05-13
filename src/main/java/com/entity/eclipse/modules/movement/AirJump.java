package com.entity.eclipse.modules.movement;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.RenderEvent;

public class AirJump extends Module {
    public AirJump() {
        super("AirJump", "fake flight", ModuleType.MOVEMENT);
    }

    @Override
    public void tick() {
        if(Eclipse.client.player == null) return;
        if(Eclipse.client.getNetworkHandler() == null) return;

        if(Eclipse.client.options.jumpKey.isPressed()) {
            Eclipse.client.player.setOnGround(true);
            Eclipse.client.player.fallDistance = 0f;
        }
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
