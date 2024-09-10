package com.entity.eclipse.utils;

import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class HitResultBuilders {
    public static BlockHitResult createBlock(BlockPos pos, Direction dir) {
        return createBlock(pos, dir, false);
    }

    public static BlockHitResult createBlock(BlockPos pos, Direction dir, boolean insideBlock) {
        double xOff = Math.random() * 0.5 - 0.25;
        double yOff = Math.random() * 0.5 - 0.25;
        double zOff = Math.random() * 0.5 - 0.25;

        if(insideBlock) {
            return new BlockHitResult(
                    Vec3d.of(pos)
                            .add(xOff, yOff, zOff),
                    dir,
                    pos,
                    true
            );
        }

        double actualXOff = dir.getOffsetX() == 0 ? xOff : 0;
        double actualYOff = dir.getOffsetY() == 0 ? yOff : 0;
        double actualZOff = dir.getOffsetZ() == 0 ? zOff : 0;

        return new BlockHitResult(
                Vec3d.of(pos)
                        .add(actualXOff, actualYOff, actualZOff),
                dir,
                pos,
                false
        );
    }
}
