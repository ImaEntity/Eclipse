package com.entity.eclipse.utils.types;

import com.entity.eclipse.utils.Strings;

public class FloatValue extends DynamicValue<Float> {
    public static Class<?> typeClass = Float.class;

    public FloatValue() {
        this.value = 0f;
    }
    public FloatValue(Float value) {
        super(value);
    }

    @Override
    public FloatValue fromString(String value) {
        if(value.isEmpty()) return new FloatValue();
        return new FloatValue(Float.parseFloat(value));
    }

    @Override
    public void setValue(Object value) {
        if(!(value instanceof Float)) return;
        this.value = (Float) value;
    }

    @Override
    public String toRawString() {
        return Float.toString(this.value);
    }

    @Override
    public String toString() {
        return Strings.format(this.value);
    }
}
