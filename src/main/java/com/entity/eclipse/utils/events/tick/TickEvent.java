package com.entity.eclipse.utils.events.tick;

public class TickEvent {
    private boolean cancelled = false;

    public boolean isCancelled() {
        return this.cancelled;
    }
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
