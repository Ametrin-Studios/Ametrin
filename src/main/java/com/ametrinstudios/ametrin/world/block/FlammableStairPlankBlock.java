package com.ametrinstudios.ametrin.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class FlammableStairPlankBlock extends StairBlock {
    public FlammableStairPlankBlock(Supplier<BlockState> modelState, Properties properties) {super(modelState, properties);}

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {return true;}
    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {return 5;}
    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {return 20;}
}
