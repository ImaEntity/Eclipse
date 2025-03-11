package com.entity.eclipse.modules.movement;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.Render2DEvent;
import com.entity.eclipse.utils.events.render.Render3DEvent;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", "Forces the sprinting state.", ModuleType.MOVEMENT);
    }

    @Override
    public void tick() {
        if(Eclipse.client.player == null) return;

        Eclipse.client.player.setSprinting(
                Eclipse.client.options.sprintKey.isPressed()
        );

        if(!Eclipse.client.options.forwardKey.isPressed()) return;
        if(Eclipse.client.options.backKey.isPressed()) return;
        if(Eclipse.client.player.horizontalCollision) return;

        Eclipse.client.player.setSprinting(true);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void renderWorld(Render3DEvent event) {

    }

    @Override
    public void renderScreen(Render2DEvent event) {

    }
}
