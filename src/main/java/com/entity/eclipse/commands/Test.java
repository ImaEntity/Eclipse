package com.entity.eclipse.commands;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.commands.base.Command;

public class Test extends Command {
    public Test() {
        super("Test", "idfk", "test");
    }

    @Override
    public void onExecute(String[] args) {
        if(Eclipse.client.interactionManager == null) return;

        Eclipse.client.interactionManager.attackEntity(
                Eclipse.client.player,
                Eclipse.client.player
        );
    }
}
