package com.entity.eclipse.commands;

import com.entity.eclipse.commands.base.Command;

import java.util.HashMap;

public class Test extends Command {
    private HashMap<String, Object> data = new HashMap<>();

    public Test() {
        super("Test", "idfk", "test");
    }

    @Override
    public void onExecute(String[] args) {

    }
}
