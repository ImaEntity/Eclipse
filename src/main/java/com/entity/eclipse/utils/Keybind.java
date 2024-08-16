package com.entity.eclipse.utils;

import com.entity.eclipse.Eclipse;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class Keybind {
    private final int code;
    private final boolean tor;
    private final boolean isKey;
    private boolean wasPressed0;
    private boolean wasPressed1;

    private Keybind(int code, boolean isKey, boolean tor) {
        this.code = code;
        this.isKey = isKey;
        this.tor = tor;
    }

    public static Keybind unbound() {
        return new Keybind(GLFW.GLFW_KEY_UNKNOWN, true, false);
    }
    public static Keybind key(int code, boolean tor) {
        return new Keybind(code, true, tor);
    }
    public static Keybind mouse(int button, boolean tor) {
        return new Keybind(button, false, tor);
    }
    public boolean togglesOnRelease() {
        return this.tor;
    }

    public static boolean canBindTo(int code, boolean isKey) {
        if(code == GLFW.GLFW_KEY_UNKNOWN) return false;

        if(isKey) return code != GLFW.GLFW_KEY_ESCAPE;
        return code != GLFW.GLFW_MOUSE_BUTTON_LEFT && code != GLFW.GLFW_MOUSE_BUTTON_RIGHT;
    }

    public boolean isKey() {
        return this.isKey;
    }
    public int getCode() {
        return this.code;
    }
    public boolean isUnbound() {
        return this.code == GLFW.GLFW_KEY_UNKNOWN;
    }

    public boolean isHeld() {
        if(this.code < 0) return false;

        if(!this.isKey) {
            return GLFW.glfwGetMouseButton(Eclipse.client.getWindow().getHandle(), this.code) == GLFW.GLFW_PRESS
                    && Eclipse.client.currentScreen == null;
        }

        return InputUtil.isKeyPressed(Eclipse.client.getWindow().getHandle(), this.code)
                && Eclipse.client.currentScreen == null;
    }

    public boolean isPressed() {
        if(Eclipse.client.currentScreen != null) return false;
        if(this.code < 0) return false;

        boolean isHeld = this.isHeld();

        if(isHeld && !this.wasPressed0) {
            this.wasPressed0 = true;
            return true;
        }

        if(!isHeld)
            this.wasPressed0 = false;

        return false;
    }

    public boolean wasPressed() {
        if(Eclipse.client.currentScreen != null) return false;
        if(this.code < 0) return false;

        boolean isHeld = this.isHeld();

        if(!isHeld && this.wasPressed1) {
            this.wasPressed1 = false;
            return true;
        }

        if(isHeld)
            this.wasPressed1 = true;

        return false;
    }

    @Override
    public String toString() {
        if(this.isUnbound()) return "None";

        String keyname = GLFW.glfwGetKeyName(this.code, GLFW.glfwGetKeyScancode(this.code));

        return this.isKey ?
                (keyname != null ? keyname : "key." + this.code) :
                "Mouse Button " + (this.code + 1);
    }
}
