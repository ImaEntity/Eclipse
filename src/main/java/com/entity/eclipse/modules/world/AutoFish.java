package com.entity.eclipse.modules.world;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.packet.PacketEvents;
import com.entity.eclipse.utils.events.render.RenderEvent;
import com.entity.eclipse.utils.types.DoubleValue;
import com.entity.eclipse.utils.types.IntegerValue;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

public class AutoFish extends Module {
    private boolean caught = false;
    private int catchTicks = 0;
    private int reelTicks = 0;
    private int castTicks = 0;

    public AutoFish() {
        super("AutoFish", "Automatically fishes.", ModuleType.WORLD);

        this.config.create("HookDetectionDistance", new DoubleValue(0.25));
        this.config.create("FishingTimeout", new DoubleValue(60.0));
        this.config.create("CastCooldown", new IntegerValue(3));
        this.config.create("CatchDelay", new IntegerValue(3));

        Events.Packet.register(PacketEvents.RECEIVE, event -> {
            if(!this.isEnabled()) return;

            if(Eclipse.client.player == null) return;
            if(Eclipse.client.player.fishHook == null) return;

            if(!(event.getPacket() instanceof PlaySoundS2CPacket sound)) return;
            if(sound.getSound().value() != SoundEvents.ENTITY_FISHING_BOBBER_SPLASH) return;
            if(!isFishing()) return;

            double distance = Eclipse.client.player.fishHook.getPos()
                    .distanceTo(new Vec3d(sound.getX(), sound.getY(), sound.getZ()));

            if(distance > (double) this.config.get("HookDetectionDistance")) return;

            this.caught = true;
        });
    }

    private boolean isFishing() {
        if(Eclipse.client.player == null) return false;
        if(Eclipse.client.player.fishHook == null) return false;
        if(Eclipse.client.player.fishHook.isRemoved()) return false;

        return Eclipse.client.player.getMainHandStack().isOf(Items.FISHING_ROD);
    }

    @Override
    public void tick() {
        if(Eclipse.client.player == null) return;
        if(Eclipse.client.interactionManager == null) return;
        if(Eclipse.client.getNetworkHandler() == null) return;

        ItemStack stack = Eclipse.client.player.getMainHandStack();
        if(!stack.isOf(Items.FISHING_ROD)) return;

        this.castTicks--;
        this.reelTicks--;

        if(this.caught)
            this.catchTicks--;

        if(!isFishing()) {
            if(this.castTicks > 0) return;

            this.reelTicks = (int) (20 * (double) this.config.get("FishingTimeout"));
            this.castTicks = this.config.get("CastCooldown");

            Eclipse.client.getNetworkHandler().sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
            Eclipse.client.interactionManager.interactItem(Eclipse.client.player, Hand.MAIN_HAND);

            return;
        }

        if(Eclipse.client.player.fishHook == null) return;
        if(Eclipse.client.player.fishHook.getHookedEntity() != null)
            this.caught = true;

        if(this.caught) {
            if(this.catchTicks > 0) return;

            Eclipse.client.getNetworkHandler().sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
            Eclipse.client.interactionManager.interactItem(Eclipse.client.player, Hand.MAIN_HAND);

            this.catchTicks = this.config.get("CatchDelay");
            this.castTicks = this.config.get("CastCooldown");
            this.caught = false;

            return;
        }

        if(this.reelTicks > 0) return;

        Eclipse.client.getNetworkHandler().sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
        Eclipse.client.interactionManager.interactItem(Eclipse.client.player, Hand.MAIN_HAND);

        this.reelTicks = (int) (20 * (double) this.config.get("FishingTimeout"));
        this.castTicks = this.config.get("CastCooldown");
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
