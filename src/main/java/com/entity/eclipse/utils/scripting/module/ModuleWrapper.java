package com.entity.eclipse.utils.scripting.module;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.RenderEvent;
import org.graalvm.polyglot.Value;

public class ModuleWrapper extends Module {
    private final Value module;

    public ModuleWrapper(Value jsObject) {
        this(
                jsObject,
                jsObject.getMember("name").asString(),
                jsObject.getMember("description").asString(),
                jsObject.getMember("type").asHostObject()
        );
    }

    public ModuleWrapper(Value module, String name, String desc, ModuleType type) {
        super(
                name == null ? "{NULL}" : name,
                desc == null ? "{NULL}" : desc,
                type == null ? ModuleType.MISC : type
        );

        this.module = module;
        this.isExternal = true;

        if(!module.canInvokeMember("init")) return;

        try {
            module.invokeMember("init", this);
        } catch(Exception e) {
            e.printStackTrace();

            Eclipse.notifyUser(e.toString());
            ModuleManager.queueDisable(this);
        }
    }

    @Override
    public void tick() {
        if(!module.canInvokeMember("tick")) return;

        try {
            module.invokeMember("tick", this);
        } catch(Exception e) {
            e.printStackTrace();

            Eclipse.notifyUser(e.toString());
            ModuleManager.queueDisable(this);
        }
    }

    @Override
    public void onEnable() {
        if(!module.canInvokeMember("onEnable")) return;

        try {
            module.invokeMember("onEnable", this);
        } catch(Exception e) {
            e.printStackTrace();

            Eclipse.notifyUser(e.toString());
            ModuleManager.queueDisable(this);
        }
    }

    @Override
    public void onDisable() {
        if(!module.canInvokeMember("onDisable")) return;

        try {
            module.invokeMember("onDisable", this);
        } catch(Exception e) {
            e.printStackTrace();

            Eclipse.notifyUser(e.toString());
            ModuleManager.queueDisable(this);
        }
    }

    @Override
    public void renderWorld(RenderEvent event) {
        if(!module.canInvokeMember("renderWorld")) return;

        try {
            module.invokeMember("renderWorld", this, event);
        } catch(Exception e) {
            e.printStackTrace();

            Eclipse.notifyUser(e.toString());
            ModuleManager.queueDisable(this);
        }
    }

    @Override
    public void renderScreen(RenderEvent event) {
        if(!module.canInvokeMember("renderScreen")) return;

        try {
            module.invokeMember("renderScreen", this, event);
        } catch(Exception e) {
            e.printStackTrace();

            Eclipse.notifyUser(e.toString());
            ModuleManager.queueDisable(this);
        }
    }
}