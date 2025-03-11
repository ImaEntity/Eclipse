package com.entity.eclipse.modules.render;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.Render2DEvent;
import com.entity.eclipse.utils.events.render.Render3DEvent;
import net.minecraft.entity.effect.StatusEffects;

public class AntiBlind extends Module {
    public AntiBlind() {
        super("AntiBlind", "tells the darkness and blindness effects to piss off", ModuleType.RENDER);
    }

    @Override
    public void tick() {
        if(Eclipse.client.player == null) return;

        Eclipse.client.player.removeStatusEffect(StatusEffects.BLINDNESS);
        Eclipse.client.player.removeStatusEffect(StatusEffects.DARKNESS);
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
