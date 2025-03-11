package com.entity.eclipse.modules.world;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.HitResultBuilders;
import com.entity.eclipse.utils.Slots;
import com.entity.eclipse.utils.events.render.Render2DEvent;
import com.entity.eclipse.utils.events.render.Render3DEvent;
import com.entity.eclipse.utils.types.BooleanValue;
import com.entity.eclipse.utils.types.DoubleValue;
import net.minecraft.block.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.HashMap;

public class AutoFarm extends Module {
    public HashMap<Block, Item> seedMap = new HashMap<>();
    public HashMap<BlockPos, Item> blockSeedCache = new HashMap<>();

    public AutoFarm() {
        super("AutoFarm", "Automatically harvests and replants crops.", ModuleType.WORLD);

        this.config.create("Range", new DoubleValue(4.0));
        this.config.create("TillSoil", new BooleanValue(false));
        this.config.create("AllowInventory", new BooleanValue(true));
        this.config.create("Replant", new BooleanValue(true));
        this.config.create("Harvest", new BooleanValue(true));
        this.config.create("RememberSeedTypes", new BooleanValue(false));

        this.seedMap.put(Blocks.WHEAT, Items.WHEAT_SEEDS);
        this.seedMap.put(Blocks.CARROTS, Items.CARROT);
        this.seedMap.put(Blocks.POTATOES, Items.POTATO);
        this.seedMap.put(Blocks.BEETROOTS, Items.BEETROOT_SEEDS);
    }

    @Override
    public void tick() {
        if(Eclipse.client.player == null) return;
        if(Eclipse.client.world == null) return;
        if(Eclipse.client.interactionManager == null) return;

        double range = this.config.get("Range");

        for(int yOff = -3; yOff <= 3; yOff++) {
            for(int zOff = (int) -Math.ceil(range); zOff <= Math.ceil(range); zOff++) {
                for(int xOff = (int) -Math.ceil(range); xOff <= Math.ceil(range); xOff++) {
                    int dist = xOff * xOff + zOff * zOff;
                    if(dist > range * range) continue;

                    BlockPos pos = Eclipse.client.player.getBlockPos()
                            .add(xOff, yOff, zOff);

                    BlockState state = Eclipse.client.world.getBlockState(pos);

                    boolean isFarmland = state.getBlock() == Blocks.FARMLAND;
                    boolean isCrop = state.getBlock() instanceof CropBlock;
                    boolean isTillable =
                            state.getBlock() == Blocks.DIRT ||
                            state.getBlock() == Blocks.GRASS_BLOCK ||
                            state.getBlock() == Blocks.DIRT_PATH;

                    if(!isFarmland && !isCrop && !isTillable) continue;

                    if(isFarmland && (boolean) this.config.get("Replant")) {
                        BlockState potentialPlant = Eclipse.client.world.getBlockState(pos.up());
                        if(!(potentialPlant.getBlock() instanceof AirBlock))
                             continue;

                        int prevSlot = Eclipse.client.player.getInventory().selectedSlot;
                        int endSlot = (boolean) this.config.get("AllowInventory") ?
                                Slots.MAIN.end() :
                                Slots.HOTBAR.end();

                        int slot = Slots.findFirst(
                                new Slots.Range(Slots.HOTBAR.start(), endSlot),
                                stack -> slotIdx -> {
                                    if((boolean) this.config.get("RememberSeedTypes")) {
                                        if(this.blockSeedCache.get(pos) != stack.getItem())
                                            return false;
                                    }

                                    return this.seedMap.containsValue(stack.getItem());
                                }
                        );

                        if(slot == Slots.INVALID_SLOT)
                            continue;

                        if(!Slots.HOTBAR.contains(slot)) {
                            Slots.swap(
                                    Slots.indexToID(slot),
                                    Slots.indexToID(Slots.OFFHAND)
                            );
                        } else {
                            Eclipse.client.player.getInventory().selectedSlot = slot;
                        }

                        Eclipse.client.interactionManager.interactBlock(
                                Eclipse.client.player,
                                Slots.HOTBAR.contains(slot) ? Hand.MAIN_HAND : Hand.OFF_HAND,
                                HitResultBuilders.createBlock(pos, Direction.UP)
                        );

                        Eclipse.client.player.getInventory().selectedSlot = prevSlot;
                        if(!Slots.HOTBAR.contains(slot)) {
                            Slots.swap(
                                    Slots.indexToID(slot),
                                    Slots.indexToID(prevSlot)
                            );
                        }
                    }

                    if(isCrop && (boolean) this.config.get("Harvest")) {
                        CropBlock block = (CropBlock) state.getBlock();
                        Item seedItem = this.seedMap.get(block);
                        if(!block.isMature(state)) continue;

                        if((boolean) this.config.get("RememberSeedTypes") && seedItem != null)
                            this.blockSeedCache.put(pos, seedItem);

                        Eclipse.client.interactionManager.attackBlock(
                                pos,
                                Direction.UP
                        );
                    }

                    if(isTillable && (boolean) this.config.get("TillSoil")) {
                        int endSlot = (boolean) this.config.get("AllowInventory") ?
                                Slots.MAIN.end() :
                                Slots.HOTBAR.end();

                        int prevSlot = Eclipse.client.player.getInventory().selectedSlot;
                        int slot = Slots.findFirst(
                                new Slots.Range(Slots.HOTBAR.start(), endSlot),
                                stack -> slotIdx -> stack.isIn(ItemTags.HOES)
                        );

                        if(!Slots.HOTBAR.contains(slot)) {
                            Slots.swap(
                                    Slots.indexToID(slot),
                                    Slots.indexToID(prevSlot)
                            );
                        } else {
                            Eclipse.client.player.getInventory().selectedSlot = slot;
                        }

                        Eclipse.client.interactionManager.interactBlock(
                                Eclipse.client.player,
                                Hand.MAIN_HAND,
                                HitResultBuilders.createBlock(pos, Direction.UP)
                        );

                        Eclipse.client.player.getInventory().selectedSlot = prevSlot;
                        if(!Slots.HOTBAR.contains(slot)) {
                            Slots.swap(
                                    Slots.indexToID(slot),
                                    Slots.indexToID(prevSlot)
                            );
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onEnable() {
        this.blockSeedCache.clear();
    }

    @Override
    public void onDisable() {
        this.blockSeedCache.clear();
    }

    @Override
    public void renderWorld(Render3DEvent event) {

    }

    @Override
    public void renderScreen(Render2DEvent event) {

    }
}
