package com.entity.eclipse.modules.misc;

import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.Render2DEvent;
import com.entity.eclipse.utils.events.render.Render3DEvent;
import com.entity.eclipse.utils.types.StringValue;

public class BrandSpoof extends Module {
    public BrandSpoof() {
        super("BrandSpoof", "\"Are you hacking?\" Nuh uh", ModuleType.MISC);

        this.config.create("NewBrand", new StringValue("vanilla"));
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
    public void renderWorld(Render3DEvent event) {

    }

    @Override
    public void renderScreen(Render2DEvent event) {

    }
}
