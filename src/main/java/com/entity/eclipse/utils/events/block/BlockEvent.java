package com.entity.eclipse.utils.events.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class BlockEvent {
    private final BlockPos position;
    private final BlockState state;
    private final Direction direction;
    private boolean cancelled = false;

    public BlockEvent(BlockPos position, Direction direction, BlockState state) {
        this.position = position;
        this.direction = direction;
        this.state = state;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    public BlockState getState() {
        return this.state;
    }
    public BlockPos getPosition() {
        return this.position;
    }
    public Direction getDirection() {
        return this.direction;
    }
}
