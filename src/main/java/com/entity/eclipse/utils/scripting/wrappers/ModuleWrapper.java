package com.entity.eclipse.utils.scripting.wrappers;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.RenderEvent;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;

import java.util.HashMap;

public class ModuleWrapper extends Module {
    private final HashMap<String, Function> events;

    public ModuleWrapper(String name, String description, ModuleType type) {
        super(name, description, type);

        this.events = new HashMap<>();
        this.isExternal = true;
    }

    public void on(Object jsEvtName, Object jsCallback) {
        String eventName = (String) Context.jsToJava(jsEvtName, String.class);
        Function callback = (Function) Context.jsToJava(jsCallback, Function.class);

        this.events.put(eventName, callback);
    }

    @Override
    public void tick() {
        if(!this.events.containsKey("tick"))
            return;

        this.events.get("tick").call(
                Eclipse.jsEngine,
                Eclipse.engineScope,
                null,
                new Object[] {}
        );
    }

    @Override
    public void onEnable() {
        if(!this.events.containsKey("enable"))
            return;

        this.events.get("enable").call(
                Eclipse.jsEngine,
                Eclipse.engineScope,
                null,
                new Object[] {}
        );
    }

    @Override
    public void onDisable() {
        if(!this.events.containsKey("disable"))
            return;

        this.events.get("disable").call(
                Eclipse.jsEngine,
                Eclipse.engineScope,
                null,
                new Object[] {}
        );
    }

    @Override
    public void renderWorld(RenderEvent event) {
        if(!this.events.containsKey("render3D"))
            return;

        this.events.get("render3D").call(
                Eclipse.jsEngine,
                Eclipse.engineScope,
                null,
                new Object[] {event}
        );
    }

    @Override
    public void renderScreen(RenderEvent event) {
        if(!this.events.containsKey("render2D"))
            return;

        this.events.get("render2D").call(
                Eclipse.jsEngine,
                Eclipse.engineScope,
                null,
                new Object[] {event}
        );
    }
}
