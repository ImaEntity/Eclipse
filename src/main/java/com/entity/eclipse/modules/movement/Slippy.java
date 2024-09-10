package com.entity.eclipse.modules.movement;

import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.RenderEvent;
import com.entity.eclipse.utils.types.BlockValue;
import com.entity.eclipse.utils.types.FloatValue;
import com.entity.eclipse.utils.types.ListValue;

public class Slippy extends Module {
    public Slippy() {
        super("Slippy", "Makes everything slippery.", ModuleType.MOVEMENT);

        this.config.create("Slipperiness", new FloatValue(0.75f));
        this.config.create("BlacklistedBlocks", new ListValue(BlockValue.class));
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
