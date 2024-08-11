package com.entity.eclipse.modules.misc;

import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.packet.PacketEvents;
import com.entity.eclipse.utils.events.render.RenderEvent;
import com.entity.eclipse.utils.types.DynamicValue;

import java.util.HashMap;

public class Test extends Module {
    private HashMap<String, DynamicValue<?>> data = new HashMap<>();

    public Test() {
        super("Test", "idfk", ModuleType.MISC);

        Events.Packet.register(PacketEvents.SEND, event -> {
            if(!this.isEnabled()) return;
        });

        Events.Packet.register(PacketEvents.RECEIVE, event -> {
            if(!this.isEnabled()) return;
        });
    }

    @Override
    public void tick() {
//        if(!Ticker.isQueueEmpty()) return;
//
//        CommandManager
//                .getByClass(com.entity.eclipse.commands.Test.class)
//                .onExecute(new String[0]);
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
