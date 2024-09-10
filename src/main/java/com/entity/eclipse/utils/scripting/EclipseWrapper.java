package com.entity.eclipse.utils.scripting;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.utils.Configuration;
import net.minecraft.client.MinecraftClient;
public class EclipseWrapper {
    public final String MOD_ID = Eclipse.MOD_ID;
    public final String MOD_NAME = Eclipse.MOD_NAME;
    public final String VERSION = Eclipse.VERSION;

    public MinecraftClient client = Eclipse.client;
    public Configuration config = Eclipse.config;

    public void log(String message) {
        Eclipse.log(message);
    }
    public void notifyUser(String message) {
        Eclipse.notifyUser(message);
    }
    public void notifyUser(String message, boolean actionBar) {
        Eclipse.notifyUser(message, actionBar);
    }
}
