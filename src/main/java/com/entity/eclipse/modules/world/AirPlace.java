package com.entity.eclipse.modules.world;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.RenderEvent;
import com.entity.eclipse.utils.types.DoubleValue;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

public class AirPlace extends Module {
    public AirPlace() {
        super("AirPlace", "floating blocks", ModuleType.WORLD);

        this.config.create("Distance", new DoubleValue(3.5));
    }

    @Override
    public void tick() {
        if(Eclipse.client.player == null) return;
        if(Eclipse.client.interactionManager == null) return;
        if(Eclipse.client.getCameraEntity() == null) return;

        double range = this.config.get("Distance");
        HitResult hit = Eclipse.client.getCameraEntity().raycast(range, 0, false);

        if(!(hit instanceof BlockHitResult)) return;

        Item mainHandItem = Eclipse.client.player.getMainHandStack().getItem();
        Item offHandItem = Eclipse.client.player.getOffHandStack().getItem();

        boolean validMainHand = (mainHandItem instanceof BlockItem) || (mainHandItem instanceof SpawnEggItem);
        boolean validOffHand = (offHandItem instanceof BlockItem) || (offHandItem instanceof SpawnEggItem);

        if(!validMainHand && !validOffHand) return;

        if(Eclipse.client.options.useKey.isPressed()) {
            Eclipse.client.interactionManager.interactBlock(
                    Eclipse.client.player,
                    validMainHand ? Hand.MAIN_HAND : Hand.OFF_HAND,
                    (BlockHitResult) hit
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
