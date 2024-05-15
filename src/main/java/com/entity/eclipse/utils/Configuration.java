package com.entity.eclipse.utils;

import com.entity.eclipse.utils.types.DynamicValue;

import java.util.HashMap;
import java.util.Set;

public class Configuration {
    private final HashMap<String, DynamicValue<?>> options;

    public Configuration() {
        this.options = new HashMap<>();
    }

    public void create(String name, DynamicValue<?> value) {
        this.options.put(name, value);
    }
    public DynamicValue<?> getRaw(String name) {
        return this.options.get(name);
    }
    public DynamicValue<?> removeRaw(String name) {
        return this.options.remove(name);
    }
    public <T> void set(String name, T value) {
        this.options.get(name).setValue(value);
    }
    @SuppressWarnings("unchecked")
    public <T> T remove(String name) {
        return (T) this.options.remove(name).getValue();
    }
    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        return (T) this.options.get(name).getValue();
    }
    public Set<String> getAll() {
        return this.options.keySet();
    }
}
