package com.ametrinstudios.ametrin.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;

public class FlammableFenceBlock extends FenceBlock {
    public FlammableFenceBlock(Properties properties) {super(properties);}

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {return true;}
    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {return 5;}
    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {return 20;}
}
