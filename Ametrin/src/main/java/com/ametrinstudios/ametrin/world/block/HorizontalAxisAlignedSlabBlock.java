package com.ametrinstudios.ametrin.world.block;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jspecify.annotations.Nullable;

public class HorizontalAxisAlignedSlabBlock extends SlabBlock {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;

    public HorizontalAxisAlignedSlabBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(AXIS, Direction.Axis.X));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        var placed = super.getStateForPlacement(context);
        if (placed == null) return null;

        var pos = context.getClickedPos();
        var replacedBlockState = context.getLevel().getBlockState(pos);
        if (replacedBlockState.is(this)) {
            return placed.setValue(AXIS, replacedBlockState.getValue(AXIS));
        }

        return placed.setValue(AXIS, context.getHorizontalDirection().getAxis());
    }

    @Override
    protected BlockState rotate(BlockState blockState, Rotation rotation) {
        return switch (rotation) {
            case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> switch (blockState.getValue(AXIS)) {
                case Z -> blockState.setValue(AXIS, Direction.Axis.X);
                case X -> blockState.setValue(AXIS, Direction.Axis.Z);
                default -> blockState;
            };
            default -> blockState;
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AXIS);
    }
}
