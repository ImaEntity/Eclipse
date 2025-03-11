package com.entity.eclipse.utils.events.render;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public class Render2DEvent {
    private final DrawContext context;
    private final RenderTickCounter tickCounter;

    private boolean cancelled = false;

    public Render2DEvent(DrawContext context, RenderTickCounter tickCounter) {
        this.context = context;
        this.tickCounter = tickCounter;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public DrawContext getContext() {
        return this.context;
    }
    public RenderTickCounter getTickCounter() {
        return this.tickCounter;
    }
}
