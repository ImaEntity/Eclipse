package com.entity.eclipse.utils.types;

import com.entity.eclipse.utils.Strings;

public class ByteValue extends DynamicValue<Byte> {
    public static Class<?> typeClass = Byte.class;

    public ByteValue() {
        this.value = 0;
    }
    public ByteValue(Byte value) {
        super(value);
    }

    @Override
    public ByteValue fromString(String value) {
        if(value.isEmpty()) return new ByteValue();
        return new ByteValue(Byte.parseByte(value));
    }

    @Override
    public void setValue(Object value) {
        if(!(value instanceof Byte)) return;
        this.value = (Byte) value;
    }

    @Override
    public String toRawString() {
        return Byte.toString(this.value);
    }

    @Override
    public String toString() {
        return Strings.format(this.value);
    }
}
