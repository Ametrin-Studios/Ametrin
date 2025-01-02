package com.ametrinstudios.ametrin.world.gen.feature.tree.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import static com.ametrinstudios.ametrin.world.gen.feature.tree.helper.TreeHelper.setBlockChecked;

public final class TreeHelper3x3 {
    /**
     * Places a vertical 3 by 3 trunk.
     *
     * @param log     trunk {@link BlockState}
     * @param height  height of the trunk in blocks
     * @param context {@link TreePlaceContext}
     * @return {@link BlockPos} over the trunk
     */
    public static BlockPos placeTrunk(BlockState log, int height, TreePlaceContext context) {
        for (var y = 0; y < height; y++) {
            setBlockChecked(log, context.pos().offset(-1, y, -1), context.level(), context.changedLogs());
            setBlockChecked(log, context.pos().offset(-1, y, 0), context.level(), context.changedLogs());
            setBlockChecked(log, context.pos().offset(-1, y, 1), context.level(), context.changedLogs());

            setBlockChecked(log, context.pos().offset(0, y, -1), context.level(), context.changedLogs());
            setBlockChecked(log, context.pos().offset(0, y, 0), context.level(), context.changedLogs());
            setBlockChecked(log, context.pos().offset(0, y, 1), context.level(), context.changedLogs());

            setBlockChecked(log, context.pos().offset(1, y, -1), context.level(), context.changedLogs());
            setBlockChecked(log, context.pos().offset(1, y, 0), context.level(), context.changedLogs());
            setBlockChecked(log, context.pos().offset(1, y, 1), context.level(), context.changedLogs());
        }
        return context.pos().above(height);
    }
}
