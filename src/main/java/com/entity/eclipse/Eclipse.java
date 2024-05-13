package com.entity.eclipse;

import com.entity.eclipse.gui.ClickGUI;
import com.entity.eclipse.utils.Configuration;
import com.entity.eclipse.utils.SaveManager;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.tick.TickEvents;
import com.entity.eclipse.utils.types.StringValue;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Eclipse implements ModInitializer {
	public static final String MOD_ID = "eclipse";
	public static final String MOD_NAME = "Eclipse";
	public static MinecraftClient client = MinecraftClient.getInstance();
	public static Configuration config = new Configuration();

	private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final KeyBinding openGUIKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.eclipse.open_gui",
			GLFW.GLFW_KEY_RIGHT_SHIFT,
			"key.categories.eclipse"
	));

	public static void log(String message) {
		LOGGER.info("[" + MOD_NAME + "] " + message);
	}

	public static void notifyUser(String message) {
		if(client.player == null) return;
		client.player.sendMessage(Text.of("[§6§lEcl§0§lipse§r] " + message), false);
	}

	@Override
	public void onInitialize() {
		Runtime.getRuntime().addShutdownHook(new Thread(SaveManager::saveState));

		// Value must exist for SaveManager to change it
		config.create("chatPrefix", new StringValue("."));
		SaveManager.loadState();

		Events.Tick.register(TickEvents.START, event -> {
			if(openGUIKey.wasPressed())
				client.setScreen(new ClickGUI());
		});
	}
}