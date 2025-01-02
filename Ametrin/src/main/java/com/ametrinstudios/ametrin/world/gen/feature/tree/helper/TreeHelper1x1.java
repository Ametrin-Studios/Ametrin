package com.ametrinstudios.ametrin.world.gen.feature.tree.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import static com.ametrinstudios.ametrin.world.gen.feature.tree.helper.TreeHelper.setBlockChecked;

public final class TreeHelper1x1 {
    public static final double CIRCULAR_LEAVES_ROUNDING_MULTIPLIER = 1.7;

    /**
     * Places a vertical trunk.
     *
     * @param log     trunk {@link BlockState}
     * @param height  height of the trunk in blocks
     * @param context {@link TreePlaceContext}
     * @return {@link BlockPos} over the trunk
     */
    public static BlockPos placeTrunk(BlockState log, int height, TreePlaceContext context) {
        for (int y = 0; y < height; y++) {
            setBlockChecked(log, context.pos().above(y), context.level(), context.changedLogs());
        }
        return context.pos().above(height);
    }

    public static void squareLeaves(BlockState leaf, int radius, TreePlaceContext context) {
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                setBlockChecked(leaf, context.pos().offset(x, 0, z), context.level(), context.changedLeaves());
            }
        }
    }

    public static void squareSparseLeaves(BlockState leaf, int radius, TreePlaceContext context) {
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (context.random().nextBoolean()) continue;
                setBlockChecked(leaf, context.pos().offset(x, 0, z), context.level(), context.changedLeaves());
            }
        }
    }

    public static void squareSparseLeaves(BlockState leaf, int radius, double decayChance, TreePlaceContext context) {
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (context.random().nextDouble() <= decayChance) continue;
                setBlockChecked(leaf, context.pos().offset(x, 0, z), context.level(), context.changedLeaves());
            }
        }
    }

    public static void circularLeaves(BlockState leaf, int radius, TreePlaceContext context) {
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (Math.abs(z) + Math.abs(x) > radius * CIRCULAR_LEAVES_ROUNDING_MULTIPLIER) continue;
                setBlockChecked(leaf, context.pos().offset(x, 0, z), context.level(), context.changedLeaves());
            }
        }
    }

    public static void circularSparseLeaves(BlockState leaf, int radius, TreePlaceContext context) {
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (context.random().nextBoolean()) continue;
                if (Math.abs(z) + Math.abs(x) > radius * CIRCULAR_LEAVES_ROUNDING_MULTIPLIER) continue;
                setBlockChecked(leaf, context.pos().offset(x, 0, z), context.level(), context.changedLeaves());
            }
        }
    }

    public static void circularSparseLeaves(BlockState leaf, int radius, double decayChance, TreePlaceContext context) {
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (context.random().nextDouble() <= decayChance) continue;
                if (Math.abs(z) + Math.abs(x) > radius * CIRCULAR_LEAVES_ROUNDING_MULTIPLIER) continue;
                setBlockChecked(leaf, context.pos().offset(x, 0, z), context.level(), context.changedLeaves());
            }
        }
    }
}
