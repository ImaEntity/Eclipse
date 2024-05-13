package com.entity.eclipse.modules.misc;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.packet.PacketEvents;
import com.entity.eclipse.utils.events.render.RenderEvent;
import com.entity.eclipse.utils.types.DynamicValue;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;

public class Test extends Module {
    private HashMap<String, DynamicValue<?>> data = new HashMap<>();

    public Test() {
        super("Test", "idfk", ModuleType.MISC);

        Events.Packet.register(PacketEvents.SEND, event -> {

        });

        Events.Packet.register(PacketEvents.RECEIVE, event -> {

        });
    }

    @Override
    public void tick() {
        if(Eclipse.client.player == null) return;

        Eclipse.client.options.sprintKey.setPressed(false);

        if(!Eclipse.client.options.forwardKey.isPressed()) return;
        if(Eclipse.client.options.backKey.isPressed()) return;
        if(Eclipse.client.player.horizontalCollision) return;

        Eclipse.client.options.sprintKey.setPressed(true);
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
