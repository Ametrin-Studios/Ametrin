package com.ametrinstudios.ametrin.word.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class CustomHeadBlock extends AmAbstractHeadBlock {
    private static final int MaxRotations = 16;
    public static final IntegerProperty Rotation = BlockStateProperties.ROTATION_16;
    protected static final VoxelShape Shape = Block.box(4d, 0d, 4d, 12d, 8d, 12d);

    public CustomHeadBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(Rotation, 0).setValue(Waterlogged, false));
    }

    @Override @ParametersAreNonnullByDefault
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos pos, CollisionContext context) {return Shape;}

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(Rotation, Mth.floor((context.getRotation() * MaxRotations/360f) + 0.5) & 15).setValue(Waterlogged, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {stateBuilder.add(Rotation, Waterlogged);}
}