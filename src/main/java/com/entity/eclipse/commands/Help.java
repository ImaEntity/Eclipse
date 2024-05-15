package com.entity.eclipse.commands;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.commands.base.Command;
import com.entity.eclipse.commands.base.CommandManager;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.utils.Strings;

public class Help extends Command {

    public Help() {
        super("Help", "Lists commands.", "help", "commands");
    }

    @Override
    public void onExecute(String[] args) {
        for(Command command : CommandManager.getCommands())
            Eclipse.notifyUser(command + " - " + command.getDescription());
    }
}