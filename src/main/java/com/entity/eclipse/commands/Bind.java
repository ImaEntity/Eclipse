package com.entity.eclipse.commands;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.commands.base.Command;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.utils.Keybind;
import com.entity.eclipse.utils.Strings;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

public class Bind extends Command {
    public Bind() {
        super("Bind", "press button -> hacker", "bind");
    }

    @Override
    public void onExecute(String[] args) {
        if(args.length == 0) {
            Eclipse.notifyUser("Syntax: bind <module> <code>");
            return;
        } else if(args.length == 1) {
            Eclipse.notifyUser("I need a key");
            return;
        }

        Module module = ModuleManager.getByName(args[0].toLowerCase());
        if(module == null) {
            Eclipse.notifyUser("Invalid module!");
            return;
        }

        int code = -1;

        // I know this doesn't work, but this was before there was ANY gui.
        // So this did the trick, not really meant to be used anymore though.
        // You still can if you want, however.

        for(int i = 0; i < 256; i++) {
            int scancode = GLFW.glfwGetKeyScancode(i);

            if(Objects.equals(GLFW.glfwGetKeyName(i, scancode), args[1].toLowerCase()))
                code = i;

            if(Objects.equals(GLFW.glfwGetKeyName(i, scancode), args[1].toUpperCase()))
                code = i;
        }

        if(code == -1) {
            Eclipse.notifyUser("Invalid key!");
            return;
        }

        module.keybind = code < 8 ?
                Keybind.mouse(code, false) :
                Keybind.key(code, false);

        String formattedKey = Strings.format(module.keybind.toString());

        Eclipse.notifyUser("Bound " + module + " to " + formattedKey);
    }
}
