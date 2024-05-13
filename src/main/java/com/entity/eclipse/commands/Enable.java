package com.entity.eclipse.commands;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.commands.base.Command;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;

public class Enable extends Command {
    public Enable() {
        super("Enable", "ur haxor", "enable");
    }

    @Override
    public void onExecute(String[] args) {
        if(args.length == 0) {
            Eclipse.notifyUser("Syntax: enable <module>");
            return;
        }

        Module target = ModuleManager.getByName(args[0].toLowerCase());
        if(target == null) {
            Eclipse.notifyUser("Invalid module!");
            return;
        }

        ModuleManager.enable(target);
    }
}
