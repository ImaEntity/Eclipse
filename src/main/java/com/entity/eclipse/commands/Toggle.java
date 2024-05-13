package com.entity.eclipse.commands;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.commands.base.Command;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;

public class Toggle extends Command {
    public Toggle() {
        super("Toggle", "flip-flop", "toggle");
    }

    @Override
    public void onExecute(String[] args) {
        if(args.length == 0) {
            Eclipse.notifyUser("Syntax: toggle <module>");
            return;
        }

        Module target = ModuleManager.getByName(args[0].toLowerCase());
        if(target == null) {
            Eclipse.notifyUser("Invalid module!");
            return;
        }

        ModuleManager.toggle(target);
    }
}
