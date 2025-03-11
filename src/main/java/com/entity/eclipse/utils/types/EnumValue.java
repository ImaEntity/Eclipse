package com.entity.eclipse.utils.types;

import com.entity.eclipse.utils.Strings;

// Half of this code's probably just wrong
public class EnumValue<T extends Enum<?>> extends DynamicValue<T> {
    public static Class<?> typeClass = Enum.class;
    public final T[] values;

    @SuppressWarnings("unchecked")
    public EnumValue() {
        this.value = (T) T.valueOf(Enum.class, "");
        this.values = (T[]) this.value.getDeclaringClass().getEnumConstants();
    }

    @SuppressWarnings("unchecked")
    public EnumValue(T value) {
        super(value);
        this.values = (T[]) this.value.getDeclaringClass().getEnumConstants();
    }

    @Override
    public EnumValue<T> fromString(String value) {
        if(value.isEmpty()) return new EnumValue<>();

        for(T potentialValue : this.values) {
            if(value.equalsIgnoreCase(potentialValue.toString()))
                return new EnumValue<>(potentialValue);
        }

        throw new NullPointerException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setValue(Object value) {
        if(!(value instanceof Enum<?>)) return;
        this.value = (T) value;
    }

    @Override
    public String toRawString() {
        if(this.value == null) return "";
        return this.value.toString();
    }

    @Override
    public String toString() {
        if(this.value == null) return "";
        return Strings.format(this.value.toString());
    }
}
