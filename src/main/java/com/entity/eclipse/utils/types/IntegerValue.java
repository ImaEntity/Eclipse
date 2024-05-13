package com.entity.eclipse.utils.types;

import com.entity.eclipse.utils.Strings;

public class IntegerValue extends DynamicValue<Integer> {
    public static Class<?> typeClass = Integer.class;

    public IntegerValue() {
        this.value = 0;
    }
    public IntegerValue(Integer value) {
        super(value);
    }

    @Override
    public IntegerValue fromString(String value) {
        if(value.isEmpty()) return new IntegerValue();
        return new IntegerValue(Integer.parseInt(value));
    }

    @Override
    public void setValue(Object value) {
        if(!(value instanceof Integer)) return;
        this.value = (Integer) value;
    }

    @Override
    public String toString() {
        return Strings.format(this.value);
    }
}
