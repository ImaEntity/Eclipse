package com.entity.eclipse.utils.types;

import com.entity.eclipse.utils.Strings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;

public class BlockValue extends DynamicValue<Block> {
    public static Class<?> typeClass = Block.class;

    public BlockValue() {
        this.value = Blocks.AIR;
    }
    public BlockValue(Block value) {
        super(value);
    }

    @Override
    public BlockValue fromString(String value) {
        if(value.isEmpty()) return new BlockValue();

        for(int i = 0; i < Registries.BLOCK.size(); i++) {
            Block block = Registries.BLOCK.get(i);
            String blockID = Registries.BLOCK.getId(block).getPath();

            if(blockID.equalsIgnoreCase(value)) return new BlockValue(block);
        }

        throw new NullPointerException();
    }

    @Override
    public void setValue(Object value) {
        if(!(value instanceof Block)) return;
        this.value = (Block) value;
    }

    @Override
    public String toRawString() {
        return Registries.BLOCK.getId(this.value).getPath();
    }

    @Override
    public String toString() {
        return Strings.format(this.toRawString());
    }
}
