package com.entity.eclipse.modules;

public enum ModuleType {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    PLAYER("Player"),
    RENDER("Render"),
    WORLD("World"),
    NETWORK("Network"),
    MISC("Miscellaneous");

    private final String name;
    ModuleType(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
}
