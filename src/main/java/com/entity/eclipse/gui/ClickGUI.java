package com.entity.eclipse.gui;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.Strings;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

// Fuck my life
public class ClickGUI extends Screen {
    private final ArrayList<Module> modules;
    private final ModuleType[] types = ModuleType.values();
    private final double padding = 3;

    public ClickGUI() {
        super(Text.of("Click GUI"));
        this.modules = ModuleManager.getModules();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(Eclipse.openGUIKey.matchesKey(keyCode, scanCode)) {
            this.close();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for(int i = 0; i < this.types.length; i++) {
            ArrayList<Module> ourModules = new ArrayList<>();

            for(Module module : this.modules)
                if(module.getType() == this.types[i]) ourModules.add(module);

            double width = (this.width - (this.types.length + 1) * this.padding) / this.types.length;
            double x = i * (this.padding + width);

            for(int j = 0; j < ourModules.size(); j++) {
                Module module = ourModules.get(j);

                if(
                        mouseX >= x + this.padding * 2 &&
                        mouseX <= x + width &&
                        mouseY >= this.padding * 4 + (this.textRenderer.fontHeight + this.padding) * (j + 1) - this.textRenderer.fontHeight / 2.0 &&
                        mouseY <= this.padding * 5 + (this.textRenderer.fontHeight + this.padding) * (j + 1) + this.textRenderer.fontHeight / 2.0
                ) {
                    if(button == GLFW.GLFW_MOUSE_BUTTON_LEFT) ModuleManager.toggle(module);
                    else if(button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
                        this.close();
                        Eclipse.client.setScreen(new ModuleSettingsGUI(module));
                    }
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float tickDelta) {
        for(int i = 0; i < this.types.length; i++) {
            ArrayList<Module> ourModules = new ArrayList<>();

            for(Module module : this.modules)
                if(module.getType() == this.types[i]) ourModules.add(module);

            double width = (this.width - (this.types.length + 1) * this.padding) / this.types.length;
            double x = i * (this.padding + width);
            double height = (this.padding * 3 + (this.textRenderer.fontHeight + this.padding) * (ourModules.size()));

            context.fill(
                    (int) (this.padding + x),
                    (int) (this.padding * 3 + this.textRenderer.fontHeight),
                    (int) (this.padding + x + width),
                    (int) (this.padding * 3 + this.textRenderer.fontHeight / 2 + height),
                    0xEE000000
            );

            context.fill(
                    (int) (this.padding + x),
                    (int) this.padding,
                    (int) (this.padding + x + width),
                    (int) (this.padding * 3 + this.textRenderer.fontHeight),
                    0xFF000000
            );

            context.drawCenteredTextWithShadow(
                    this.textRenderer,
                    this.types[i].name(),
                    (int) (this.padding + x + width / 2),
                    (int) (this.padding * 2),
                    0xFFAA00
            );

            for(int j = 0; j < ourModules.size(); j++) {
                Module module = ourModules.get(j);

                String formattedName = Strings.camelToReadable(module.getName());
                String enabledString = module.isEnabled() ? "✔" : "❌";

                int textWidth = this.textRenderer.getWidth(enabledString);
                double y = this.padding * 3 + (this.textRenderer.fontHeight + this.padding) * (j + 1);

                context.drawTextWithShadow(
                        this.textRenderer,
                        formattedName,
                        (int) (2 * this.padding + x),
                        (int) y,
                        0xAAAAAA
                );

                context.drawTextWithShadow(
                        this.textRenderer,
                        enabledString,
                        (int) (x + width - textWidth),
                        (int) y,
                        module.isEnabled() ? 0x55FF55 : 0xFF5555
                );
            }
        }
    }
}
