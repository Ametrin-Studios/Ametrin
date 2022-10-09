package com.ametrinstudios.ametrin.world.gen.feature.tree;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * This is an alternative way for tree grower, if possible use the vanilla!
 */
public class CustomTreeGrower extends AbstractTreeGrower {
    protected final Supplier<? extends CustomTreeFeature> tree;

    public CustomTreeGrower(Supplier<? extends CustomTreeFeature> tree) {this.tree = tree;}

    @Override@Nullable
    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(@NotNull RandomSource random, boolean pLargeHive) {return null;}

    @Override @ParametersAreNonnullByDefault
    public boolean growTree(ServerLevel level, ChunkGenerator generator, BlockPos pos, BlockState blockState, RandomSource random) {
        BiConsumer<BlockPos, BlockState> placedLogs = level::setBlockAndUpdate;
        BiConsumer<BlockPos, BlockState> placedLeaves = level::setBlockAndUpdate;
        return tree.get().place(level, random, pos, placedLogs, placedLeaves);
    }
}
