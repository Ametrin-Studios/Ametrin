package com.ametrinstudios.ametrin.world.block;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jspecify.annotations.Nullable;

public class HorizontalRotatedSlabBlock extends SlabBlock {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    public HorizontalRotatedSlabBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        var placed = super.getStateForPlacement(context);
        if (placed == null) return null;

        var pos = context.getClickedPos();
        var replacedBlockState = context.getLevel().getBlockState(pos);
        if (replacedBlockState.is(this)) {
            return placed.setValue(FACING, replacedBlockState.getValue(FACING));
        }

        return placed.setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }
}
