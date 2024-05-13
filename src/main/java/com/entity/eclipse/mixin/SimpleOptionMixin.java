package com.entity.eclipse.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;
import java.util.function.Consumer;

// This wasn't required until SOMEONE decided that
// adding a validation function to the SimpleOption
// class was a good idea. This just reverses it.

@Mixin(SimpleOption.class)
public class SimpleOptionMixin<T> {
	@Shadow
	T value;

	@Final
	@Shadow
	private Consumer<T> changeCallback;

	@Overwrite
	public void setValue(T value) {
		if(!MinecraftClient.getInstance().isRunning()) {
			this.value = value;
		} else {
			if(!Objects.equals(this.value, value)) {
				this.value = value;
				this.changeCallback.accept(this.value);
			}
		}
	}
}