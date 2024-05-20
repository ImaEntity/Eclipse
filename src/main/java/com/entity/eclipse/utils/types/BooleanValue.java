package com.entity.eclipse.utils.types;

import com.entity.eclipse.utils.Strings;

public class BooleanValue extends DynamicValue<Boolean> {
    public static Class<?> typeClass = Boolean.class;

    public BooleanValue() {
        this.value = false;
    }
    public BooleanValue(Boolean value) {
        super(value);
    }

    @Override
    public BooleanValue fromString(String value) {
        if(value.isEmpty()) return new BooleanValue();
        return new BooleanValue(Boolean.parseBoolean(value));
    }

    @Override
    public void setValue(Object value) {
        if(!(value instanceof Boolean)) return;
        this.value = (Boolean) value;
    }

    @Override
    public String toRawString() {
        return Boolean.toString(this.value);
    }

    @Override
    public String toString() {
        return Strings.format(this.value);
    }
}
