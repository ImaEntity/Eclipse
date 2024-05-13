package com.entity.eclipse.modules;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.combat.AutoLog;
import com.entity.eclipse.modules.combat.AutoTotem;
import com.entity.eclipse.modules.combat.Criticals;
import com.entity.eclipse.modules.combat.Killaura;
import com.entity.eclipse.modules.misc.Test;
import com.entity.eclipse.modules.movement.*;
import com.entity.eclipse.modules.network.AntiPacketKick;
import com.entity.eclipse.modules.player.AntiHunger;
import com.entity.eclipse.modules.player.AutoEat;
import com.entity.eclipse.modules.render.DamagePerSecond;
import com.entity.eclipse.modules.render.Freecam;
import com.entity.eclipse.modules.render.Fullbright;
import com.entity.eclipse.modules.render.Xray;
import com.entity.eclipse.modules.world.AirPlace;
import com.entity.eclipse.modules.world.FastUse;
import com.entity.eclipse.modules.world.LawnMower;
import com.entity.eclipse.utils.events.*;
import com.entity.eclipse.utils.events.render.RenderEvent;
import com.entity.eclipse.utils.events.render.RenderEvents;
import com.entity.eclipse.utils.events.tick.TickEvent;
import com.entity.eclipse.utils.events.tick.TickEvents;

import java.util.ArrayList;

public class ModuleManager {
    private static final ArrayList<Module> activeModules = new ArrayList<>();
    private static final ArrayList<Module> modules = new ArrayList<>();
    private static final ArrayList<Module> toBeEnabled = new ArrayList<>();
    private static final ArrayList<Module> toBeDisabled = new ArrayList<>();

    static {
        Events.Tick.register(TickEvents.START, ModuleManager::tick);
        Events.Render.register(RenderEvents.D2, ModuleManager::renderScreen);
        Events.Render.register(RenderEvents.D3, ModuleManager::renderWorld);

        // Combat

        modules.add(new AutoTotem());
        modules.add(new AutoLog());
        modules.add(new Criticals());
        modules.add(new Killaura());

        // Movement

        modules.add(new AirJump());
        modules.add(new Blink());
        modules.add(new Flight());
        modules.add(new Jesus());
        modules.add(new NoFall());
        modules.add(new NoJumpCooldown());
        modules.add(new Speed());
        modules.add(new Velocity());

        // Player

        modules.add(new AntiHunger());
        modules.add(new AutoEat());

        // Render

        modules.add(new DamagePerSecond());
        modules.add(new Freecam());
        modules.add(new Fullbright());
        modules.add(new Xray());

        // World

        modules.add(new AirPlace());
        modules.add(new FastUse());
        modules.add(new LawnMower());

        // Network

        modules.add(new AntiPacketKick());

        // Misc

        modules.add(new Test());
    }

    public static void tick(TickEvent event) {
        for(int i = toBeEnabled.size() - 1; i >= 0; i--)
            enable(toBeEnabled.remove(i));

        for(int i = toBeDisabled.size() - 1; i >= 0; i--)
            disable(toBeDisabled.remove(i));

        for(Module module : modules) {
            if(module.keybind.isUnbound()) continue;

            if(module.keybind.isPressed())
                toggle(module);
        }

        for(Module module : activeModules) {
            try {
                module.tick();
            } catch(Exception e) {
                e.printStackTrace();

                Eclipse.notifyUser(e.toString());
                queueDisable(module);
            }
        }
    }

    public static void renderWorld(RenderEvent event) {
        for(Module module : activeModules)
            module.renderWorld(event);
    }

    public static void renderScreen(RenderEvent event) {
        for(Module module : activeModules)
            module.renderScreen(event);
    }

    public static ArrayList<Module> getModules() {
        return modules;
    }
    public static ArrayList<Module> getActiveModules() {
        return activeModules;
    }

    public static Module getByClass(Class<? extends Module> _class) {
        for(Module module : modules)
            if(module.getClass() == _class) return module;

        return null;
    }

    public static Module getByName(String name) {
        for(Module module : modules)
            if(module.getName().equalsIgnoreCase(name)) return module;

        return null;
    }

    public static void queueEnable(Module module) {
        toBeEnabled.add(module);
    }
    public static void queueDisable(Module module) {
        toBeDisabled.add(module);
    }

    public static void enable(Module module) {
        if(module.enabled) return;

        activeModules.add(module);
        Eclipse.notifyUser("Enabled " + module);

        module.enabled = true;
        module.onEnable();
    }

    public static void disable(Module module) {
        if(!module.enabled) return;

        activeModules.remove(module);
        Eclipse.notifyUser("Disabled " + module);

        module.enabled = false;
        module.onDisable();
    }

    public static void toggle(Module module) {
        if(!module.isEnabled()) enable(module);
        else disable(module);
    }
}