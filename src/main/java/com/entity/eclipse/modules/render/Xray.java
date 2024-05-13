package com.entity.eclipse.modules.render;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.RenderEvent;
import com.entity.eclipse.utils.types.BlockValue;
import com.entity.eclipse.utils.types.ListValue;

public class Xray extends Module {
    public Xray() {
        super("Xray", "makes you better at mining", ModuleType.RENDER);

        this.config.create("BlockIds", new ListValue(
                BlockValue.class,
                "diamond_ore",
                "deepslate_diamond_ore",
                "ancient_debris"
        ));
    }

    @Override
    public void tick() {

    }

    @Override
    public void onEnable() {
        if(Eclipse.client.worldRenderer == null) return;
        Eclipse.client.worldRenderer.reload();
    }

    @Override
    public void onDisable() {
        if(Eclipse.client.worldRenderer == null) return;
        Eclipse.client.worldRenderer.reload();
    }

    @Override
    public void renderWorld(RenderEvent event) {

    }

    @Override
    public void renderScreen(RenderEvent event) {

    }
}
