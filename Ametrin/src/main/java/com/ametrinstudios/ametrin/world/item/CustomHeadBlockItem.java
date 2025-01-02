package com.ametrinstudios.ametrin.world.item;

import com.ametrinstudios.ametrin.world.block.CustomHeadBlock;
import com.ametrinstudios.ametrin.world.block.CustomWallHeadBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class CustomHeadBlockItem extends BlockItem {
    protected final Supplier<CustomHeadBlock> block;
    protected final Supplier<CustomWallHeadBlock> wallBlock;

    public CustomHeadBlockItem(Supplier<CustomHeadBlock> block, Supplier<CustomWallHeadBlock> wallBlock, Properties properties) {
        super(block.get(), properties);
        this.block = block;
        this.wallBlock = wallBlock;
    }

    @Override
    @Nullable
    protected BlockState getPlacementState(@NotNull BlockPlaceContext context) {
        BlockState blockState;
        if (context.getClickedFace() == Direction.DOWN) {
            return null;
        } else if (context.getClickedFace() == Direction.UP) {
            blockState = getBlock().getStateForPlacement(context);
        } else {
            blockState = getWallBlock().getStateForPlacement(context);
        }

        return blockState != null && canPlace(context, blockState) ? blockState : null;
    }

    @Override
    public @NotNull Block getBlock() {
        return block.get();
    }

    public @NotNull Block getWallBlock() {
        return wallBlock.get();
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return true;
    }
}