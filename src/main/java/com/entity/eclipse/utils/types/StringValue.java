package com.entity.eclipse.utils.types;

import com.entity.eclipse.utils.Strings;

public class StringValue extends DynamicValue<String> {
    public static Class<?> typeClass = String.class;

    public StringValue() {
        this.value = "";
    }
    public StringValue(String value) {
        super(value);
    }

    @Override
    public StringValue fromString(String value) {
        if(value.isEmpty()) return new StringValue();
        return new StringValue(value);
    }

    @Override
    public void setValue(Object value) {
        if(!(value instanceof String)) return;
        this.value = (String) value;
    }

    @Override
    public String toString() {
        return Strings.format(this.value);
    }
}
