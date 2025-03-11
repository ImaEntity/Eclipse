package com.entity.eclipse.utils;

import com.entity.eclipse.utils.types.DynamicValue;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;


public class Configuration {
    @FunctionalInterface
    public interface VisibilityFilter {
        boolean isVisible();
    }

    // Hacky ass way to do this without rewriting like half the client
    public record FilterManager(String key, HashMap<String, VisibilityFilter> filters) {
        public void visibleIf(VisibilityFilter filter) {
            this.filters.put(this.key, filter);
        }
    }

    private final HashMap<String, VisibilityFilter> filters = new HashMap<>();
    private final LinkedHashMap<String, DynamicValue<?>> options = new LinkedHashMap<>();

    public FilterManager create(String name, DynamicValue<?> value) {
        this.options.put(name, value);
        return new FilterManager(name, this.filters);
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
    public boolean isVisible(String name) {
        return this.filters.getOrDefault(name, () -> true).isVisible();
    }
    public Set<String> getAll() {
        return this.options.keySet();
    }
}
