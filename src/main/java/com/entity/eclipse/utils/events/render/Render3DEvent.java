package com.entity.eclipse.utils.events.render;

public class Render3DEvent {
    private boolean cancelled = false;

    public Render3DEvent() {

    }

    public boolean isCancelled() {
        return this.cancelled;
    }
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
