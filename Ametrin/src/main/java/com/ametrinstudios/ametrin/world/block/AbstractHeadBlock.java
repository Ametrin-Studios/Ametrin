package com.ametrinstudios.ametrin.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class AbstractHeadBlock extends Block implements Equipable, SimpleWaterloggedBlock {
    public static final BooleanProperty Waterlogged = BlockStateProperties.WATERLOGGED;
    public AbstractHeadBlock(Properties properties) {super(properties);}

    @Override
    public @NotNull FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(Waterlogged) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }
    @Override @ParametersAreNonnullByDefault
    public boolean isPathfindable(BlockState blockState, PathComputationType pathType) {return false;}

    @Override @ParametersAreNonnullByDefault
    public @NotNull VoxelShape getOcclusionShape(BlockState blockState, BlockGetter level, BlockPos pos) {return Shapes.empty();}

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {return EquipmentSlot.HEAD;}
}