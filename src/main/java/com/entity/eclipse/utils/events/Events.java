package com.entity.eclipse.utils.events;

import com.entity.eclipse.utils.events.block.BlockEvent;
import com.entity.eclipse.utils.events.block.BlockEvents;
import com.entity.eclipse.utils.events.chat.ChatEvent;
import com.entity.eclipse.utils.events.chat.ChatEvents;
import com.entity.eclipse.utils.events.lore.LoreEvent;
import com.entity.eclipse.utils.events.lore.LoreEvents;
import com.entity.eclipse.utils.events.packet.PacketEvent;
import com.entity.eclipse.utils.events.packet.PacketEvents;
import com.entity.eclipse.utils.events.render.Render2DEvent;
import com.entity.eclipse.utils.events.tick.TickEvent;
import com.entity.eclipse.utils.events.tick.TickEvents;

import java.util.ArrayList;
import java.util.HashMap;

public class Events {
    public static class Packet {
        private static final HashMap<PacketEvents, ArrayList<Handler>> handlers = new HashMap<>();

        public static void register(PacketEvents event, Handler handler) {
            if(!handlers.containsKey(event)) handlers.put(event, new ArrayList<>());
            handlers.get(event).add(handler);
        }

        public static boolean fireEvent(PacketEvents event, PacketEvent argument) {
            if(!handlers.containsKey(event)) return false;

            for(Handler handler : handlers.get(event))
                handler.onFired(argument);

            return argument.isCancelled();
        }

        @FunctionalInterface
        public interface Handler {
            void onFired(PacketEvent event);
        }
    }

    public static class Tick {
        private static final HashMap<TickEvents, ArrayList<Handler>> handlers = new HashMap<>();

        public static void register(TickEvents event, Handler handler) {
            if(!handlers.containsKey(event)) handlers.put(event, new ArrayList<>());
            handlers.get(event).add(handler);
        }

        public static boolean fireEvent(TickEvents event, TickEvent argument) {
            if(!handlers.containsKey(event)) return false;

            for(Handler handler : handlers.get(event))
                handler.onFired(argument);

            return argument.isCancelled();
        }

        @FunctionalInterface
        public interface Handler {
            void onFired(TickEvent event);
        }
    }

    public static class Chat {
        private static final HashMap<ChatEvents, ArrayList<Handler>> handlers = new HashMap<>();

        public static void register(ChatEvents event, Handler handler) {
            if(!handlers.containsKey(event)) handlers.put(event, new ArrayList<>());
            handlers.get(event).add(handler);
        }

        public static boolean fireEvent(ChatEvents event, ChatEvent argument) {
            if(!handlers.containsKey(event)) return false;

            for(Handler handler : handlers.get(event))
                handler.onFired(argument);

            return argument.isCancelled();
        }

        @FunctionalInterface
        public interface Handler {
            void onFired(ChatEvent event);
        }
    }

    public static class Render2D {
        private static final ArrayList<Handler> handlers = new ArrayList<>();

        public static void register(Handler handler) {
            handlers.add(handler);
        }
        public static boolean fireEvent(Render2DEvent argument) {
            for(Handler handler : handlers)
                handler.onFired(argument);

            return argument.isCancelled();
        }

        @FunctionalInterface
        public interface Handler {
            void onFired(Render2DEvent event);
        }
    }

    public static class Lore {
        private static final HashMap<LoreEvents, ArrayList<Handler>> handlers = new HashMap<>();

        public static void register(LoreEvents event, Handler handler) {
            if(!handlers.containsKey(event)) handlers.put(event, new ArrayList<>());
            handlers.get(event).add(handler);
        }

        public static boolean fireEvent(LoreEvents event, LoreEvent argument) {
            if(!handlers.containsKey(event)) return false;

            for(Handler handler : handlers.get(event))
                handler.onFired(argument);

            return argument.isCancelled();
        }

        @FunctionalInterface
        public interface Handler {
            void onFired(LoreEvent event);
        }
    }

    public static class Block {
        private static final HashMap<BlockEvents, ArrayList<Handler>> handlers = new HashMap<>();

        public static void register(BlockEvents event, Handler handler) {
            if(!handlers.containsKey(event)) handlers.put(event, new ArrayList<>());
            handlers.get(event).add(handler);
        }

        public static boolean fireEvent(BlockEvents event, BlockEvent argument) {
            if(!handlers.containsKey(event)) return false;

            for(Handler handler : handlers.get(event))
                handler.onFired(argument);

            return argument.isCancelled();
        }

        @FunctionalInterface
        public interface Handler {
            void onFired(BlockEvent event);
        }
    }
}
