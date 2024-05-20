package com.entity.eclipse.utils.types;

import com.entity.eclipse.utils.Strings;

public class ShortValue extends DynamicValue<Short> {
    public static Class<?> typeClass = Short.class;

    public ShortValue() {
        this.value = 0;
    }
    public ShortValue(Short value) {
        super(value);
    }

    @Override
    public ShortValue fromString(String value) {
        if(value.isEmpty()) return new ShortValue();
        return new ShortValue(Short.parseShort(value));
    }

    @Override
    public void setValue(Object value) {
        if(!(value instanceof Short)) return;
        this.value = (Short) value;
    }

    @Override
    public String toRawString() {
        return Short.toString(this.value);
    }

    @Override
    public String toString() {
        return Strings.format(this.value);
    }
}
