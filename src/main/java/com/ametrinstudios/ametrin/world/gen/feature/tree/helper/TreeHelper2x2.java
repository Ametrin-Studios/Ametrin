package com.ametrinstudios.ametrin.world.gen.feature.tree.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import static com.ametrinstudios.ametrin.world.gen.feature.tree.helper.TreeHelper.setBlockChecked;

public class TreeHelper2x2 {
    public static final double CIRCULAR_LEAVES_ROUNDING_MULTIPLIER = 1.76;

    /**
     * Places a vertical 2 by 2 trunk.
     * @param log trunk {@link BlockState}
     * @param height height of the trunk in blocks
     * @param context {@link TreePlaceContext}
     * @return {@link BlockPos} over the trunk
     */
    public static BlockPos placeTrunk(BlockState log, int height, TreePlaceContext context) {
        for(int y = 0; y < height; y++) {
            setBlockChecked(log, context.pos().offset(0, y, 0), context.level(), context.changedLogs());
            setBlockChecked(log, context.pos().offset(0, y, 1), context.level(), context.changedLogs());
            setBlockChecked(log, context.pos().offset(1, y, 0), context.level(), context.changedLogs());
            setBlockChecked(log, context.pos().offset(1, y, 1), context.level(), context.changedLogs());
        }
        return context.pos().above(height);
    }

    protected void circularLeaves(BlockState leaf, int radius, TreePlaceContext context){
        for(int x = -radius; x <= radius+1; x++) {
            for(int z = -radius; z <= radius+1; z++) {
                var dX = Math.abs(x);
                var dZ = Math.abs(z);
                if(x < 0) dX++;
                if(z < 0) dZ++;
                if (dX + dZ > radius*CIRCULAR_LEAVES_ROUNDING_MULTIPLIER) continue;
                setBlockChecked(leaf, context.pos().offset(x, 0, z), context.level(), context.changedLeaves());
            }
        }
    }

    protected void circularSparseLeaves(BlockState leaf, int radius, TreePlaceContext context){
        for(int x = -radius; x <= radius+1; x++) {
            for(int z = -radius; z <= radius+1; z++) {
                if(context.random().nextBoolean()) continue;
                var dX = Math.abs(x);
                var dZ = Math.abs(z);
                if(x < 0) dX++;
                if(z < 0) dZ++;
                if (dX + dZ <= radius*CIRCULAR_LEAVES_ROUNDING_MULTIPLIER){
                    setBlockChecked(leaf, context.pos().offset(x, 0, z), context.level(), context.changedLeaves());
                }
            }
        }
    }

    protected void circularSparseLeaves(BlockState leaf, int radius, double decayChance, TreePlaceContext context){
        for(int x = -radius; x <= radius+1; x++) {
            for(int z = -radius; z <= radius+1; z++) {
                if(context.random().nextDouble() <= decayChance) continue;
                var dX = Math.abs(x);
                var dZ = Math.abs(z);
                if(x < 0) dX++;
                if(z < 0) dZ++;
                if (dX + dZ <= radius*CIRCULAR_LEAVES_ROUNDING_MULTIPLIER){
                    setBlockChecked(leaf, context.pos().offset(x, 0, z), context.level(), context.changedLeaves());
                }
            }
        }
    }
}
