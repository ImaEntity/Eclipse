package com.entity.eclipse;

import com.entity.eclipse.gui.ClickGUI;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.utils.ConfigManager;
import com.entity.eclipse.utils.Configuration;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.tick.TickEvents;
import com.entity.eclipse.utils.scripting.ScriptingManager;
import com.entity.eclipse.utils.types.BooleanValue;
import com.entity.eclipse.utils.types.StringValue;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Eclipse implements ModInitializer {
	public static final String MOD_ID = "eclipse";
	public static final String MOD_NAME = "Eclipse";
	public static final String MC_VERSION = SharedConstants.getGameVersion().getName();
	public static final String VERSION = "v" + FabricLoader
			.getInstance()
			.getModContainer(MOD_ID)
			.orElseThrow(() -> new RuntimeException("Wtf???"))
			.getMetadata()
			.getVersion()
			.getFriendlyString()
			.split("\\+")[0];

	private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final MinecraftClient client = MinecraftClient.getInstance();
	public static final Configuration config = new Configuration();

	public static final Context jsEngine = ScriptingManager.createEngine();
	public static Scriptable engineScope = ScriptingManager.createScope(jsEngine);

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
	public static void notifyUserRaw(Text message) {
		notifyUserRaw(message, false);
	}

	public static void notifyUser(String message, boolean actionBar) {
		if(client.player == null)
			return;

		client.player.sendMessage(
				Text.literal(!actionBar ? "[§6§lEcl§0§lipse§r] " : "").append(message),
				actionBar
		);
	}

	public static void notifyUserRaw(Text message, boolean actionBar) {
		if(client.player == null)
			return;

		client.player.sendMessage(
				Text.literal(!actionBar ? "[§6§lEcl§0§lipse§r] " : "").append(message),
				actionBar
		);
	}

	@Override
	public void onInitialize() {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			ConfigManager.saveState();
			ModuleManager.removeAllExternals();

			for(Module module : ModuleManager.getModules())
				if(module.isEnabled()) ModuleManager.disable(module);
		}));

		// Assign default values in case a config hasn't been created yet
		config.create("ChatPrefix", new StringValue("."));
		config.create("ShouldNotifyUpdates", new BooleanValue(true));

		// Load scripts first so script configs get loaded correctly
		ScriptingManager.loadScripts(jsEngine, engineScope);
		ConfigManager.loadState();

		Events.Tick.register(TickEvents.START, event -> {
			if(openGUIKey.wasPressed())
				client.setScreen(new ClickGUI());
		});
	}
}