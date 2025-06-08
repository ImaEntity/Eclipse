package com.entity.eclipse.modules.combat;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.HitResultBuilders;
import com.entity.eclipse.utils.Slots;
import com.entity.eclipse.utils.events.render.Render2DEvent;
import com.entity.eclipse.utils.events.render.Render3DEvent;
import com.entity.eclipse.utils.types.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

// Soon™
public class CrystalAura extends Module {
    public CrystalAura() {
        super("CrystalAura", "boom", ModuleType.COMBAT);

        this.config.create("CrystalAttackRange", new FloatValue(3.5f));
        this.config.create("CrystalTargetRange", new FloatValue(7f));
        this.config.create("AllowInventory", new BooleanValue(false));
        this.config.create("TryToProtect", new BooleanValue(true));
        this.config.create("JunkBlocks", new ListValue(
                ItemValue.class,
                "dirt",
                "cobblestone",
                "netherrack"
        )).visibleIf(() -> this.config.get("TryToProtect"));

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
        if(Eclipse.client.player == null) return new ArrayList<>();
        if(Eclipse.client.world == null) return new ArrayList<>();

        ArrayList<Entity> validEntities = new ArrayList<>();

        for(Entity entity : Eclipse.client.world.getEntities()) {
            if(entity == Eclipse.client.player) continue;
            if(!((ListValue) this.config.getRaw("Targets")).contains(entity.getType()))
                continue;

            float closest = Float.POSITIVE_INFINITY;
            for(EndCrystalEntity crystal : crystals)
                if(entity.distanceTo(crystal) < closest) closest = entity.distanceTo(crystal);

            if(closest <= (float) this.config.get("CrystalTargetRange"))
                validEntities.add(entity);
        }

        return validEntities;
    }

    private void placeCrystals() {
        // Find targets
        // Find/place obi blocks
        // place crystals on correct blocks
    }

    private void tryProtect() {
        if(Eclipse.client.world == null) return;
        if(Eclipse.client.player == null) return;
        if(Eclipse.client.interactionManager == null) return;

        if(!(boolean) this.config.get("TryToProtect")) return;
        if(Eclipse.client.player.isSpectator() || Eclipse.client.player.isCreative()) return;

        ArrayList<EndCrystalEntity> dangerousCrystals = new ArrayList<>();

        for(Entity entity : Eclipse.client.world.getEntities()) {
            if(entity == Eclipse.client.player) continue;
            if(!(entity instanceof EndCrystalEntity crystal)) continue;

            // Being below a crystal removes pretty much all damage you take from it
            if(Eclipse.client.player.getY() - entity.getY() > 1.5) continue;

            // If you're further than 10 blocks you take no damage
            if(crystal.distanceTo(Eclipse.client.player) <= 10)
                dangerousCrystals.add(crystal);
        }

        if(dangerousCrystals.isEmpty()) return;

        int endSlot = this.config.get("AllowInventory") ?
                Slots.MAIN.end() :
                Slots.HOTBAR.end();

        int blockSlot = Slots.findFirst(
                new Slots.Range(Slots.HOTBAR.start(), endSlot),
                (stack, slotIdx) -> ((ListValue) this.config.getRaw("JunkBlocks")).contains(stack.getItem())
        );

        if(!Slots.HOTBAR.contains(blockSlot))
            Slots.swap(Slots.indexToID(blockSlot), Slots.getSelectedID());
        else
            Eclipse.client.player.getInventory().setSelectedSlot(blockSlot);

        for(EndCrystalEntity crystal : dangerousCrystals) {
            Vec3d pointer = crystal.getPos()
                    .subtract(Eclipse.client.player.getPos())
                    .normalize();

            BlockPos pos = new BlockPos(
                    (int) (Eclipse.client.player.getX() + 1.5 * pointer.getX()),
                    (int) (Eclipse.client.player.getY() + 1.5 * pointer.getY()),
                    (int) (Eclipse.client.player.getZ() + 1.5 * pointer.getZ())
            );

            Eclipse.client.interactionManager.interactBlock(
                    Eclipse.client.player,
                    Hand.MAIN_HAND,
                    HitResultBuilders.createBlock(
                            pos,
                            Direction.UP
                    )
            );
        }
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

        this.tryProtect();

        if(targets.isEmpty()) return;
        this.attackCrystals(crystals);

        // place crystals for next tick
        this.placeCrystals();
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
