package com.entity.eclipse.modules.misc;

import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.packet.PacketEvents;
import com.entity.eclipse.utils.events.render.Render2DEvent;
import com.entity.eclipse.utils.events.render.Render3DEvent;

import java.util.HashMap;

public class Test extends Module {
    private HashMap<String, Object> data = new HashMap<>();

    public Test() {
        super("Test", "makes my life easier", ModuleType.MISC);

        Events.Packet.register(PacketEvents.SEND, event -> {
            if(!this.isEnabled()) return;
        });

        Events.Packet.register(PacketEvents.RECEIVE, event -> {
            if(!this.isEnabled()) return;
        });
    }

    @Override
    public void tick() {

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
