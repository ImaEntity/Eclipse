package com.entity.eclipse.gui;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.utils.Keybind;
import com.entity.eclipse.utils.Strings;
import com.entity.eclipse.utils.types.BooleanValue;
import com.entity.eclipse.utils.types.DynamicValue;
import com.entity.eclipse.utils.types.ListValue;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

// Fuck my life again
public class ModuleSettingsGUI extends Screen {
    private final Module module;
    private final double padding = 3;
    private final HashMap<String, Boolean> isCollapsed = new HashMap<>();

    private boolean listeningForKey = false;
    private String keyboardInput = "";
    private boolean listeningForValue = false;
    private String valueSettingName = "";
    private int valueListIndex = -1;
    private double prevHeight = -1;

    public ModuleSettingsGUI(Module module) {
        super(Text.of("Module Settings"));
        this.module = module;

        Set<String> config = this.module.config.getAll();
        for(String key : config) {
            DynamicValue<?> value = this.module.config.getRaw(key);
            if(!(value instanceof ListValue list)) continue;

            this.isCollapsed.put(key, list.size() > 10);
        }
    }

    @Override
    public void close() {
        super.close();
        Eclipse.client.setScreen(new ClickGUI());
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(this.listeningForKey) {
            if(Keybind.canBindTo(keyCode, true))
                this.module.keybind = Keybind.key(keyCode);
            else
                this.module.keybind = Keybind.unbound();

            this.listeningForKey = false;
            return false;
        }

        if(this.listeningForValue) {
            if(keyCode == GLFW.GLFW_KEY_ESCAPE) {
                this.listeningForValue = false;
                this.keyboardInput = "";
                this.valueListIndex = -1;
                this.valueSettingName = "";

                return false;
            }

            if(keyCode == GLFW.GLFW_KEY_BACKSPACE && this.keyboardInput.length() > 0)
                this.keyboardInput = this.keyboardInput.substring(0, this.keyboardInput.length() - 1);

            if(keyCode == GLFW.GLFW_KEY_ENTER) {
                this.listeningForValue = false;

                DynamicValue<?> value = this.module.config.getRaw(this.valueSettingName);

                try {
                    if(value instanceof ListValue list)
                        list.set(this.valueListIndex, list.get(this.valueListIndex).fromString(this.keyboardInput));
                    else
                        this.module.config.create(this.valueSettingName, value.fromString(this.keyboardInput));
                } catch(Exception e) {
                    e.printStackTrace();
                    Eclipse.notifyUser("Invalid value!");
                }

                this.keyboardInput = "";
                this.valueSettingName = "";
                this.valueListIndex = -1;

                return false;
            }

            if(GLFW.glfwGetKeyName(keyCode, scanCode) == null)
                return super.keyPressed(keyCode, scanCode, modifiers);

            String textToAppend = new StringBuilder().appendCodePoint(keyCode).toString();
            boolean shifted = (modifiers & GLFW.GLFW_MOD_SHIFT) == GLFW.GLFW_MOD_SHIFT;
            char c = textToAppend.charAt(0);

            textToAppend = textToAppend.toLowerCase();

            if(shifted) {
                if(c > 'a' && c < 'z') textToAppend = textToAppend.toUpperCase();
                else if(c == '`') textToAppend = "~";
                else if(c == '1') textToAppend = "!";
                else if(c == '2') textToAppend = "@";
                else if(c == '3') textToAppend = "#";
                else if(c == '4') textToAppend = "$";
                else if(c == '5') textToAppend = "%";
                else if(c == '6') textToAppend = "^";
                else if(c == '7') textToAppend = "&";
                else if(c == '8') textToAppend = "*";
                else if(c == '9') textToAppend = "(";
                else if(c == '0') textToAppend = ")";
                else if(c == '-') textToAppend = "_";
                else if(c == '=') textToAppend = "+";
                else if(c == '[') textToAppend = "{";
                else if(c == ']') textToAppend = "}";
                else if(c == '\\') textToAppend = "|";
                else if(c == ';') textToAppend = ":";
                else if(c == '\'') textToAppend = "\"";
                else if(c == ',') textToAppend = "<";
                else if(c == '.') textToAppend = ">";
                else if(c == '/') textToAppend = "?";
            }

            this.keyboardInput += textToAppend;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(this.listeningForKey) {
            if(Keybind.canBindTo(button, false))
                this.module.keybind = Keybind.mouse(button);

            this.listeningForKey = false;
            return false;
        }

        int left = this.width / 2 - this.width / 6;
        int right = this.width / 2 + this.width / 6;

        Set<String> settings = this.module.config.getAll();
        double y = this.padding * 3 + (this.textRenderer.fontHeight + this.padding) * 2;

        for(int i = 0; i < settings.size(); i++) {
            String settingName = settings.toArray(new String[0])[i];
            DynamicValue<?> value = this.module.config.getRaw(settingName);

            int settingNameWidth = this.textRenderer.getWidth(Strings.camelToReadable(settingName));

            if(value instanceof ListValue list) {
                String addButton = "+";
                int addButtonWidth = this.textRenderer.getWidth(addButton);

                if(
                        mouseX >= right - this.padding - addButtonWidth &&
                        mouseX <= right - this.padding &&
                        mouseY >= y &&
                        mouseY <= y + this.textRenderer.fontHeight &&
                        button == GLFW.GLFW_MOUSE_BUTTON_LEFT
                ) {
                    if(this.isCollapsed.get(settingName))
                        this.isCollapsed.put(settingName, false);

                    this.listeningForValue = true;
                    this.valueSettingName = settingName;
                    this.valueListIndex = list.size();

                    list.add(list.newElementInstance());
                }

                if(this.isCollapsed.get(settingName)) {
                    int collapsedTextWidth = this.textRenderer.getWidth("(...)");

                    if(
                            mouseX >= left + this.padding &&
                            mouseX <= left + this.padding + settingNameWidth + 2 * this.padding + collapsedTextWidth &&
                            mouseY >= y &&
                            mouseY <= y + this.textRenderer.fontHeight &&
                            button == GLFW.GLFW_MOUSE_BUTTON_LEFT
                    ) {
                        this.isCollapsed.put(settingName, false);
                    }

                    y += this.textRenderer.fontHeight;
                    y += this.padding;

                    continue;
                }

                if(
                        mouseX >= left + this.padding &&
                        mouseX <= left + this.padding + settingNameWidth &&
                        mouseY >= y &&
                        mouseY <= y + this.textRenderer.fontHeight &&
                        button == GLFW.GLFW_MOUSE_BUTTON_LEFT
                ) {
                    this.isCollapsed.put(settingName, true);
                }

                y += this.textRenderer.fontHeight;

                ArrayList<DynamicValue<?>> toRemove = new ArrayList<>();

                for(DynamicValue<?> element : list.getValue()) {
                    int elementWidth = this.textRenderer.getWidth(element.toString());

                    if(
                            mouseX >= left + this.padding + this.textRenderer.getWidth("\t") &&
                            mouseX <= left + this.padding + this.textRenderer.getWidth("\t") + elementWidth &&
                            mouseY >= y &&
                            mouseY <= y + this.textRenderer.fontHeight &&
                            button == GLFW.GLFW_MOUSE_BUTTON_LEFT
                    ) {
                        this.listeningForValue = true;
                        this.valueSettingName = settingName;
                        this.valueListIndex = list.indexOf(element);
                    }

                    String deleteButton = "-";
                    int deleteButtonWidth = this.textRenderer.getWidth(deleteButton);

                    if(
                            mouseX >= right - this.padding - deleteButtonWidth &&
                            mouseX <= right - this.padding &&
                            mouseY >= y &&
                            mouseY <= y + this.textRenderer.fontHeight &&
                            button == GLFW.GLFW_MOUSE_BUTTON_LEFT
                    ) {
                        toRemove.add(element);
                    }

                    y += this.textRenderer.fontHeight;
                }

                for(DynamicValue<?> queuedValue : toRemove)
                    list.remove(queuedValue);

                y += this.padding;

                continue;
            }

            int settingValueWidth = this.textRenderer.getWidth(value.toString());

            if(
                    ((mouseX >= left + this.padding &&
                    mouseX <= left + this.padding + settingNameWidth) ||
                    (mouseX >= right - this.padding - settingValueWidth &&
                    mouseX <= right - this.padding)) &&
                    mouseY >= y &&
                    mouseY <= y + this.textRenderer.fontHeight &&
                    button == GLFW.GLFW_MOUSE_BUTTON_LEFT
            ) {
                if(!(value instanceof BooleanValue bool)) {
                    this.valueSettingName = settingName;
                    this.listeningForValue = true;
                } else
                    bool.setValue(!bool.getValue());
            }

            y += this.textRenderer.fontHeight + this.padding;
        }

        if(!settings.isEmpty())
            y += this.textRenderer.fontHeight + this.padding;

        String keybindString = "Keybind";
        String keybindText = this.module.keybind.toString();
        int keybindStringWidth = this.textRenderer.getWidth(keybindString);
        int keybindTextWidth = this.textRenderer.getWidth(keybindText);

        if(
                ((mouseX >= left + this.padding &&
                mouseX <= left + this.padding + keybindStringWidth) ||
                (mouseX >= right - this.padding - keybindTextWidth &&
                mouseX <= right - this.padding)) &&
                mouseY >= y &&
                mouseY <= y + this.textRenderer.fontHeight &&
                button == GLFW.GLFW_MOUSE_BUTTON_LEFT
        ) {
            this.listeningForKey = true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float tickDelta) {
        if(this.listeningForKey) {
            String pressKeyText = "Press any key.";
            int pressKeyTextWidth = this.textRenderer.getWidth(pressKeyText);

            context.fill(
                    (int) (this.width / 2 - pressKeyTextWidth / 2 - this.padding),
                    (int) (this.height / 2.0 - this.padding),
                    (int) (this.width / 2 + pressKeyTextWidth / 2 + this.padding),
                    (int) (this.height / 2.0 + this.textRenderer.fontHeight + this.padding),
                    0xEE000000
            );

            context.drawCenteredTextWithShadow(
                    this.textRenderer,
                    pressKeyText,
                    this.width / 2,
                    this.height / 2,
                    0xFFAA00
            );

            return;
        }

        int left = this.width / 2 - this.width / 6;
        int right = this.width / 2 + this.width / 6;

        context.fill(
                left,
                (int) (this.padding),
                right,
                (int) (this.padding * 4 + this.textRenderer.fontHeight * 2),
                0xFF000000
        );

        context.fill(
                left,
                (int) (this.padding * 4 + this.textRenderer.fontHeight * 2),
                right,
                (int) (this.padding * 4 + this.textRenderer.fontHeight * 2 + this.prevHeight),
                0xEE000000
        );

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                Strings.camelToReadable(this.module.getName()),
                this.width / 2,
                (int) (this.padding * 2),
                0xFFAA00
        );

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                this.module.getDescription(),
                this.width / 2,
                (int) (this.padding * 3 + this.textRenderer.fontHeight),
                0xFFAA00
        );

        Set<String> settings = this.module.config.getAll();
        double y = this.padding * 3 + (this.textRenderer.fontHeight + this.padding) * 2;

        for(int i = 0; i < settings.size(); i++) {
            String settingName = settings.toArray(new String[0])[i];
            DynamicValue<?> value = this.module.config.getRaw(settingName);

            context.drawTextWithShadow(
                    this.textRenderer,
                    Strings.camelToReadable(settingName),
                    (int) (left + this.padding),
                    (int) y,
                    0xAAAAAA
            );

            if(value instanceof ListValue list) {
                String addButton = "+";
                int addButtonWidth = this.textRenderer.getWidth(addButton);

                context.drawTextWithShadow(
                        this.textRenderer,
                        addButton,
                        (int) (right - this.padding - addButtonWidth),
                        (int) y,
                        0xFFFFFF
                );

                if(this.isCollapsed.get(settingName)) {
                    int settingNameWidth = this.textRenderer.getWidth(Strings.camelToReadable(settingName));

                    context.drawTextWithShadow(
                            this.textRenderer,
                            "(...)",
                            (int) (left + this.padding + settingNameWidth + 2 * this.padding),
                            (int) y,
                            0xFFFFFF
                    );

                    y += this.textRenderer.fontHeight;
                    y += this.padding;

                    continue;
                }

                y += this.textRenderer.fontHeight;

                if(list.isEmpty()) {
                    context.drawTextWithShadow(
                            this.textRenderer,
                            "[EMPTY_LIST]",
                            (int) (left + this.padding + this.textRenderer.getWidth("\t")),
                            (int) y,
                            0x555555
                    );

                    y += this.textRenderer.fontHeight;
                    y += this.padding;

                    continue;
                }

                for(DynamicValue<?> element : list.getValue()) {
                    String elementText = element.toString();
                    int index = list.indexOf(element);

                    if(this.valueSettingName.equals(settingName) && index == this.valueListIndex) {
                        elementText = this.keyboardInput;
                        int elementTextWidth = this.textRenderer.getWidth(elementText);

                        context.fill(
                                left + this.textRenderer.getWidth("\t"),
                                (int) (y - this.padding),
                                (int) (left + this.padding + this.textRenderer.getWidth("\t") + elementTextWidth + this.padding),
                                (int) (y + this.textRenderer.fontHeight + this.padding),
                                0xFF555555
                        );
                    }

                    context.drawTextWithShadow(
                            this.textRenderer,
                            elementText,
                            (int) (left + this.padding + this.textRenderer.getWidth("\t")),
                            (int) y,
                            0xFFFFFF
                    );

                    String deleteButton = "-";
                    int deleteButtonWidth = this.textRenderer.getWidth(deleteButton);

                    context.drawTextWithShadow(
                            this.textRenderer,
                            deleteButton,
                            (int) (right - this.padding - deleteButtonWidth),
                            (int) y,
                            0xFFFFFF
                    );

                    y += this.textRenderer.fontHeight;
                }

                y += this.padding;

                continue;
            }

            String settingValue = value.toString();
            int valueWidth = this.textRenderer.getWidth(settingValue);

            if(this.valueSettingName.equals(settingName)) {
                settingValue = this.keyboardInput;
                valueWidth = this.textRenderer.getWidth(settingValue);

                context.fill(
                        (int) (right - this.padding - valueWidth - this.padding),
                        (int) (y - this.padding),
                        right,
                        (int) (y + this.textRenderer.fontHeight + this.padding),
                        0xFF555555
                );
            }

            context.drawTextWithShadow(
                    this.textRenderer,
                    settingValue,
                    (int) (right - this.padding - valueWidth),
                    (int) y,
                    0xFFFFFF
            );

            y += this.textRenderer.fontHeight + this.padding;
        }

        if(!settings.isEmpty())
            y += this.textRenderer.fontHeight + this.padding;

        String keybindText = this.module.keybind.toString();
        int keybindTextWidth = this.textRenderer.getWidth(keybindText);

        context.drawTextWithShadow(
                this.textRenderer,
                "Keybind",
                (int) (left + this.padding),
                (int) y,
                0xAAAAAA
        );

        context.drawTextWithShadow(
                this.textRenderer,
                keybindText,
                (int) (right - this.padding - keybindTextWidth),
                (int) y,
                0xAAAAAA
        );

        y += this.textRenderer.fontHeight;
        y += this.padding;

        this.prevHeight = y - this.padding * 4 - this.textRenderer.fontHeight * 2;
    }
}
