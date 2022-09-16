package com.ametrinstudios.ametrin.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import javax.annotation.ParametersAreNonnullByDefault;

public interface IAgeablePlant extends BonemealableBlock{
    int MaxAge = 3;
    IntegerProperty Age = BlockStateProperties.AGE_3;

    void onHarvest(BlockState blockState, Level level, BlockPos blockPos);

    @Override @ParametersAreNonnullByDefault
    default boolean isValidBonemealTarget(BlockGetter blockGetter, BlockPos pos, BlockState blockState, boolean bool) {return isSparse(blockState);}
    @Override @ParametersAreNonnullByDefault
    default boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState blockState) {return true;}

    default boolean isSparse(BlockState blockState) {return isSparse(blockState.getValue(Age));}
    default boolean isSparse(int age) {return age < MaxAge;}
    default boolean isFull(BlockState blockState) {return isFull(blockState.getValue(Age));}
    default boolean isFull(int age) {return age >= MaxAge;}
}