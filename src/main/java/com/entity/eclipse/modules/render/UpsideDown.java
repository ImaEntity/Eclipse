package com.entity.eclipse.modules.render;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.Render2DEvent;
import com.entity.eclipse.utils.events.render.Render3DEvent;

public class UpsideDown extends Module {
    private int originalFOV;

    public UpsideDown() {
        super("UpsideDown", "upside down camera", ModuleType.RENDER);
    }

    @Override
    public void tick() {

    }

    @Override
    public void onEnable() {
        this.originalFOV = Eclipse.client.options.getFov().getValue();
        Eclipse.client.options.getFov().setValue(360 - this.originalFOV);
    }

    @Override
    public void onDisable() {
        Eclipse.client.options.getFov().setValue(this.originalFOV);
    }

    @Override
    public void renderWorld(Render3DEvent event) {

    }

    @Override
    public void renderScreen(Render2DEvent event) {

    }
}
