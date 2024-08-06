package com.ametrinstudios.ametrin.world.gen.feature.tree.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

import static net.minecraft.world.level.levelgen.feature.Feature.isDirt;

public final class TreeHelper {
    public static boolean setBlockChecked(BlockState blockState, BlockPos pos, WorldGenLevel level, @Nullable BiConsumer<BlockPos, BlockState> changedBlocks) {
        var block = blockState.getBlock();

        if(blockState.is(BlockTags.LOGS)){
            if(!isReplaceableByLogs(pos, level)) return false;
            setBlock(blockState, pos, level, changedBlocks);
            return true;
        }

        if(blockState.is(BlockTags.LEAVES)){
            if(!isReplaceableByLeaves(pos, level)) return false;
            setBlock(blockState, pos, level, changedBlocks);
            return true;
        }

        if(!(level.isEmptyBlock(pos) && blockState.canSurvive(level, pos))) return false;

        setBlock(blockState, pos, level, changedBlocks);
        if(block instanceof SnowLayerBlock) {
            var belowState = level.getBlockState(pos.below());
            if(belowState.hasProperty(BlockStateProperties.SNOWY)) {
                level.setBlock(pos.below(), belowState.setValue(BlockStateProperties.SNOWY, true), 2);
            }
        }
        return true;
    }

    private static void setBlock(BlockState state, BlockPos pos, WorldGenLevel level, @Nullable BiConsumer<BlockPos, BlockState> changedBlocks) {
        if (state.hasProperty(BlockStateProperties.WATERLOGGED) && level.getFluidState(pos).is(Fluids.WATER)) {
            state = state.setValue(BlockStateProperties.WATERLOGGED, true);
        }

        if(changedBlocks == null){
            TreeFeature.setBlockKnownShape(level, pos, state);
            return;
        }
        changedBlocks.accept(pos, state);
    }

    public static boolean isAreaBlocked(WorldGenLevel level, BlockPos pos, int trunkHeight, int trunkWidth, int leafStartHeight, int leafWidth) {
        if (pos.getY() >= 1 && pos.getY() + trunkHeight + 1 < level.getHeight()) {
            int width;
            int y;
            int x;
            if (trunkWidth == 1) {
                for(width = 0; width <= 1 + trunkHeight && pos.getY() + width >= 1 && pos.getY() + width < level.getHeight(); ++width) {
                    if (!isReplaceableByLogs(pos.above(width), level) && !isLeavesOrLog(level.getBlockState(pos.above(width)))) {
                        return true;
                    }
                }
            } else {
                for(width = 0; width <= 1 + trunkHeight && pos.getY() + width >= 1 && pos.getY() + width < level.getHeight(); ++width) {
                    for(y = 0; y < trunkWidth; ++y) {
                        for(x = 0; x < trunkWidth; ++x) {
                            if (!isReplaceableByLogs(pos.offset(y, width, x), level) && !isLeavesOrLog(level.getBlockState(pos.offset(y, width, x)))) {
                                return true;
                            }
                        }
                    }
                }
            }

            width = Math.max((leafWidth - 1) / 2, 1);

            for(y = leafStartHeight; y <= trunkHeight + 2 && pos.getY() + y >= 0 && pos.getY() + y < level.getHeight(); ++y) {
                for(x = -width; x <= width; ++x) {
                    for(int z = -width; z <= width; ++z) {
                        if (!isReplaceableByLeaves(pos.offset(x, y, z), level) && !isLeavesOrLog(level.getBlockState(pos.offset(x, y, z)))) {
                            return true;
                        }
                    }
                }
            }

            return false;
        } else {
            return true;
        }
    }

    public static boolean isValidGround(BlockState state) {return isDirt(state);}

    public static boolean isReplaceableByLeaves(BlockPos pos, WorldGenLevel level) {
        return isReplaceableByLeaves(level.getBlockState(pos));
    }
    public static boolean isReplaceableByLogs(BlockPos pos, WorldGenLevel level) {
        return isReplaceableByLogs(level.getBlockState(pos));
    }
    public static boolean isReplaceableByLeaves(BlockState state) {
        return state.canBeReplaced() || state.is(BlockTags.LEAVES);
    }
    public static boolean isReplaceableByLogs(BlockState state) {
        return state.canBeReplaced() || isLeavesOrLog(state); //|| state.getMaterial() == Material.PLANT;
    }
    public static boolean isLeavesOrLog(BlockState state) {return state.is(BlockTags.LOGS) || state.is(BlockTags.LEAVES);}
}
