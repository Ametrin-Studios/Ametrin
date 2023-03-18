package com.ametrinstudios.ametrin.world.gen.feature.tree;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * This is an alternative way for tree grower, if possible use the vanilla!
 */
public class CustomTreeGrower extends AbstractTreeGrower {
    protected final Supplier<? extends CustomTreeFeature> tree;

    public CustomTreeGrower(Supplier<? extends CustomTreeFeature> tree) {this.tree = tree;}

    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(@NotNull RandomSource random, boolean pLargeHive) {return null;}

    @Override @ParametersAreNonnullByDefault
    public boolean growTree(ServerLevel level, ChunkGenerator generator, BlockPos pos, BlockState blockState, RandomSource random) {
        /*FoliagePlacer.FoliageSetter foliageplacer$foliagesetter = new FoliagePlacer.FoliageSetter() {
            public void set(BlockPos p_272825_, BlockState p_273311_) {
                set2.add(p_272825_.immutable());
                worldgenlevel.setBlock(p_272825_, p_273311_, 19);
            }

            public boolean isSet(BlockPos p_272999_) {
                return set2.contains(p_272999_);
            }
        };*/

        BiConsumer<BlockPos, BlockState> placedLogs = level::setBlockAndUpdate;
        BiConsumer<BlockPos, BlockState> placedLeaves = level::setBlockAndUpdate;
        return tree.get().place(level, random, pos, placedLogs, placedLeaves, null);
    }
}
