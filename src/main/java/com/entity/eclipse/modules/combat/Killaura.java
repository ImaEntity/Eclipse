package com.entity.eclipse.modules.combat;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.RenderEvent;
import com.entity.eclipse.utils.types.*;
import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;

public class Killaura extends Module {
    private final HashMap<Entity, Integer> delays = new HashMap<>();

    public Killaura() {
        super("Killaura", "Attacks everything around you.", ModuleType.COMBAT);

        this.config.create("Range", new FloatValue(4f));
        this.config.create("HitDelay", new IntegerValue(0));
        this.config.create("AutoDelay", new BooleanValue(true));
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

        this.delays.replaceAll((k, v) -> v + 1);

        ArrayList<Entity> withinRange = new ArrayList<>();

        for(Entity entity : Eclipse.client.world.getEntities()) {
            if(entity == Eclipse.client.player) continue;
            if(!entity.isAlive()) continue;
            if(!entity.isAttackable()) continue;

            // Fuck
            if(!((ListValue) this.config.getRaw("Entities")).contains(entity.getType())) continue;

            if(entity.distanceTo(Eclipse.client.player) <= (float) this.config.get("Range"))
                withinRange.add(entity);
        }

        if(withinRange.isEmpty()) return;

        Entity closestEntity = null;
        float closestDistance = Float.POSITIVE_INFINITY;

        for(Entity entity : withinRange) {
            float distance = entity.distanceTo(Eclipse.client.player);

            if(distance < closestDistance) {
                closestDistance = distance;
                closestEntity = entity;
            }
        }

        int ticksPerAttack = (int) (Eclipse.client.player.getAttackCooldownProgressPerTick());
        int defaultDelay = (boolean) this.config.get("AutoDelay") ?
                ticksPerAttack :
                this.config.get("HitDelay");

        int delay = this.delays.getOrDefault(closestEntity, defaultDelay);

        if(delay < defaultDelay) return;

        // TODO: Look at the entity before attacking

        Eclipse.client.interactionManager.attackEntity(Eclipse.client.player, closestEntity);
        this.delays.put(closestEntity, 0);
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
