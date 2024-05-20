package com.entity.eclipse.modules.combat;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.Slots;
import com.entity.eclipse.utils.events.render.RenderEvent;
import com.entity.eclipse.utils.types.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class CrystalAura extends Module {
    public CrystalAura() {
        super("CrystalAura", "*boom*", ModuleType.COMBAT);

        this.config.create("CrystalAttackRange", new FloatValue(3.5f));
        this.config.create("CrystalTargetRange", new FloatValue(3.5f));
        this.config.create("AllowInventory", new BooleanValue(false));
        this.config.create("TryProtect", new BooleanValue(true));
        this.config.create("JunkBlocks", new ListValue(
                ItemValue.class,
                "dirt",
                "cobblestone",
                "netherrack"
        ));

        this.config.create("Targets", new ListValue(
                EntityTypeValue.class,
                "player"
        ));
    }

    private ArrayList<EndCrystalEntity> findCrystals() {
        if(Eclipse.client.player == null) return new ArrayList<>();
        if(Eclipse.client.world == null) return new ArrayList<>();

        ArrayList<EndCrystalEntity> crystalsInRange = new ArrayList<>();

        for(Entity entity : Eclipse.client.world.getEntities()) {
            if(entity == Eclipse.client.player) continue;
            if(!(entity instanceof EndCrystalEntity crystal)) continue;

            if(crystal.distanceTo(Eclipse.client.player) <= (float) this.config.get("CrystalAttackRange"))
                crystalsInRange.add(crystal);
        }

        return crystalsInRange;
    }

    private ArrayList<Entity> findTargets(ArrayList<EndCrystalEntity> crystals) {
        return new ArrayList<>();
    }

    private ArrayList<BlockPos> findPlaceableBlocks() {
        return new ArrayList<>();
    }

    private void placeCrystals(ArrayList<BlockPos> placeableBlocks) {

    }

    private void tryProtect() {
        if(!(boolean) this.config.get("TryProtect")) return;

        int startSlot = this.config.get("AllowInventory") ?
                Slots.MAIN.start() :
                Slots.HOTBAR.start();

        int blockSlot = Slots.findFirst(
                new Slots.Range(startSlot, Slots.MAIN.end()),
                item -> ((ListValue) this.config.getRaw("JunkBlocks")).contains(item)
        );

        Slots.swap(Slots.indexToID(blockSlot), Slots.getSelectedID());

        // TODO: Place blocks towards crystals
    }

    private void attackCrystals(ArrayList<EndCrystalEntity> crystals) {
        if(Eclipse.client.player == null) return;
        if(Eclipse.client.interactionManager == null) return;

        for(EndCrystalEntity crystal : crystals)
            Eclipse.client.interactionManager.attackEntity(Eclipse.client.player, crystal);
    }

    @Override
    public void tick() {
        if(Eclipse.client.player == null) return;
        if(Eclipse.client.world == null) return;
        if(Eclipse.client.interactionManager == null) return;

        ArrayList<EndCrystalEntity> crystals = this.findCrystals();
        ArrayList<Entity> targets = this.findTargets(crystals);

        if(targets.isEmpty()) return;

        this.tryProtect();
        this.attackCrystals(crystals);
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
