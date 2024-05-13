package com.entity.eclipse.modules.combat;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.RenderEvent;
import com.entity.eclipse.utils.types.FloatValue;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.text.Text;

public class AutoLog extends Module {
    public AutoLog() {
        super("AutoLog", "Automatically disconnects at or below a certain health.", ModuleType.COMBAT);

        this.config.create("HealthThreshold", new FloatValue(15f));
    }

    @Override
    public void tick() {
        if(Eclipse.client.player == null) return;
        if(Eclipse.client.getNetworkHandler() == null) return;

        float health = Eclipse.client.player.getHealth();
        if(health > (float) this.config.get("HealthThreshold")) return;

        Eclipse.client.getNetworkHandler().onDisconnect(new DisconnectS2CPacket(Text.of(
                "[AutoLog] Health fell below " +
                        this.config.getRaw("HealthThreshold").toString()
        )));

        ModuleManager.queueDisable(this);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void renderWorld(RenderEvent event) {

    }

    @Override
    public void renderScreen(RenderEvent event) {

    }
}
