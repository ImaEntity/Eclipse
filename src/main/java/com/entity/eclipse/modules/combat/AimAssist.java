package com.entity.eclipse.modules.combat;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.PlayerUtils;
import com.entity.eclipse.utils.events.render.RenderEvent;
import com.entity.eclipse.utils.types.DoubleValue;
import net.minecraft.entity.Entity;

public class AimAssist extends Module {
    public AimAssist() {
        super("AimAssist", "Helps you aim at entities around you.", ModuleType.COMBAT);

        this.config.create("Laziness", new DoubleValue(1.0));
        this.config.create("TargetReleaseTimeout", new DoubleValue(3.0));
    }

    @Override
    public void tick() {
        if(Eclipse.client.player == null) return;

        Entity lastAttacked = PlayerUtils.getLastAttacked((long) ((double) this.config.get("TargetReleaseTimeout") * 1000));
        if(lastAttacked == null) return;

        PlayerUtils.tickSmoothLook(
                lastAttacked
                        .getPos()
                        .add(0, lastAttacked.getHeight() / 2, 0),
                this.config.get("Laziness")
        );
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
