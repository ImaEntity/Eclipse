package com.entity.eclipse.utils.types;

import com.entity.eclipse.utils.Strings;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;

public class EntityTypeValue extends DynamicValue<EntityType<?>> {
    public static Class<?> typeClass = EntityType.class;

    public EntityTypeValue() {
        this.value = EntityType.PLAYER;
    }
    public EntityTypeValue(EntityType<?> value) {
        super(value);
    }

    @Override
    public EntityTypeValue fromString(String value) {
        if(value.isEmpty()) return new EntityTypeValue();

        for(int i = 0; i < Registries.ENTITY_TYPE.size(); i++) {
            EntityType<?> entityType = Registries.ENTITY_TYPE.get(i);
            String entityID = Registries.ENTITY_TYPE.getId(entityType).getPath();

            if(entityID.equalsIgnoreCase(value)) return new EntityTypeValue(entityType);
        }

        throw new NullPointerException();
    }

    @Override
    public void setValue(Object value) {
        if(!(value instanceof EntityType<?>)) return;
        this.value = (EntityType<?>) value;
    }

    @Override
    public String toRawString() {
        return Registries.ENTITY_TYPE.getId(this.value).getPath();
    }

    @Override
    public String toString() {
        return Strings.format(Registries.ENTITY_TYPE.getId(this.value).getPath());
    }
}
