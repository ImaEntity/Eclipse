package com.entity.eclipse.modules;

import com.entity.eclipse.utils.Configuration;
import com.entity.eclipse.utils.Keybind;
import com.entity.eclipse.utils.events.render.RenderEvent;

public abstract class Module {
    private final String name;
    private final String description;
    private final ModuleType type;

    protected boolean isExternal = false;
    protected boolean enabled = false;
    protected boolean showToasts = true;

    public Keybind keybind;
    public Configuration config;

    public Module(String name, String description, ModuleType type) {
        this.name = name;
        this.description = description;
        this.type = type;

        this.config = new Configuration();
        this.keybind = Keybind.unbound();
    }

    public String getName() {
        if(this.isExternal())
            return 'λ' + this.name;

        return this.name;
    }

    public String getDescription() {
        return this.description;
    }
    public ModuleType getType() {
        return this.type;
    }

    public boolean isExternal() {
        return this.isExternal;
    }
    public boolean isEnabled() {
        return this.enabled;
    }
    public boolean shouldShowToasts() {
        return this.showToasts;
    }
    public void shouldShowToasts(boolean showToasts) {
        this.showToasts = showToasts;
    }

    public abstract void tick();
    public abstract void onEnable();
    public abstract void onDisable();
    public abstract void renderWorld(RenderEvent event);
    public abstract void renderScreen(RenderEvent event);

    @Override
    public String toString() {
        return "§6" + this.getName() + "§r";
    }

}