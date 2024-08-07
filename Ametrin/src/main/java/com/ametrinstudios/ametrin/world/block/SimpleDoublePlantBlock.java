package com.ametrinstudios.ametrin.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class SimpleDoublePlantBlock extends DoublePlantBlock {
    protected static final VoxelShape LOWER_SHAPE = Block.box(2d, 0d, 2d, 14d, 16d, 14d);
    protected static final VoxelShape UPPER_SHAPE = Block.box(2d, 0d, 2d, 14d, 8d, 14d);

    public SimpleDoublePlantBlock(Properties properties) {super(properties);}

    @Override @ParametersAreNonnullByDefault
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos pos, CollisionContext context) {
        return blockState.getValue(HALF) == DoubleBlockHalf.UPPER ? UPPER_SHAPE : LOWER_SHAPE;
    }

    @Override @ParametersAreNonnullByDefault
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return true; }

    @Override @ParametersAreNonnullByDefault
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return 60; }

    @Override @ParametersAreNonnullByDefault
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return 100; }
}