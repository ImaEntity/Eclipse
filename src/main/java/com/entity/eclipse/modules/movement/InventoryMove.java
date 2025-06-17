package com.entity.eclipse.modules.movement;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.gui.ClickGUI;
import com.entity.eclipse.gui.ClientSettingsGUI;
import com.entity.eclipse.gui.ModuleSettingsGUI;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.InputUtils;
import com.entity.eclipse.utils.events.render.Render2DEvent;
import com.entity.eclipse.utils.events.render.Render3DEvent;
import com.entity.eclipse.utils.types.BooleanValue;
import com.entity.eclipse.utils.types.FloatValue;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.option.GameOptions;

public class InventoryMove extends Module {
    public InventoryMove() {
        super("InventoryMove", "Lets you move inside GUIs.", ModuleType.MOVEMENT);

        this.config.create("UseMouse", new BooleanValue(false));
        this.config.create("MouseSensitivity", new FloatValue(1f))
                .visibleIf(() -> this.config.get("UseMouse"));
    }

    @Override
    public void tick() {
        // "if" statement can be replaced with "switch" statement
        //     -Intellij
        switch(Eclipse.client.currentScreen) {
            case null                -> { return; }
            case ChatScreen c        -> { return; }
            case ClickGUI c          -> { return; }
            case ClientSettingsGUI c -> { return; }
            case ModuleSettingsGUI m -> { return; }
            default -> {}
        }

        GameOptions opt = Eclipse.client.options;

        opt.forwardKey.setPressed(InputUtils.isKeyPressed(opt.forwardKey));
        opt.backKey.setPressed(InputUtils.isKeyPressed(opt.backKey));
        opt.leftKey.setPressed(InputUtils.isKeyPressed(opt.leftKey));
        opt.rightKey.setPressed(InputUtils.isKeyPressed(opt.rightKey));
        opt.jumpKey.setPressed(InputUtils.isKeyPressed(opt.jumpKey));
        opt.sneakKey.setPressed(InputUtils.isKeyPressed(opt.sneakKey));
        opt.sprintKey.setPressed(InputUtils.isKeyPressed(opt.sprintKey));
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void renderWorld(Render3DEvent event) {

    }

    @Override
    public void renderScreen(Render2DEvent event) {

    }
}
