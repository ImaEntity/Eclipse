package com.entity.eclipse.utils;

import net.minecraft.text.ClickEvent;

public class RunnableClickEvent implements ClickEvent {
    private final Runnable lamb;
    public RunnableClickEvent(Runnable lamb) {
        this.lamb = lamb;
    }

    public Runnable getRunnable() {
        return this.lamb;
    }

    @Override
    public Action getAction() {
        return null;
    }
}
