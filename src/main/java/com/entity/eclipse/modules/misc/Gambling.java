package com.entity.eclipse.modules.misc;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.RenderEvent;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class Gambling extends Module {
    public Gambling() {
        super("Gambling", "Why would you use this?", ModuleType.MISC);
    }

    @Override
    public void tick() {
        if(Eclipse.client.getNetworkHandler() == null) return;

        if(Math.random() > 0.0001)
            return;

        Eclipse.client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                Double.NaN,
                Double.NaN,
                Double.NaN,
                false
        ));
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
