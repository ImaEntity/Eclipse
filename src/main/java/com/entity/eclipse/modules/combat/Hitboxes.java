package com.entity.eclipse.modules.combat;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.RenderEvent;
import com.entity.eclipse.utils.types.DoubleValue;
import com.entity.eclipse.utils.types.EntityTypeValue;
import com.entity.eclipse.utils.types.ListValue;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;

public class Hitboxes extends Module {
    public Hitboxes() {
        super("Hitboxes", "Bigger hitboxes.", ModuleType.COMBAT);

        this.config.create("Width", new DoubleValue(0.1));
        this.config.create("Height", new DoubleValue(0.1));
        this.config.create("Entities", new ListValue(
                EntityTypeValue.class,
                "player",
                "blaze",
                "cave_spider",
                "creeper",
                "drowned",
                "elder_guardian",
                "enderman",
                "endermite",
                "ender_dragon",
                "evoker",
                "ghast",
                "guardian",
                "hoglin",
                "husk",
                "magma_cube",
                "phantom",
                "piglin",
                "piglin_brute",
                "pillager",
                "ravager",
                "shulker",
                "silverfish",
                "skeleton",
                "slime",
                "spider",
                "stray",
                "vex",
                "vindicator",
                "warden",
                "witch",
                "wither",
                "wither_skeleton",
                "zoglin",
                "zombie",
                "zombie_villager"
        ));
    }

    @Override
    public void tick() {
        if(Eclipse.client.player == null) return;
        if(Eclipse.client.world == null) return;
        if(Eclipse.client.interactionManager == null) return;

        for(Entity entity : Eclipse.client.world.getEntities()) {
            if(entity == Eclipse.client.player) continue;
            if(!((ListValue) this.config.getRaw("Entities")).contains(entity.getType())) continue;

            double width = entity.getType().getWidth();
            double height = entity.getType().getHeight();
            double depth = entity.getType().getWidth();

            entity.setBoundingBox(new Box(
                    entity.getPos().getX() - (double) this.config.get("Width") / 2 - width / 2,
                    entity.getPos().getY() - (double) this.config.get("Height") / 2,
                    entity.getPos().getZ() - (double) this.config.get("Width") / 2 - depth / 2,
                    entity.getPos().getX() + (double) this.config.get("Width") / 2 + width / 2,
                    entity.getPos().getY() + (double) this.config.get("Height") / 2 + height,
                    entity.getPos().getZ() + (double) this.config.get("Width") / 2 + depth / 2
            ));
        }
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
