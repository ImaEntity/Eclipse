package com.entity.eclipse.commands;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.commands.base.Command;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.utils.scripting.ScriptingManager;

public class Reload extends Command {
    public Reload() {
        super("Reload", "Reloads all scripts.", "reload");
    }

    @Override
    public void onExecute(String[] args) {
        ModuleManager.removeAllExternals();
        ScriptingManager.loadScripts(Eclipse.jsEngine, Eclipse.engineScope);
    }
}
