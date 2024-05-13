package com.entity.eclipse.modules.render;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.RenderEvent;

public class Freecam extends Module {
    public Freecam() {
        super("Freecam", "ascend your soul", ModuleType.RENDER);
    }

    @Override
    public void tick() {

    }

    @Override
    public void onEnable() {
        Eclipse.notifyUser("idfk it broke one day, and never worked since");
        Eclipse.notifyUser("ill fix it eventually");

        ModuleManager.disable(this);
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
