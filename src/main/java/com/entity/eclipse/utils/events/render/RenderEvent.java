package com.entity.eclipse.utils.events.render;

import net.minecraft.client.util.math.MatrixStack;

public class RenderEvent {
    private final MatrixStack matrices;
    private final float tickDelta;
    private final double offX;
    private final double offY;
    private final double offZ;

    private boolean cancelled = false;

    public RenderEvent(MatrixStack matrices, float tickDelta, double offX, double offY, double offZ) {
        this.matrices = matrices;
        this.tickDelta = tickDelta;
        this.offX = offX;
        this.offY = offY;
        this.offZ = offZ;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public MatrixStack getMatrices() {
        return this.matrices;
    }
    public float getTickDelta() {
        return this.tickDelta;
    }
    public double getOffX() {
        return this.offX;
    }
    public double getOffY() {
        return this.offY;
    }
    public double getOffZ() {
        return this.offZ;
    }
}
