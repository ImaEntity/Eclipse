package com.entity.eclipse.utils;

import net.minecraft.text.ClickEvent;

public class RunnableClickEvent extends ClickEvent {
    private final Runnable lamb;
    public RunnableClickEvent(Runnable lamb) {
        super(null, null);
        this.lamb = lamb;
    }

    public Runnable getRunnable() {
        return this.lamb;
    }
}
