package com.entity.eclipse.utils;

import net.minecraft.client.input.Input;
import net.minecraft.util.PlayerInput;

public class MutableInput extends Input {
    @Override
    public void tick() {
        this.movementForward = this.playerInput.forward() == this.playerInput.backward() ? 0f : (this.playerInput.forward() ? 1f : -1f);
        this.movementSideways = this.playerInput.left() == this.playerInput.right() ? 0f : (this.playerInput.left() ? 1f : -1f);
    }

    public void stop() {
        this.playerInput = PlayerInput.DEFAULT;
    }

    public boolean forward() {
        return this.playerInput.forward();
    }
    public void forward(boolean isForward) {
        this.playerInput = new PlayerInput(
                isForward,
                this.backward(),
                this.left(),
                this.right(),
                this.isJump(),
                this.sneak(),
                this.sprint()
        );
    }

    public boolean backward() {
        return this.playerInput.backward();
    }
    public void backward(boolean isBack) {
        this.playerInput = new PlayerInput(
                this.forward(),
                isBack,
                this.left(),
                this.right(),
                this.isJump(),
                this.sneak(),
                this.sprint()
        );
    }

    public boolean left() {
        return this.playerInput.left();
    }
    public void left(boolean isLeft) {
        this.playerInput = new PlayerInput(
                this.forward(),
                this.backward(),
                isLeft,
                this.right(),
                this.isJump(),
                this.sneak(),
                this.sprint()
        );
    }

    public boolean right() {
        return this.playerInput.right();
    }
    public void right(boolean isRight) {
        this.playerInput = new PlayerInput(
                this.forward(),
                this.backward(),
                this.left(),
                isRight,
                this.isJump(),
                this.sneak(),
                this.sprint()
        );
    }

    public boolean isJump() {
        return this.playerInput.jump();
    }
    public void jump(boolean isJumping) {
        this.playerInput = new PlayerInput(
                this.forward(),
                this.backward(),
                this.left(),
                this.right(),
                isJumping,
                this.sneak(),
                this.sprint()
        );
    }

    public boolean sneak() {
        return this.playerInput.sneak();
    }
    public void sneak(boolean isSneaking) {
        this.playerInput = new PlayerInput(
                this.forward(),
                this.backward(),
                this.left(),
                this.right(),
                this.isJump(),
                isSneaking,
                this.sprint()
        );
    }

    public boolean sprint() {
        return this.playerInput.sprint();
    }
    public void sprint(boolean isSprinting) {
        this.playerInput = new PlayerInput(
                this.forward(),
                this.backward(),
                this.left(),
                this.right(),
                this.isJump(),
                this.sneak(),
                isSprinting
        );
    }
}
