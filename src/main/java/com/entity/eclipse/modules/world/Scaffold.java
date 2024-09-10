package com.entity.eclipse.modules.world;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.Slots;
import com.entity.eclipse.utils.events.render.RenderEvent;
import com.entity.eclipse.utils.types.BooleanValue;
import com.entity.eclipse.utils.types.ItemValue;
import com.entity.eclipse.utils.types.ListValue;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class Scaffold extends Module {
    public Scaffold() {
        super("Scaffold", "Automatically places blocks beneath you.", ModuleType.WORLD);

        this.config.create("AllowInventory", new BooleanValue(true));
        this.config.create("AllowedBlocks", new ListValue(
                ItemValue.class,
                "dirt",
                "cobbled_deepslate",
                "cobblestone",
                "netherrack",
                "end_stone",
                "obsidian"
        ));
    }

    @Override
    public void tick() {
        if(Eclipse.client.player == null) return;
        if(Eclipse.client.interactionManager == null) return;
        if(Eclipse.client.world == null) return;

        ItemStack offStack = Eclipse.client.player.getOffHandStack();

        boolean isBlock = offStack.getItem() instanceof BlockItem;
        boolean isEmpty = offStack.isEmpty();

        if(!isBlock && !isEmpty)
            return;

        if(!isBlock) {
            int endSlot = this.config.get("AllowInventory") ?
                    Slots.MAIN.end() :
                    Slots.HOTBAR.end();

            int slot = Slots.findFirst(
                    new Slots.Range(Slots.HOTBAR.start(), endSlot),
                    stack -> slotIdx ->
                            ((ListValue) this.config.getRaw("AllowedBlocks")).contains(stack.getItem())
            );

            if(slot == Slots.INVALID_SLOT)
                return;

            Slots.swap(
                    Slots.indexToID(slot),
                    Slots.indexToID(Slots.OFFHAND)
            );
        }

        Eclipse.client.player.setPitch(0);
        Direction facing = Direction.getFacing(Eclipse.client.player.getRotationVec(0));
        Eclipse.client.player.setPitch(Eclipse.client.player.prevPitch);

        int minX = -Math.abs(facing.getOffsetX());
        int maxX =  Math.abs(facing.getOffsetX());
        int minZ = -Math.abs(facing.getOffsetZ());
        int maxZ =  Math.abs(facing.getOffsetZ());

        for(int x = minX; x <= maxX; x++) {
            for(int z = minZ; z <= maxZ; z++) {
                BlockPos pos = Eclipse.client.player.getBlockPos()
                        .add(x, -1, z);

                Block block = Eclipse.client.world.getBlockState(pos).getBlock();

                boolean isAir = block instanceof AirBlock;
                boolean isLiquid = block == Blocks.WATER || block == Blocks.LAVA;
                boolean isSolid = block.getDefaultState().isSolidBlock(null, pos);

                if(!isAir && !isLiquid && isSolid) continue;

                Eclipse.client.interactionManager.interactBlock(
                        Eclipse.client.player,
                        Hand.OFF_HAND,
                        new BlockHitResult(
                                Vec3d.of(pos),
                                Direction.UP,
                                pos,
                                true
                        )
                );
            }
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
