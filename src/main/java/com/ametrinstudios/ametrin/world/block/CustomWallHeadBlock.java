package com.ametrinstudios.ametrin.world.block;

import com.ametrinstudios.ametrin.world.item.CustomHeadBlockItem;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.function.Supplier;

public class CustomWallHeadBlock extends AmAbstractHeadBlock {
    public static final DirectionProperty Facing = BlockStateProperties.HORIZONTAL_FACING;

    protected final Supplier<CustomHeadBlockItem> item;

    private static final Map<Direction, VoxelShape> AABBS = Maps.newEnumMap(ImmutableMap.of(
            Direction.NORTH, Block.box(4, 4, 8, 12, 12, 16),
            Direction.SOUTH, Block.box(4, 4, 0, 12, 12, 8),
            Direction.WEST, Block.box(8, 4, 4, 16, 12, 12),
            Direction.EAST, Block.box(0, 4, 4, 8, 12, 12)));

    public CustomWallHeadBlock(Supplier<CustomHeadBlockItem> item, Properties properties) {
        super(properties);
        this.item = item;
        registerDefaultState(stateDefinition.any().setValue(Facing, Direction.NORTH).setValue(Waterlogged, false));
    }

    @Override @ParametersAreNonnullByDefault
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos pos, CollisionContext context){
        return AABBS.get(blockState.getValue(Facing));
    }

    @Override
    public @NotNull Item asItem() {return item.get();}

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(Facing, context.getClickedFace()).setValue(Waterlogged, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {stateBuilder.add(Facing, Waterlogged);}
}