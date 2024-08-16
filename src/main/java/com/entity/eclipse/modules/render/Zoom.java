package com.entity.eclipse.modules.render;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.RenderEvent;
import com.entity.eclipse.utils.types.IntegerValue;

public class Zoom extends Module {
    private int originalFOV;
    private double originalSens;

    public Zoom() {
        super("Zoom", "Zooms in your camera.", ModuleType.RENDER);

        this.config.create("ZoomedFov", new IntegerValue(25));
    }

    @Override
    public void tick() {

    }

    @Override
    public void onEnable() {
        this.originalFOV = Eclipse.client.options.getFov().getValue();
        this.originalSens = Eclipse.client.options.getMouseSensitivity().getValue();

        Eclipse.client.options.getFov().setValue(this.config.get("ZoomedFov"));
        Eclipse.client.options.getMouseSensitivity().setValue(
                this.originalSens * ((double) (int) this.config.get("ZoomedFov") / this.originalFOV)
        );
    }

    @Override
    public void onDisable() {
        Eclipse.client.options.getFov().setValue(this.originalFOV);
        Eclipse.client.options.getMouseSensitivity().setValue(this.originalSens);
    }

    @Override
    public void renderWorld(RenderEvent event) {

    }

    @Override
    public void renderScreen(RenderEvent event) {

    }
}
