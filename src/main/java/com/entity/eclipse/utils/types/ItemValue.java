package com.entity.eclipse.utils.types;

import com.entity.eclipse.utils.Strings;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;

public class ItemValue extends DynamicValue<Item> {
    public static Class<?> typeClass = Item.class;

    public ItemValue() {
        this.value = Items.AIR;
    }
    public ItemValue(Item value) {
        super(value);
    }

    @Override
    public ItemValue fromString(String value) {
        if(value.isEmpty()) return new ItemValue();

        for(int i = 0; i < Registries.ITEM.size(); i++) {
            Item item = Registries.ITEM.get(i);
            String itemID = Registries.ITEM.getId(item).getPath();

            if(itemID.equalsIgnoreCase(value)) return new ItemValue(item);
        }

        throw new NullPointerException();
    }

    @Override
    public void setValue(Object value) {
        if(!(value instanceof Item)) return;
        this.value = (Item) value;
    }

    @Override
    public String toString() {
        return Strings.format(Registries.ITEM.getId(this.value).getPath());
    }
}
