package com.ametrinstudios.ametrin.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import javax.annotation.ParametersAreNonnullByDefault;

public interface IAgeablePlant extends BonemealableBlock{
    int MAX_AGE = 3;
    IntegerProperty AGE = BlockStateProperties.AGE_3;

    void onHarvest(BlockState blockState, Level level, BlockPos blockPos, Player player);
    @Override @ParametersAreNonnullByDefault
    default boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState blockState) {return isSparse(blockState);}
    @Override @ParametersAreNonnullByDefault
    default boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState blockState) {return true;}

    default boolean isSparse(BlockState blockState) {return isSparse(blockState.getValue(AGE));}
    default boolean isSparse(int age) {return age < MAX_AGE;}
    default boolean isFullyAged(BlockState blockState) {return isFullyAged(blockState.getValue(AGE));}
    default boolean isFullyAged(int age) {return age >= MAX_AGE;}
}