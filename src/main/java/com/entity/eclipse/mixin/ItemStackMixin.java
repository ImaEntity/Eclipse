package com.entity.eclipse.mixin;

import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.lore.LoreEvent;
import com.entity.eclipse.utils.events.lore.LoreEvents;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "getTooltip", at = @At("RETURN"), cancellable = true)
    void onTooltipRequest(Item.TooltipContext context, PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> info) {
        List<Text> returnValue = info.getReturnValue();
        LoreEvent event = new LoreEvent((ItemStack) (Object) this, returnValue);

        Events.Lore.fireEvent(LoreEvents.REQUEST, event);
        info.setReturnValue(event.getLore());
    }
}
