package com.entity.eclipse.modules.misc;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.RunnableClickEvent;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.packet.PacketEvents;
import com.entity.eclipse.utils.events.render.Render2DEvent;
import com.entity.eclipse.utils.events.render.Render3DEvent;
import net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket;
import net.minecraft.network.packet.s2c.common.ResourcePackSendS2CPacket;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.net.URI;
import java.net.URL;

public class ResourceSpoof extends Module {
    // Reset by some random mixin
    public boolean cancelStatusPackets = false;

    public ResourceSpoof() {
        super("ResourceSpoof", "Tells the server you accepted their resource packs.", ModuleType.MISC);

        Events.Packet.register(PacketEvents.SEND, event -> {
            if(!this.isEnabled()) return;

            if(!this.cancelStatusPackets) return;
            if(!(event.getPacket() instanceof ResourcePackStatusC2SPacket)) return;

            event.setCancelled(true);
        });

        Events.Packet.register(PacketEvents.RECEIVE, event -> {
            if(!this.isEnabled()) return;
            if(Eclipse.client.getNetworkHandler() == null) return;

            if(!(event.getPacket() instanceof ResourcePackSendS2CPacket packet)) return;

            MutableText acceptPrompt = Text.literal("[Accept Pack]").setStyle(
                    Style.EMPTY
                            .withColor(Formatting.GOLD)
                            .withUnderline(true)
                            .withHoverEvent(new HoverEvent.ShowText(Text.literal("Accept pack?")))
                            .withClickEvent(new RunnableClickEvent(() -> {
                                URL url = null;
                                try { url = new URI(packet.url()).toURL(); } catch(Exception ignored) {}

                                if(url == null)
                                    Eclipse.notifyUser("Invalid url!");

                                this.cancelStatusPackets = true;
                                Eclipse.client.getServerResourcePackProvider().addResourcePack(packet.id(), url, packet.hash());
                                Eclipse.client.getServerResourcePackProvider().acceptAll();

                                Eclipse.notifyUser("Accepted resource pack.");
                            }))
            );

            MutableText msg = Text.literal("This server has ")
                    .append(packet.required() ? "a required" : "an optional")
                    .append(" resource pack. ")
                    .append(acceptPrompt);

            this.notifyUserRaw(msg);
            event.setCancelled(true);

            Eclipse.client.getNetworkHandler().sendPacket(new ResourcePackStatusC2SPacket(packet.id(), ResourcePackStatusC2SPacket.Status.ACCEPTED));
            Eclipse.client.getNetworkHandler().sendPacket(new ResourcePackStatusC2SPacket(packet.id(), ResourcePackStatusC2SPacket.Status.DOWNLOADED));
            Eclipse.client.getNetworkHandler().sendPacket(new ResourcePackStatusC2SPacket(packet.id(), ResourcePackStatusC2SPacket.Status.SUCCESSFULLY_LOADED));
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
