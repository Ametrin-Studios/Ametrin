package com.ametrinstudios.ametrin.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

public class FlammableLeavesBlock extends LeavesBlock {

    public FlammableLeavesBlock(Properties properties) {super(properties);}

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {return !state.getValue(LeavesBlock.WATERLOGGED);}
    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {return state.getValue(LeavesBlock.WATERLOGGED) ? 0 : 30;}
    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {return 60;}
}