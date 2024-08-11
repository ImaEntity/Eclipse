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
        if(Eclipse.client.cameraEntity == null) return;
        if(Eclipse.client.player == null) return;

        Eclipse.client.cameraEntity.noClip = true;
        Eclipse.client.player.noClip = true;
    }

    @Override
    public void onDisable() {
        if(Eclipse.client.cameraEntity == null) return;
        if(Eclipse.client.player == null) return;

        Eclipse.client.cameraEntity.noClip = false;
        Eclipse.client.player.noClip = false;
    }

    @Override
    public void renderWorld(RenderEvent event) {

    }

    @Override
    public void renderScreen(RenderEvent event) {

    }
}
