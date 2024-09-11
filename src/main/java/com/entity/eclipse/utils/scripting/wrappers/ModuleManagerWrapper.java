package com.entity.eclipse.utils.scripting.wrappers;

import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.ModuleType;

import java.util.ArrayList;

public class ModuleManagerWrapper {
    public ArrayList<Module> getModules() {
        return ModuleManager.getModules();
    }
    public ArrayList<Module> getActiveModules() {
        return ModuleManager.getActiveModules();
    }

    public Module getByClass(Class<? extends Module> _class) {
        return ModuleManager.getByClass(_class);
    }
    public Module getByName(String name) {
        return ModuleManager.getByName(name);
    }

    public void queueEnable(Module module) {
        ModuleManager.queueEnable(module);
    }
    public void queueDisable(Module module) {
        ModuleManager.queueDisable(module);
    }

    public void enable(Module module) {
        ModuleManager.enable(module);
    }
    public void disable(Module module) {
        ModuleManager.disable(module);
    }
    public void toggle(Module module) {
        ModuleManager.toggle(module);
    }

    public void tempEnable(Module module) {
        ModuleManager.tempEnable(module);
    }
    public void tempDisable(Module module) {
        ModuleManager.tempDisable(module);
    }
    public void revertTemp(Module module) {
        ModuleManager.revertTemp(module);
    }

    public Module create(String name, String description, ModuleType type) {
        return ModuleManager.create(name, description, type);
    }
}
