package com.entity.eclipse.modules.combat;

import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.RenderEvent;
import com.entity.eclipse.utils.types.DoubleValue;

public class Reach extends Module {

    public Reach() {
        super("Reach", "gives you really long arms", ModuleType.COMBAT);

        this.config.create("Distance", new DoubleValue(4.0));
    }

    @Override
    public void tick() {

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
