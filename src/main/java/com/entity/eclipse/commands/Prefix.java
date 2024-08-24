package com.entity.eclipse.commands;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.commands.base.Command;

public class Prefix extends Command {
    public Prefix() {
        super("Prefix", "Changes the clients chat prefix.", "prefix");
    }

    @Override
    public void onExecute(String[] args) {
        if(args.length == 0) {
            Eclipse.notifyUser("Syntax: prefix <prefix>");
            return;
        }

        Eclipse.config.set("chatPrefix", args[0]);
        Eclipse.notifyUser("Set new prefix to '" + args[0] + "'");
    }
}
