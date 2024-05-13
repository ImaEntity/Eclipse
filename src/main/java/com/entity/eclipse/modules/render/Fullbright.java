package com.entity.eclipse.modules.render;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.RenderEvent;

public class Fullbright extends Module {
    private double originalGamma;

    public Fullbright() {
        super("Fullbright", "let there be light", ModuleType.RENDER);
    }

    @Override
    public void tick() {

    }

    @Override
    public void onEnable() {
        this.originalGamma = Eclipse.client.options.getGamma().getValue();
        Eclipse.client.options.getGamma().setValue(100d);
    }

    @Override
    public void onDisable() {
        Eclipse.client.options.getGamma().setValue(this.originalGamma);
    }

    @Override
    public void renderWorld(RenderEvent event) {

    }

    @Override
    public void renderScreen(RenderEvent event) {

    }
}
