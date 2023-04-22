package com.ametrinstudios.ametrin.world.gen.feature.tree;

import com.ametrinstudios.ametrin.world.gen.feature.tree.helper.TreePlaceContext;
import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * This is an alternative way for tree grower, if possible use the vanilla!
 */
public class CustomTreeGrower extends AbstractTreeGrower {
    protected final Supplier<? extends CustomTreeFeature> Tree;

    public CustomTreeGrower(Supplier<? extends CustomTreeFeature> tree) {Tree = tree;}

    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(@NotNull RandomSource random, boolean hasFlowers) {return null;}

    @Override @ParametersAreNonnullByDefault
    public boolean growTree(ServerLevel level, ChunkGenerator generator, BlockPos pos, BlockState blockState, RandomSource random) {
        final Set<BlockPos> foliage = Sets.newHashSet();

        BiConsumer<BlockPos, BlockState> placedLogs = (blockPos, state) -> TreeFeature.setBlockKnownShape(level, blockPos, state);
        BiConsumer<BlockPos, BlockState> placedLeaves = (blockPos, state) -> TreeFeature.setBlockKnownShape(level, blockPos, state);

        var foliageSetter = new FoliagePlacer.FoliageSetter() {
            public void set(BlockPos blockPos, BlockState state) {
                foliage.add(blockPos.immutable());
                TreeFeature.setBlockKnownShape(level, blockPos, state);
            }
            public boolean isSet(BlockPos blockPos) {
                return foliage.contains(blockPos);
            }
        };

        return Tree.get().place(new TreePlaceContext(pos, level, random, placedLogs, placedLeaves, foliageSetter));
    }
}
