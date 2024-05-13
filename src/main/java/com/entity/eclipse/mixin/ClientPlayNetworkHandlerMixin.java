package com.entity.eclipse.mixin;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.commands.base.Command;
import com.entity.eclipse.commands.base.CommandManager;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
	@Inject(at = @At("HEAD"), method = "sendChatMessage", cancellable = true)
	public void onChatMessage(String message, CallbackInfo info) {
		if(Eclipse.client == null) return;

		String chatPrefix = ((String) Eclipse.config.get("chatPrefix")).toLowerCase();
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
}