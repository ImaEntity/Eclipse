package com.entity.eclipse.modules.movement;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.RenderEvent;
import com.entity.eclipse.utils.types.DoubleValue;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;

public class Speed extends Module {
    private double prevSpeed;

    public Speed() {
        super("Speed", "gotta go fast", ModuleType.MOVEMENT);

        this.config.create("Multiplier", new DoubleValue(1.5));
    }

    @Override
    public void tick() {
        if(Eclipse.client.player == null) return;

        EntityAttributeInstance attr = Eclipse.client.player
                .getAttributes()
                .getCustomInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);

        if(attr == null) return;

        attr.setBaseValue(this.prevSpeed * (double) this.config.get("Multiplier"));
    }

    @Override
    public void onEnable() {
        if(Eclipse.client.player == null) return;

        EntityAttributeInstance attr = Eclipse.client.player
                .getAttributes()
                .getCustomInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);

        if(attr == null) return;

        this.prevSpeed = attr.getBaseValue();
    }

    @Override
    public void onDisable() {
        if(Eclipse.client.player == null) return;

        EntityAttributeInstance attr = Eclipse.client.player
                .getAttributes()
                .getCustomInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);

        if(attr == null) return;

        attr.setBaseValue(this.prevSpeed);
    }

    @Override
    public void renderWorld(RenderEvent event) {

    }

    @Override
    public void renderScreen(RenderEvent event) {

    }
}
