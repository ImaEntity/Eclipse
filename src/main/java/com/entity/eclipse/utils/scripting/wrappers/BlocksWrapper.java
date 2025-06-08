package com.entity.eclipse.utils.scripting.wrappers;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.lang.reflect.Field;

public class BlocksWrapper {
    public Block get(String name) {
        try {
            Field field = Blocks.class.getDeclaredField(name);
            return (Block) field.get(null);
        } catch(Exception e) {
            return Blocks.AIR;
        }
    }
}
