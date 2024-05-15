package com.entity.eclipse.commands.base;

public abstract class Command {
    private final String name;
    private final String description;
    private final String[] aliases;

    public Command(String name, String description, String... aliases) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
    }

    public String getName() {
        return this.name;
    }
    public String getDescription() {
        return this.description;
    }
    public String[] getAliases() {
        return this.aliases;
    }

    public abstract void onExecute(String[] args);

    @Override
    public String toString() {
        return "§6" + this.name + "§r";
    }
}
