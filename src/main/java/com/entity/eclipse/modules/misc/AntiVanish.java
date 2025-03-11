package com.entity.eclipse.modules.misc;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.chat.ChatEvents;
import com.entity.eclipse.utils.events.packet.PacketEvents;
import com.entity.eclipse.utils.events.render.Render2DEvent;
import com.entity.eclipse.utils.events.render.Render3DEvent;
import com.entity.eclipse.utils.types.EnumValue;
import com.entity.eclipse.utils.types.IntegerValue;
import com.entity.eclipse.utils.types.StringValue;
import com.mojang.brigadier.suggestion.Suggestion;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;

import java.util.*;
import java.util.stream.Collectors;

public class AntiVanish extends Module {
    private final ArrayList<Integer> completionIDs = new ArrayList<>();
    private List<String> usernameCache = new ArrayList<>();

    private final ArrayList<String> messageList = new ArrayList<>();
    private final Map<UUID, String> vanishMap = new HashMap<>();
    private Map<UUID, String> playerMap = new HashMap<>();

    private int timer = 0;

    private enum Mode {
        LeaveMessage,
        CommandCompletion
    }

    public AntiVanish() {
        super("AntiVanish", "Notifies you when players enter or exit vanish.", ModuleType.MISC);

        this.config.create("Mode", new EnumValue<>(Mode.LeaveMessage));
        this.config.create("CheckInterval", new IntegerValue(60));
        this.config.create("Command", new StringValue("minecraft:msg"))
                .visibleIf(() -> this.config.get("Mode") == Mode.CommandCompletion);

        Events.Chat.register(ChatEvents.RECEIVE, event -> {
            this.messageList.add(event.getMessage().getString());
        });

        Events.Packet.register(PacketEvents.RECEIVE, event -> {
            if(Eclipse.client.player == null) return;
            if(!this.isEnabled()) return;

            if(!(event.getPacket() instanceof CommandSuggestionsS2CPacket packet)) return;
            if(this.config.get("Mode") != Mode.CommandCompletion) return;

            if(!this.completionIDs.contains(packet.id())) return;

            List<String> prevUsernames = this.usernameCache.stream().toList();
            this.usernameCache = packet.getSuggestions().getList().stream()
                    .map(Suggestion::getText)
                    .toList();

            if(prevUsernames.isEmpty()) return;

            for(String username : prevUsernames) {
                if(Eclipse.client.player.getName().getString().equals(username)) continue;

                if(username.contains(" ")) continue;
                if(username.length() < 3 || username.length() > 16) continue;

                if(!this.usernameCache.contains(username)) continue;
                this.notifyUser(String.format("%s left the game", username));
            }

            for(String username : this.usernameCache) {
                if(Eclipse.client.player.getName().getString().equals(username)) continue;

                if(username.contains(" ")) continue;
                if(username.length() < 3 || username.length() > 16) continue;

                if(!prevUsernames.contains(username)) continue;
                this.notifyUser(String.format("%s joined the game", username));
            }

            this.completionIDs.remove(packet.id());
            event.setCancelled(true);
        });
    }

    private void checkLeaveMessage() {
        if(Eclipse.client.getNetworkHandler() == null) return;
        if(Eclipse.client.player == null) return;

        Map<UUID, String> prevPlayerMap = Map.copyOf(this.playerMap);
        this.playerMap = Eclipse.client.getNetworkHandler()
                .getPlayerList()
                .stream()
                .collect(Collectors.toMap(
                        playerEntry -> playerEntry.getProfile().getId(),
                        playerEntry -> playerEntry.getProfile().getName()
                ));

        for(UUID uuid : prevPlayerMap.keySet()) {
            if(Eclipse.client.player.getUuid().equals(uuid)) continue;
            if(this.playerMap.containsKey(uuid)) continue;

            String username = prevPlayerMap.get(uuid);

            if(username.contains(" ")) continue;
            if(username.length() < 3 || username.length() > 16) continue;

            if(this.messageList.stream().anyMatch(str -> str.contains(username)))
                continue;

            this.vanishMap.put(uuid, username);
            this.notifyUser(String.format("%s entered vanish", username));
        }

        // Prevent ConcurrentModificationException
        for(UUID uuid : this.vanishMap.keySet().toArray(new UUID[0])) {
            if(Eclipse.client.player.getUuid().equals(uuid)) continue;
            if(prevPlayerMap.containsKey(uuid)) continue;
            if(!this.playerMap.containsKey(uuid)) continue;

            String username = this.vanishMap.get(uuid);

            if(username.contains(" ")) continue;
            if(username.length() < 3 || username.length() > 16) continue;

            if(this.messageList.stream().anyMatch(str -> str.contains(username)))
                continue;

            this.vanishMap.remove(uuid);
            this.notifyUser(String.format("%s exited vanish", username));
        }

        this.messageList.clear();
    }

    @Override
    public void tick() {
        if(Eclipse.client.getNetworkHandler() == null) return;
        if(Eclipse.client.world == null) {
            this.vanishMap.clear();
            return;
        }

        this.timer++;
        if(this.timer < (int) this.config.get("CheckInterval"))
            return;

        switch((Mode) this.config.get("Mode")) {
            case LeaveMessage -> checkLeaveMessage();
            case CommandCompletion -> {
                int id = Eclipse.client.world.random.nextInt();
                this.completionIDs.add(id);

                Eclipse.client.getNetworkHandler().sendPacket(new RequestCommandCompletionsC2SPacket(
                        id,
                        String.format("%s ", this.config.get("Command"))
                ));
            }
        }

        this.timer = 0;
    }

    @Override
    public void onEnable() {
        this.playerMap.clear();
        this.vanishMap.clear();
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
