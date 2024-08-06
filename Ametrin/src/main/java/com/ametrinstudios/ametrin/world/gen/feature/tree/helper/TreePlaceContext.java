package com.ametrinstudios.ametrin.world.gen.feature.tree.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;

import java.util.function.BiConsumer;

public record TreePlaceContext(BlockPos pos, WorldGenLevel level, RandomSource random, BiConsumer<BlockPos, BlockState> changedLogs, BiConsumer<BlockPos, BlockState> changedLeaves, FoliagePlacer.FoliageSetter foliageSetter) {
    public TreePlaceContext at(BlockPos pos){
        return new TreePlaceContext(pos, level(), random(), changedLogs(), changedLeaves(), foliageSetter());
    }
}
