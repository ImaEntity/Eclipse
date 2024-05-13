package com.entity.eclipse.commands;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.commands.base.Command;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.utils.Keybind;

public class Unbind extends Command {
    public Unbind() {
        super("Unbind", "no more hacker", "unbind");
    }

    @Override
    public void onExecute(String[] args) {
        if(args.length == 0) {
            Eclipse.notifyUser("Syntax: unbind <module>");
            return;
        }

        Module module = ModuleManager.getByName(args[0].toLowerCase());
        if(module == null) {
            Eclipse.notifyUser("Invalid module!");
            return;
        }

        module.keybind = Keybind.unbound();
        Eclipse.notifyUser("Unbound " + module);
    }
}
