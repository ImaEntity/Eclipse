package com.entity.eclipse.modules;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.combat.*;
import com.entity.eclipse.modules.misc.BrandSpoof;
import com.entity.eclipse.modules.misc.Gambling;
import com.entity.eclipse.modules.misc.Test;
import com.entity.eclipse.modules.movement.*;
import com.entity.eclipse.modules.network.AntiPacketKick;
import com.entity.eclipse.modules.network.Blink;
import com.entity.eclipse.modules.network.PacketLoss;
import com.entity.eclipse.modules.player.AntiHunger;
import com.entity.eclipse.modules.player.AutoEat;
import com.entity.eclipse.modules.player.AutoTool;
import com.entity.eclipse.modules.render.*;
import com.entity.eclipse.modules.world.*;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.render.RenderEvent;
import com.entity.eclipse.utils.events.render.RenderEvents;
import com.entity.eclipse.utils.events.tick.TickEvent;
import com.entity.eclipse.utils.events.tick.TickEvents;
import com.entity.eclipse.utils.scripting.wrappers.ModuleWrapper;

import java.util.ArrayList;

public class ModuleManager {
    private static final ArrayList<Module> activeModules = new ArrayList<>();
    private static final ArrayList<Module> modules = new ArrayList<>();
    private static final ArrayList<Module> externalModules = new ArrayList<>();
    private static final ArrayList<Module> toBeEnabled = new ArrayList<>();
    private static final ArrayList<Module> toBeDisabled = new ArrayList<>();

    static {
        Events.Tick.register(TickEvents.START, ModuleManager::tick);
        Events.Render.register(RenderEvents.D2, ModuleManager::renderScreen);
        Events.Render.register(RenderEvents.D3, ModuleManager::renderWorld);

        // Combat

        modules.add(new AimAssist());
        modules.add(new AutoTotem());
        modules.add(new AutoLog());
        modules.add(new Criticals());
//        modules.add(new CrystalAura());
        modules.add(new Hitboxes());
        modules.add(new Killaura());
        modules.add(new Knockback());
        modules.add(new MacePlus());
        modules.add(new Reach());

        // Movement

        modules.add(new AirJump());
        modules.add(new AutoSneak());
        modules.add(new Flight());
        modules.add(new Jesus());
        modules.add(new LongJump());
        modules.add(new NoFall());
        modules.add(new NoJumpCooldown());
        modules.add(new SafeWalk());
        modules.add(new Slippy());
        modules.add(new Speed());
        modules.add(new Sprint());
        modules.add(new Velocity());

        // Player

        modules.add(new AntiHunger());
        modules.add(new AutoEat());
        modules.add(new AutoTool());

        // Render

        modules.add(new AntiBlind());
        modules.add(new DamagePerSecond());
//        modules.add(new Freecam());
        modules.add(new Fullbright());
        modules.add(new ItemInfo());
        modules.add(new ModuleList());
        modules.add(new NegativeGraphics());
        modules.add(new UpsideDown());
        modules.add(new Xray());
        modules.add(new Zoom());

        // World

        modules.add(new AirPlace());
        modules.add(new AutoFarm());
        modules.add(new AutoFish());
        modules.add(new BridgeBuilder());
        modules.add(new Extinguisher());
        modules.add(new FastUse());
        modules.add(new LawnMower());
        modules.add(new Scaffold());

        // Network

        modules.add(new AntiPacketKick());
        modules.add(new Blink());
        modules.add(new PacketLoss());

        // Misc

        modules.add(new BrandSpoof());
        modules.add(new Gambling());
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

            if(module.keybind.togglesOnRelease() && module.keybind.wasPressed())
                toggle(module);
        }

        for(Module module : activeModules) {
            if(!module.isEnabled()) continue;

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
        for(Module module : activeModules) {
            if(!module.isEnabled()) continue;
            module.renderWorld(event);
        }
    }

    public static void renderScreen(RenderEvent event) {
        for(Module module : activeModules) {
            if(!module.isEnabled()) continue;
            module.renderScreen(event);
        }
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
        if(module == null) return;
        toBeEnabled.add(module);
    }
    public static void queueDisable(Module module) {
        if(module == null) return;
        toBeDisabled.add(module);
    }

    public static void enable(Module module) {
        if(module == null) return;
        if(module.isEnabled()) return;

        activeModules.add(module);

        if(module.shouldShowToasts())
            Eclipse.notifyUser("Enabled " + module);

        module.enabled = true;
        module.onEnable();
    }

    public static void disable(Module module) {
        if(module == null) return;
        if(!module.isEnabled()) return;

        activeModules.remove(module);

        if(module.shouldShowToasts())
            Eclipse.notifyUser("Disabled " + module);

        module.enabled = false;
        module.onDisable();
    }

    public static void toggle(Module module) {
        if(module == null) return;
        if(!module.isEnabled()) enable(module);
        else disable(module);
    }

    public static void tempEnable(Module module) {
        if(module == null) return;
        module.enabled = true;
    }
    public static void tempDisable(Module module) {
        if(module == null) return;
        module.enabled = false;
    }
    public static void revertTemp(Module module) {
        if(module == null) return;
        module.enabled = activeModules.contains(module);
    }

    public static void appendExternal(Module module) {
        Module old = getByName(module.getName());
        if(old != null)
            removeExternal(old);

        externalModules.add(module);
        modules.add(module);
    }

    public static boolean removeExternal(Module module) {
        return removeExternal(module, true);
    }
    private static boolean removeExternal(Module module, boolean removeFromExternals) {
        disable(module);

        if(removeFromExternals)
            externalModules.remove(module);

        return modules.remove(module);
    }

    public static void removeAllExternals() {
        for(Module module : externalModules)
            removeExternal(module, false);

        externalModules.clear();
    }

    public static Module create(String name, String description, ModuleType type) {
        Module module =  new ModuleWrapper(name, description, type);
        appendExternal(module);

        return module;
    }
}
