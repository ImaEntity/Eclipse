package com.entity.eclipse;

import com.entity.eclipse.gui.ClickGUI;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.utils.ConfigManager;
import com.entity.eclipse.utils.Configuration;
import com.entity.eclipse.utils.scripting.ScriptingManager;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.tick.TickEvents;
import com.entity.eclipse.utils.types.StringValue;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.graalvm.polyglot.Context;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Eclipse implements ModInitializer {
	public static final String MOD_ID = "eclipse";
	public static final String MOD_NAME = "Eclipse";
	public static final String VERSION = "v" + FabricLoader
			.getInstance()
			.getModContainer(MOD_ID)
			.orElseThrow(() -> new RuntimeException("Wtf???"))
			.getMetadata()
			.getVersion()
			.getFriendlyString()
			.split("\\+")[0];

	private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static MinecraftClient client = MinecraftClient.getInstance();
	public static Configuration config = new Configuration();

	public static final Context jsEngine = ScriptingManager.createEngine("js");
	public static final KeyBinding openGUIKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.eclipse.open_gui",
			GLFW.GLFW_KEY_RIGHT_SHIFT,
			"key.categories.eclipse"
	));

	public static void log(String message) {
		LOGGER.info("[" + MOD_NAME + "] " + message);
	}

	public static void notifyUser(String message) {
		notifyUser(message, false);
	}

	public static void notifyUser(String message, boolean actionBar) {
		if(client.player == null)
			return;

		client.player.sendMessage(
				Text.of((!actionBar ? "[§6§lEcl§0§lipse§r] " : "") + message),
				actionBar
		);
	}

	@Override
	public void onInitialize() {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			ModuleManager.removeAllExternals();
			ConfigManager.saveState();

			for(Module module : ModuleManager.getModules())
				if(module.isEnabled()) ModuleManager.disable(module);
		}));

		ScriptingManager.loadBindings(jsEngine, "js");

		// Value must exist for SaveManager to change it
		config.create("chatPrefix", new StringValue("."));
		ConfigManager.loadState();

		ScriptingManager.loadScripts(jsEngine, "js");

		Events.Tick.register(TickEvents.START, event -> {
			if(openGUIKey.wasPressed())
				client.setScreen(new ClickGUI());
		});
	}
}