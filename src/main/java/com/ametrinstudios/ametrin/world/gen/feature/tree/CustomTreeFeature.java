package com.ametrinstudios.ametrin.world.gen.feature.tree;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BiConsumer;

/**
 * This is an alternative method of adding trees, if possible use the vanilla way!
 */
public abstract class CustomTreeFeature extends TreeFeature{
    public CustomTreeFeature(Codec<TreeConfiguration> codec) {super(codec);}

    @Override @ApiStatus.Internal @ParametersAreNonnullByDefault
    protected boolean doPlace(WorldGenLevel level, RandomSource random, BlockPos pos, BiConsumer<BlockPos, BlockState> changedLogs, BiConsumer<BlockPos, BlockState> changedLeaves, FoliagePlacer.FoliageSetter foliageSetter, TreeConfiguration configuration) {
        return place(new PlaceContext(level, pos, changedLogs, changedLeaves, foliageSetter, random));
    }

    protected boolean isValidGround(BlockState state) {return isDirt(state);}

    public abstract boolean place(PlaceContext context);

    protected BlockPos placeTrunk(BlockState log, int height, BlockPos pos, WorldGenLevel level, BiConsumer<BlockPos, BlockState> changedLogs){
        for(int i = 0; i <= height; i++){
            setBlockChecked(log, pos, level, changedLogs);
            pos = pos.above();
        }
        return pos;
    }

    protected boolean setBlockChecked(BlockState blockState, BlockPos pos, WorldGenLevel level, @Nullable BiConsumer<BlockPos, BlockState> changedBlocks) {
        Block block = blockState.getBlock();
        if (block instanceof RotatedPillarBlock) {
            if (isReplaceableByLogs(level, pos)) {
                setBlock(changedBlocks, level, pos, blockState);
                return true;
            } else {
                return false;
            }
        } else if (block instanceof LeavesBlock) {
            if (isReplaceableByLeaves(level, pos)) {
                setBlock(changedBlocks, level, pos, blockState);
                return true;
            } else {
                return false;
            }
        } else if (level.isEmptyBlock(pos) && blockState.canSurvive(level, pos)) {
            setBlock(changedBlocks, level, pos, blockState);
            if (block instanceof SnowLayerBlock) {
                BlockState lower = level.getBlockState(pos.below());
                if (lower.hasProperty(BlockStateProperties.SNOWY)) {
                    level.setBlock(pos.below(), lower.setValue(BlockStateProperties.SNOWY, true), 2);
                }
            }

            return true;
        } else {
            return false;
        }
    }

    private void setBlock(@Nullable BiConsumer<BlockPos, BlockState> changedBlocks, WorldGenLevel level, BlockPos pos, BlockState state) {
        if (state.hasProperty(BlockStateProperties.WATERLOGGED) && level.getFluidState(pos).is(Fluids.WATER)) {
            state = state.setValue(BlockStateProperties.WATERLOGGED, true);
        }

        TreeFeature.setBlockKnownShape(level, pos, state);
        if (changedBlocks != null) {
            changedBlocks.accept(pos, state);
        }

    }

    protected boolean isReplaceableByLeaves(WorldGenLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.getMaterial().isReplaceable() || state.is(BlockTags.LEAVES);
    }
    protected boolean isReplaceableByLogs(WorldGenLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.getMaterial().isReplaceable() || isLeavesOrLog(state) || state.getMaterial() == Material.PLANT;
    }
    protected boolean isLeavesOrLog(BlockState state) {return state.is(BlockTags.LOGS) || state.is(BlockTags.LEAVES);}

    protected boolean isAreaBlocked(WorldGenLevel level, BlockPos pos, int trunkHeight, int trunkWidth, int leafStartHeight, int leafWidth) {
        if (pos.getY() >= 1 && pos.getY() + trunkHeight + 1 < level.getHeight()) {
            int width;
            int y;
            int x;
            if (trunkWidth == 1) {
                for(width = 0; width <= 1 + trunkHeight && pos.getY() + width >= 1 && pos.getY() + width < level.getHeight(); ++width) {
                    if (!isReplaceableByLogs(level, pos.above(width)) && !isLeavesOrLog(level.getBlockState(pos.above(width)))) {
                        return true;
                    }
                }
            } else {
                for(width = 0; width <= 1 + trunkHeight && pos.getY() + width >= 1 && pos.getY() + width < level.getHeight(); ++width) {
                    for(y = 0; y < trunkWidth; ++y) {
                        for(x = 0; x < trunkWidth; ++x) {
                            if (!isReplaceableByLogs(level, pos.offset(y, width, x)) && !isLeavesOrLog(level.getBlockState(pos.offset(y, width, x)))) {
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
                        if (!isReplaceableByLeaves(level, pos.offset(x, y, z)) && !isLeavesOrLog(level.getBlockState(pos.offset(x, y, z)))) {
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

    public record PlaceContext(WorldGenLevel level, BlockPos pos, BiConsumer<BlockPos, BlockState> changedLogs, BiConsumer<BlockPos, BlockState> changedLeaves, FoliagePlacer.FoliageSetter foliageSetter, RandomSource random) {}
}