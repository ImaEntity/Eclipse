package com.entity.eclipse.modules.misc;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.packet.PacketEvents;
import com.entity.eclipse.utils.events.render.RenderEvent;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;

public class Test extends Module {
    private HashMap<String, Object> data = new HashMap<>();

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
        if(Eclipse.client.world == null) return;
        if(Eclipse.client.player == null) return;
        if(Eclipse.client.interactionManager == null) return;


//        Iterable<Entity> entities = Eclipse.client.world.getEntities();
//        for(Entity e : entities) {
//            if(e.getType() != EntityType.PLAYER) continue;
//            if(Eclipse.client.player.distanceTo(e) > 50f) continue;
//            if(Eclipse.client.player == e) continue;
//
//            Eclipse.notifyUser(e.getName().getString() + " is near you!");
//        }

        int x = Eclipse.client.player.getBlockX();
        int z = Eclipse.client.player.getBlockZ();
        int minY = Eclipse.client.world.getBottomY();
        int maxY = Eclipse.client.world.getTopY();

        for(int y = minY; y <= maxY; y++) {
            BlockPos pos = new BlockPos(x, y, z);
            Block block = Eclipse.client.world.getBlockState(pos).getBlock();

            if(block != Blocks.SCULK) continue;

            Eclipse.notifyUser(
                    "Found ancient city at y=" + y,
                    true
            );
        }
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
