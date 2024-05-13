package com.entity.eclipse.commands;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.commands.base.Command;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.utils.Strings;

public class Binds extends Command {
    public Binds() {
        super("Binds", "Shows current module binds.", "binds");
    }

    @Override
    public void onExecute(String[] args) {
        for(Module module : ModuleManager.getModules()) {
            if(module.keybind.isUnbound()) continue;

            String keyName = Strings.format(module.keybind.toString());
            Eclipse.notifyUser(module + " is bound to " + keyName);
        }
    }
}
