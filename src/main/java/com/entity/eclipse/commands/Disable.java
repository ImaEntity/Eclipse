package com.entity.eclipse.commands;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.commands.base.Command;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.utils.Keybind;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

public class Disable extends Command {
    public Disable() {
        super("Disable", "turn off hax", "disable");
    }

    @Override
    public void onExecute(String[] args) {
        if(args.length == 0) {
            Eclipse.notifyUser("Syntax: bind <module>");
            return;
        }

        Module target = ModuleManager.getByName(args[0].toLowerCase());
        if(target == null) {
            Eclipse.notifyUser("Invalid module!");
            return;
        }

        ModuleManager.disable(target);
    }
}
