package com.entity.eclipse.commands;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.commands.base.Command;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.utils.Strings;
import com.entity.eclipse.utils.types.DynamicValue;

import java.util.Arrays;
import java.util.Set;

public class Config extends Command {
    public Config() {
        super("Config", "Configures modules", "config", "conf");
    }

    @Override
    public void onExecute(String[] args) {
        if(args.length == 0) {
            Eclipse.notifyUser("Syntax: config <module> [setting] [value]");
            return;
        }

        Module module = ModuleManager.getByName(args[0]);
        if(module == null) {
            Eclipse.notifyUser("Invalid module!");
            return;
        }

        if(args.length == 1) {
            try {
                Set<String> settings = module.config.getAll();

                if(settings.isEmpty()) {
                    Eclipse.notifyUser(module + " has no settings");
                    return;
                }

                Eclipse.notifyUser(module + " has the following settings:");
                for(String setting : settings) {
                    String value = module.config.getRaw(setting).toString();
                    Eclipse.notifyUser(Strings.format(setting) + " - " + value);
                }
            } catch(Exception e) {
                e.printStackTrace();
                Eclipse.notifyUser(e.toString());
            }

            return;
        }

        String wasInject = args.length == 2 ? " is " : " was ";
        String settingName = args[1];

        // Makes settingName case-insensitive
        for(String caseSensitiveName : module.config.getAll())
            if(caseSensitiveName.equalsIgnoreCase(settingName))
                settingName = caseSensitiveName;

        DynamicValue<?> value = module.config.getRaw(settingName);
        String formattedKey = Strings.format(settingName);

        if(value == null) {
            Eclipse.notifyUser(module + " doesn't have the setting " + formattedKey);
            return;
        }

        // Ex: "The value of Flight.SprintMultiplier was 1.2345"
        Eclipse.notifyUser("The value of " + module + "." + formattedKey + wasInject + value);

        if(args.length > 2) {
            args = Arrays.copyOfRange(args, 2, args.length);
            DynamicValue<?> newValue = value.fromString(String.join(" ", args));

            Eclipse.notifyUser("Set " + module + "." + formattedKey + " to " + newValue);
            module.config.create(settingName, newValue);
        }
    }
}
