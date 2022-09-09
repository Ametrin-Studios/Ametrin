package com.ametrinstudios.ametrin.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class GrowableBushBlock extends BushBlock implements IGrowable {
    public final int GrowRarity;
    public final int BonusDrop;
    private static final VoxelShape SaplingShape = Block.box(3, 0, 3, 13, 8, 13);
    private static final VoxelShape MidGrowthShape = Block.box(1, 0, 1, 15, 16, 15);

    public GrowableBushBlock(int bonusDrop, int growRate, Properties properties) {
        super(properties);
        GrowRarity = growRate;
        BonusDrop = bonusDrop;
        registerDefaultState(stateDefinition.any().setValue(Age, 0));
    }
    @Override @ParametersAreNonnullByDefault
    public @NotNull ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState blockState) {
        return new ItemStack(asItem());
    }
    @Override @ParametersAreNonnullByDefault
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (blockState.getValue(Age) == 0) {
            return SaplingShape;
        } else {
            return blockState.getValue(Age) < MaxAge ? MidGrowthShape : super.getShape(blockState, level, pos, context);
        }
    }

    public boolean isRandomlyTicking(BlockState blockState) {return isSparse(blockState);}

    @Override @ParametersAreNonnullByDefault
    public void randomTick(BlockState blockState, ServerLevel level, BlockPos pos, RandomSource random) {
        int i = blockState.getValue(Age);
        if (i < MaxAge && level.getRawBrightness(pos.above(), 0) >= 9 && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(level, pos, blockState,random.nextInt(GrowRarity) == 0)) {
            level.setBlock(pos, blockState.setValue(Age, i + 1), 2);
            net.minecraftforge.common.ForgeHooks.onCropsGrowPost(level, pos, blockState);
        }
    }


    @Override
    public void onHarvest(BlockState blockState, Level level, BlockPos blockPos) {
        int dropAmount = 1;
        if(BonusDrop > 0){
            dropAmount += level.random.nextInt(BonusDrop);
        }
        popResource(level, blockPos, new ItemStack(asItem(), dropAmount));
        level.playSound(null, blockPos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1, 0.8f + level.random.nextFloat() * 0.4f);
        level.setBlock(blockPos, blockState.setValue(Age, 1), 2);
    }

    @Override @ParametersAreNonnullByDefault
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        boolean isFull = isFull(blockState.getValue(Age));
        if (!isFull && player.getItemInHand(hand).is(Items.BONE_MEAL)) {
            return InteractionResult.PASS;
        } else if (isFull) {
            onHarvest(blockState, level, blockPos);
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return super.use(blockState, level, blockPos, player, hand, blockHitResult);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(Age);
    }
    @Override @ParametersAreNonnullByDefault
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState blockState) {
        if(isFull(blockState)) {return;}
        level.setBlock(pos, blockState.setValue(Age, Math.min(MaxAge, blockState.getValue(Age) + 1)), 2);
    }
}