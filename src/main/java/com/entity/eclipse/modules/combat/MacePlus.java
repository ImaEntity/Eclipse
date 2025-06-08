package com.entity.eclipse.modules.combat;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.modules.movement.NoFall;
import com.entity.eclipse.modules.player.AntiHunger;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.packet.PacketEvents;
import com.entity.eclipse.utils.events.render.Render2DEvent;
import com.entity.eclipse.utils.events.render.Render3DEvent;
import com.entity.eclipse.utils.types.DoubleValue;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class MacePlus extends Module {
    public MacePlus() {
        super("MacePlus", "turns your mace into a one shot wonder", ModuleType.COMBAT);

        this.config.create("DropDistance", new DoubleValue(3.0));

        Events.Packet.register(PacketEvents.SEND, event -> {
            if(Eclipse.client.player == null) return;
            if(Eclipse.client.getNetworkHandler() == null) return;

            if(!(event.getPacket() instanceof PlayerInteractEntityC2SPacket)) return;
            if(!this.isEnabled()) return;

            if(!Eclipse.client.player.getMainHandStack().isOf(Items.MACE)) return;

            Module noFall = ModuleManager.getByClass(NoFall.class);
            if(noFall == null) return; // If this ever gets run, something is very broken

            Module antiHunger = ModuleManager.getByClass(AntiHunger.class);
            if(antiHunger == null) return;

            ModuleManager.tempDisable(noFall);
            ModuleManager.tempDisable(antiHunger);

            Vec3d pos = Eclipse.client.player.getPos();
            int grounds = (int) Math.ceil(Math.abs(
                    (double) this.config.get("DropDistance")
            ) / 10);

            for(int i = 0; i < grounds - 1; i++)
                Eclipse.client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(false, Eclipse.client.player.horizontalCollision));

            Eclipse.client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                    pos.getX(),
                    pos.getY() + (double) this.config.get("DropDistance"),
                    pos.getZ(),
                    false,
                    Eclipse.client.player.horizontalCollision
            ));

            for(int i = 0; i < grounds - 1; i++)
                Eclipse.client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(false, Eclipse.client.player.horizontalCollision));

            Eclipse.client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    false,
                    Eclipse.client.player.horizontalCollision
            ));

            ModuleManager.revertTemp(antiHunger);
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
    public void renderWorld(Render3DEvent event) {

    }

    @Override
    public void renderScreen(Render2DEvent event) {

    }
}
