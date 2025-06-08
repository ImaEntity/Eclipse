package com.entity.eclipse.mixin;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.commands.base.Command;
import com.entity.eclipse.commands.base.CommandManager;
import com.entity.eclipse.utils.UpdateManager;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.URI;
import java.util.Arrays;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
	@Inject(at = @At("HEAD"), method = "sendChatMessage", cancellable = true)
	public void onChatMessage(String message, CallbackInfo info) {
		if(Eclipse.client == null) return;

		String chatPrefix = ((String) Eclipse.config.get("ChatPrefix")).toLowerCase();
		if(!message.toLowerCase().startsWith(chatPrefix)) return;

		info.cancel();

		Eclipse.client.inGameHud.getChatHud().addToMessageHistory(message);

		String[] args = message.substring(chatPrefix.length()).split(" ");
		String command = args[0].toLowerCase();
		args = Arrays.copyOfRange(args, 1, args.length);

		Command cmd = CommandManager.getByName(command);
		if(cmd == null) Eclipse.notifyUser("Unknown command!");
		else {
			Eclipse.notifyUser(command);
			cmd.onExecute(args);
		}
	}

	@Inject(method = "onGameJoin", at = @At("TAIL"))
	public void checkForUpdate(CallbackInfo info) {
		UpdateManager.Update latest = UpdateManager.getLatest();
		if(latest.version().equalsIgnoreCase(Eclipse.VERSION))
			return;

		if(!latest.gameVersion().equalsIgnoreCase(Eclipse.MC_VERSION))
			return;

		if(!((boolean) Eclipse.config.get("ShouldNotifyUpdates")))
			return;

		Eclipse.notifyUser("New version available: " + latest.version());
		Eclipse.notifyUserRaw(Text.literal("")
				.append(Text.literal("Click "))
				.append(Text.literal("here")
						.setStyle(Style.EMPTY
								.withColor(Formatting.GOLD)
								.withUnderline(true)
								.withClickEvent(new ClickEvent.OpenUrl(URI.create(String.format(
										"https://github.com/ImaEntity/Eclipse/releases/download/%s/%s",
										latest.version(),
										latest.jarFileName()
								))))
								.withHoverEvent(new HoverEvent.ShowText(
										Text.of("Download " + latest.jarFileName() + "?")
								))
						)
				)
				.append(Text.of(" to download the latest update!"))
		);
	}
}