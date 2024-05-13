package com.entity.eclipse.utils;

import java.util.ArrayList;

public class Ticker {
    private static final ArrayList<Runnable> actions = new ArrayList<>();

    public static void tick() {
        if(actions.size() > 0)
            actions.remove(0).run();
    }

    public static void queue(Runnable action) {
        actions.add(action);
    }
}
