package com.entity.eclipse.modules.misc;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.packet.PacketEvents;
import com.entity.eclipse.utils.events.render.Render2DEvent;
import com.entity.eclipse.utils.events.render.Render3DEvent;
import com.entity.eclipse.utils.types.BooleanValue;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;

public class GamemodeNotifier extends Module {
    public GamemodeNotifier() {
        super("GamemodeNotifier", "Notifies you when player change their gamemode.", ModuleType.MISC);

        this.config.create("ExcludeSelf", new BooleanValue(true));

        Events.Packet.register(PacketEvents.RECEIVE, event -> {
            if(!this.isEnabled()) return;
            if(Eclipse.client.player == null) return;
            if(Eclipse.client.getNetworkHandler() == null) return;

            if(!(event.getPacket() instanceof PlayerListS2CPacket packet)) return;
            if(!packet.getActions().contains(PlayerListS2CPacket.Action.UPDATE_GAME_MODE)) return;

            for(PlayerListS2CPacket.Entry newEntry : packet.getEntries()) {
                PlayerListEntry oldEntry = Eclipse.client.getNetworkHandler().getPlayerListEntry(newEntry.profileId());
                if(oldEntry == null) continue;

                boolean isSelf = Eclipse.client.player.getUuid() == oldEntry.getProfile().getId();
                if((boolean) this.config.get("ExcludeSelf") && isSelf) continue;

                if(oldEntry.getGameMode() == newEntry.gameMode()) continue;
                this.notifyUser(String.format("%s switched to %s", oldEntry.getProfile().getName(), newEntry.gameMode().asString()));
            }
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
