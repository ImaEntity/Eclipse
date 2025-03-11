package com.entity.eclipse.modules.misc;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.Render2DEvent;
import com.entity.eclipse.utils.events.render.Render3DEvent;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class Gambling extends Module {
    public Gambling() {
        super("Gambling", "Has a 0.01% to kick you from the server each tick.", ModuleType.MISC);
    }

    @Override
    public void tick() {
        if(Eclipse.client.getNetworkHandler() == null) return;
        if(Eclipse.client.player == null) return;

        if(Math.random() > 0.0001)
            return;

        Eclipse.client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                Double.NaN,
                Double.NaN,
                Double.NaN,
                false,
                Eclipse.client.player.horizontalCollision
        ));
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
