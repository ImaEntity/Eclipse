package com.entity.eclipse.utils.types;

import com.entity.eclipse.utils.Strings;

public class CharacterValue extends DynamicValue<Character> {
    public static Class<?> typeClass = Character.class;

    public CharacterValue() {
        this.value = '\0';
    }
    public CharacterValue(Character value) {
        super(value);
    }

    @Override
    public CharacterValue fromString(String value) {
        if(value.isEmpty()) return new CharacterValue();
        return new CharacterValue(value.charAt(0));
    }

    @Override
    public void setValue(Object value) {
        if(!(value instanceof Character)) return;
        this.value = (Character) value;
    }

    @Override
    public String toRawString() {
        return Character.toString(this.value);
    }

    @Override
    public String toString() {
        return Strings.format(this.value);
    }
}
