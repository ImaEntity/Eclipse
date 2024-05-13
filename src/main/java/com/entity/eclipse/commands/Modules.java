package com.entity.eclipse.commands;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.commands.base.Command;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.utils.Strings;

public class Modules extends Command {

    public Modules() {
        super("Modules", "Lists modules.", "modules");
    }

    @Override
    public void onExecute(String[] args) {
        for(Module module : ModuleManager.getModules())
            Eclipse.notifyUser(module + " - " + Strings.format(module.isEnabled(), "on", "off"));
    }
}