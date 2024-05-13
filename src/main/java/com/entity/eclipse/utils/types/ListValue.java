package com.entity.eclipse.utils.types;

import java.util.ArrayList;

public class ListValue extends DynamicValue<ArrayList<DynamicValue<?>>> {
    public static Class<?> typeClass = ArrayList.class;
    private final Class<?> elementClass;

    public ListValue(Class<?> elementClass, ArrayList<DynamicValue<?>> values) {
        super(values);

        this.elementClass = elementClass;
    }

    public ListValue(Class<?> elementClass, String... values) {
        super(
                values.length == 0 ?
                        new ArrayList<>() :
                        new ListValue(elementClass, new ArrayList<>())
                                .fromString(String.join(",", values))
                                .getValue()
        );

        this.elementClass = elementClass;
    }

    @SuppressWarnings("unchecked")
    public <T extends DynamicValue<?>> T newElementInstance() {
        try {
            return (T) ((DynamicValue<?>) this.elementClass
                    .getConstructor((Class<?>) this.elementClass.getField("typeClass").get(null))
                    .newInstance(new Object[]{null}))
                    .fromString("");
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ListValue fromString(String value) {
        ArrayList<DynamicValue<?>> list = new ArrayList<>();
        String[] values = value.split(",");

        for(String val : values) {
            try {
                // All because I don't want a list type for every class
                // Please fucking kill me

                list.add(
                        ((DynamicValue<?>) this.elementClass
                                .getConstructor((Class<?>) this.elementClass.getField("typeClass").get(null))
                                .newInstance(new Object[]{null}))
                                .fromString(val)
                );
            } catch(Exception e) {
                e.printStackTrace();
                return new ListValue(this.elementClass, new ArrayList<>());
            }
        }

        return new ListValue(this.elementClass, list);
    }

    public <T extends DynamicValue<?>> void set(int index, T element) {
        this.value.set(index, element);
    }
    @SuppressWarnings("unchecked")
    public <T extends DynamicValue<?>> T get(int index) {
        return (T) this.value.get(index);
    }
    public <T extends DynamicValue<?>> void add(T element) {
        this.value.add(element);
    }
    public <T extends DynamicValue<?>> void insert(int index, T element) {
        this.value.add(index, element);
    }
    @SuppressWarnings("unchecked")
    public <T extends DynamicValue<?>> T remove(int index) {
        return (T) this.value.remove(index);
    }
    public <T extends DynamicValue<?>> void remove(T element) {
        this.value.remove(element);
    }
    public int size() {
        return this.value.size();
    }
    public boolean isEmpty() {
        return this.value.isEmpty();
    }
    public <T extends DynamicValue<?>> int indexOf(T element) {
        return this.value.indexOf(element);
    }
    @SuppressWarnings("unchecked")
    public <T extends DynamicValue<?>> T copy(int index) {
        return (T) this.value.get(index).clone();
    }

    public <T> boolean contains(T element) {
        for(DynamicValue<?> val : this.value) {
            if(val.getValue().getClass() != element.getClass()) return false;
            if(val.getValue() == element) return true;
        }

        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setValue(Object value) {
        if(!(value instanceof ArrayList)) return;
        this.value = (ArrayList<DynamicValue<?>>) value;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        if(this.value.size() == 0)
            return "§8[EMPTY_LIST]§r";

        for(DynamicValue<?> value : this.value)
            str.append(value.toString()).append(',');

        str.reverse();
        str.deleteCharAt(0);
        str.reverse();

        return str.toString();
    }
}
