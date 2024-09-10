package com.entity.eclipse.utils.scripting.events;

import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.block.BlockEvents;
import com.entity.eclipse.utils.events.lore.LoreEvents;
import com.entity.eclipse.utils.events.packet.PacketEvents;
import com.entity.eclipse.utils.events.render.RenderEvents;
import com.entity.eclipse.utils.events.tick.TickEvents;

public class EventsWrapper {
    public final PacketWrapper Packet = new PacketWrapper();
    public final TickWrapper Tick = new TickWrapper();
    public final RenderWrapper Render = new RenderWrapper();
    public final LoreWrapper Lore = new LoreWrapper();
    public final BlockWrapper Block = new BlockWrapper();

    public static class PacketWrapper {
        public void register(PacketEvents event, Events.Packet.Handler handler) {
            Events.Packet.register(event, handler);
        }
    }

    public static class TickWrapper {
        public void register(TickEvents event, Events.Tick.Handler handler) {
            Events.Tick.register(event, handler);
        }
    }

    public static class RenderWrapper {
        public void register(RenderEvents event, Events.Render.Handler handler) {
            Events.Render.register(event, handler);
        }
    }

    public static class LoreWrapper {
        public void register(LoreEvents event, Events.Lore.Handler handler) {
            Events.Lore.register(event, handler);
        }
    }

    public static class BlockWrapper {
        public void register(BlockEvents event, Events.Block.Handler handler) {
            Events.Block.register(event, handler);
        }
    }
}
