package com.entity.eclipse.mixin;

import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.chat.ChatEvent;
import com.entity.eclipse.utils.events.chat.ChatEvents;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class ChatHudMixin {
    @Inject(
            at = @At("HEAD"),
            method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
            cancellable = true
    )
    private void fireChatEvent(Text message, MessageSignatureData signatureData, MessageIndicator indicator, CallbackInfo info) {
        boolean isCancelled = Events.Chat.fireEvent(ChatEvents.RECEIVE, new ChatEvent(
                message,
                signatureData,
                indicator
        ));

        if(isCancelled)
            info.cancel();
    }
}
