package com.entity.eclipse.commands;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.commands.base.Command;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.Keybind;
import com.entity.eclipse.utils.Strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Panic extends Command {
    private final ArrayList<Module> modules = new ArrayList<>();
    private final HashMap<Module, Keybind> keybinds = new HashMap<>();

    public Panic() {
        super("Panic", "OHFUCKOHFUCKOHFUCK", "panic", "p", "fuck");
    }

    @Override
    public void onExecute(String[] args) {
        String option = args.length != 0 ? args[0] : "";

        if(option.equalsIgnoreCase("restore")) {
            for(Module module : modules)
                ModuleManager.enable(module);

            for(Map.Entry<Module, Keybind> entry : keybinds.entrySet())
                entry.getKey().keybind = entry.getValue();

            modules.clear();
            keybinds.clear();

            return;
        }

        try {
            for(Module module : ModuleManager.getModules()) {
                if(!option.equalsIgnoreCase("hard")) continue;

                keybinds.put(module, module.keybind);
                module.keybind = Keybind.unbound();
            }

            for(Module module : ModuleManager.getActiveModules()) {
                if(module.getType() == ModuleType.RENDER && !option.equalsIgnoreCase("hard")) continue;

                modules.add(module);

                // Queue for next tick, prevents ConcurrentModificationException
                ModuleManager.queueDisable(module);
            }
        } catch(Exception e) {
            e.printStackTrace();
            Eclipse.notifyUser(e.getMessage());
        }

        if(!option.equalsIgnoreCase("hard")) return;

        Eclipse.client.inGameHud.getChatHud().clear(true);
    }
}
