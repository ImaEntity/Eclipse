package com.entity.eclipse.modules.combat;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.modules.movement.NoFall;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.packet.PacketEvents;
import com.entity.eclipse.utils.events.render.RenderEvent;
import com.entity.eclipse.utils.types.DoubleValue;
import net.minecraft.item.MaceItem;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class MacePlus extends Module {
    public MacePlus() {
        super("MacePlus", "Makes maces do more damage.", ModuleType.COMBAT);

        this.config.create("DropDistance", new DoubleValue(3.0));

        Events.Packet.register(PacketEvents.SEND, event -> {
            if(Eclipse.client.player == null) return;
            if(Eclipse.client.getNetworkHandler() == null) return;

            if(!(event.getPacket() instanceof PlayerInteractEntityC2SPacket)) return;
            if(!this.isEnabled()) return;

            if(!(Eclipse.client.player.getMainHandStack().getItem() instanceof MaceItem)) return;

            Module noFall = ModuleManager.getByClass(NoFall.class);
            if(noFall == null) return; // If this ever gets run, something is very broken

            ModuleManager.tempDisable(noFall);

            Vec3d pos = Eclipse.client.player.getPos();

            Eclipse.client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                    pos.getX(),
                    pos.getY() + (double) this.config.get("DropDistance"),
                    pos.getZ(),
                    false
            ));

            Eclipse.client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    false
            ));

            ModuleManager.revertTemp(noFall);
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
    public void renderWorld(RenderEvent event) {

    }

    @Override
    public void renderScreen(RenderEvent event) {

    }
}
