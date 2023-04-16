package com.ametrinstudios.ametrin.world.gen.feature.tree;

import com.ametrinstudios.ametrin.world.gen.feature.tree.helper.TreePlaceContext;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
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
        return place(new TreePlaceContext(pos, level, random, changedLogs, changedLeaves, foliageSetter));
    }

    public abstract boolean place(TreePlaceContext context);
}