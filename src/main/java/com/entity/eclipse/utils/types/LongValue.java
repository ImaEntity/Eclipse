package com.entity.eclipse.utils.types;

import com.entity.eclipse.utils.Strings;

public class LongValue extends DynamicValue<Long> {
    public static Class<?> typeClass = Long.class;

    public LongValue() {
        this.value = 0L;
    }
    public LongValue(Long value) {
        super(value);
    }

    @Override
    public LongValue fromString(String value) {
        if(value.isEmpty()) return new LongValue();
        return new LongValue(Long.parseLong(value));
    }

    @Override
    public void setValue(Object value) {
        if(!(value instanceof Long)) return;
        this.value = (Long) value;
    }

    @Override
    public String toString() {
        return Strings.format(this.value);
    }
}
