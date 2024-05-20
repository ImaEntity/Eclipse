package com.entity.eclipse.utils.types;

public class DynamicValue<T> implements Cloneable {
    public static Class<?> typeClass = null;

    protected T value;

    public DynamicValue() {
        this.value = null;
    }
    public DynamicValue(T value) {
        this.value = value;
    }

    public DynamicValue<T> fromString(String value) {
        return new DynamicValue<>();
    }

    @SuppressWarnings("unchecked")
    public void setValue(Object value) {
        if(this.value.getClass() != value.getClass()) return;
        this.value = (T) value;
    }

    public T getValue() {
        return this.value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public DynamicValue<T> clone() {
        try {
            DynamicValue<T> clone = (DynamicValue<T>) super.clone();
            clone.setValue(this.value);

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public String toRawString() {
        return this.value.toString();
    }
}
