package com.entity.eclipse.utils.events.chat;

import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;

public class ChatEvent {
    private final Text message;
    private final MessageSignatureData signature;
    private final MessageIndicator indicator;

    private boolean cancelled = false;

    public ChatEvent(Text message, MessageSignatureData signature, MessageIndicator indicator) {
        this.message = message;
        this.signature = signature;
        this.indicator = indicator;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    public Text getMessage() {
        return this.message;
    }
    public MessageSignatureData getSignature() {
        return this.signature;
    }
    public MessageIndicator getIndicator() {
        return this.indicator;
    }
}
