package com.entity.eclipse.utils.types;

import com.entity.eclipse.utils.Strings;

public class DoubleValue extends DynamicValue<Double> {
    public static Class<?> typeClass = Double.class;

    public DoubleValue() {
        this.value = 0.0;
    }
    public DoubleValue(Double value) {
        super(value);
    }

    @Override
    public DoubleValue fromString(String value) {
        if(value.isEmpty()) return new DoubleValue();
        return new DoubleValue(Double.parseDouble(value));
    }

    @Override
    public void setValue(Object value) {
        if(!(value instanceof Double)) return;
        this.value = (Double) value;
    }

    @Override
    public String toString() {
        return Strings.format(this.value);
    }
}
