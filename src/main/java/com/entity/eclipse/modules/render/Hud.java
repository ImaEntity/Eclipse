package com.entity.eclipse.modules.render;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.packet.PacketEvents;
import com.entity.eclipse.utils.events.render.Render2DEvent;
import com.entity.eclipse.utils.events.render.Render3DEvent;
import com.entity.eclipse.utils.types.BooleanValue;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class Hud extends Module {
    private double tps;
    private long lastPacketTime;
    private final ArrayList<Double> tpsHistory = new ArrayList<>();
    private int tick = 0;

    public Hud() {
        super("Hud", "Shows various info on your HUD.", ModuleType.RENDER);

        this.config.create("ShowFps", new BooleanValue(true));
        this.config.create("ShowTps", new BooleanValue(true));
        this.config.create("ShowCoordinates", new BooleanValue(true));
        this.config.create("ShowPing", new BooleanValue(true));
        this.config.create("ShowSpeed", new BooleanValue(true));
        this.config.create("ShowFacing", new BooleanValue(true));

        Events.Packet.register(PacketEvents.RECEIVE, event -> {
            if(!(event.getPacket() instanceof WorldTimeUpdateS2CPacket)) return;
            this.lastPacketTime = System.currentTimeMillis();
        });
    }

    @Override
    public void tick() {
        this.tick++;

        // only update tps related shit every 100ms
        if(this.tick <= 2) return;
        this.tick = 0;

        long milliDiff = System.currentTimeMillis() - this.lastPacketTime;

        this.tpsHistory.add(20.0 / Math.max((milliDiff - 1000.0) / 500.0, 1.0));
        if(this.tpsHistory.size() > 50) // 50 entries * 100 ms = last 5 seconds of tps
            this.tpsHistory.removeFirst();

        this.tps = this.tpsHistory.stream().reduce(Double::sum).orElse(0.0) / this.tpsHistory.size();
    }

    @Override
    public void onEnable() {
        this.tick = 0;
        this.tpsHistory.clear();
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void renderWorld(Render3DEvent event) {

    }

    @Override
    public void renderScreen(Render2DEvent event) {
        if(Eclipse.client.player == null) return;
        if(Eclipse.client.getNetworkHandler() == null) return;

        int padding = 1;

        TextRenderer textRenderer = Eclipse.client.textRenderer;
        DrawContext context = event.getContext();

        ArrayList<String> left = new ArrayList<>();
        ArrayList<String> right = new ArrayList<>();

        if((boolean) this.config.get("ShowFps"))
            left.add(String.format("%d FPS", Eclipse.client.getCurrentFps()));

        Vec3d p = Eclipse.client.player.getPos();
        if((boolean) this.config.get("ShowCoordinates"))
            left.add(String.format("XYZ: %.3f, %.3f, %.3f", p.x, p.y, p.z));

        String direction = Eclipse.client.player.getFacing().asString();
        String axisDir = Eclipse.client.player.getFacing().getAxis().asString().toUpperCase();
        boolean positive = Eclipse.client.player.getFacing().getDirection() == Direction.AxisDirection.POSITIVE;
        if((boolean) this.config.get("ShowFacing"))
            left.add(String.format("Facing: %s; %c%s", direction, positive ? '+' : '-', axisDir));

        if((boolean) this.config.get("ShowSpeed"))
            left.add(String.format("%.3f m/s", Eclipse.client.player.getVelocity().length() * 20));

        PlayerListEntry entry = Eclipse.client.getNetworkHandler().getPlayerListEntry(Eclipse.client.player.getUuid());
        if(entry != null && (boolean) this.config.get("ShowPing"))
            right.add(String.format("Latency: %d ms", entry.getLatency()));

        if((boolean) this.config.get("ShowTps"))
            right.add(String.format("%.2f tps", this.tps));

        int y = padding;
        for(String str : left) {
            int width = textRenderer.getWidth(str);

            context.fill(
                    0 + padding,
                    y,
                    0 + padding + padding + width + padding,
                    y + padding + textRenderer.fontHeight,
                    0x99000000
            );

            context.drawText(
                    textRenderer,
                    str,
                    0 + padding + padding,
                    y + padding,
                    0xFFAA00,
                    false
            );

            y += padding + textRenderer.fontHeight;
        }

        y = padding;
        int sWidth = context.getScaledWindowWidth();
        for(String str : right) {
            int width = textRenderer.getWidth(str);

            context.fill(
                    sWidth - padding - padding - width - padding,
                    y,
                    sWidth - padding,
                    y + padding + textRenderer.fontHeight,
                    0x99000000
            );

            context.drawText(
                    textRenderer,
                    str,
                    sWidth - padding - padding - width,
                    y + padding,
                    0xFFAA00,
                    false
            );

            y += padding + textRenderer.fontHeight;
        }

        int sHeight = context.getScaledWindowHeight();
        long milliDiff = System.currentTimeMillis() - this.lastPacketTime;
        if(milliDiff > 3000) {
            String text = String.format("Server not responding! %dms", milliDiff);
            int textWidth = textRenderer.getWidth(text);

            context.fill(
                    sWidth / 2 - 2 * padding - textWidth / 2,
                    sHeight - padding - 70,
                    sWidth / 2 + 2 * padding + textWidth / 2,
                    sHeight - 70 + padding + textRenderer.fontHeight,
                    0xCC000000
            );

            context.drawBorder(
                    sWidth / 2 - 2 * padding - textWidth / 2,
                    sHeight - padding - 70,
                    2 * padding + textWidth + 2 * padding,
                    padding + textRenderer.fontHeight + padding,
                    0xFFFFAA00
            );

            context.drawCenteredTextWithShadow(
                    textRenderer,
                    text,
                    sWidth / 2,
                    sHeight - 70,
                    0xFFAA00
            );
        }
    }
}
