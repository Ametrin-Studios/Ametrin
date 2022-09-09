package com.ametrinstudios.ametrin.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

public class GrowableDoublePlantBlock extends SimpleDoublePlantBlock implements BonemealableBlock, IGrowable{
    public final int GrowRarity;
    public final int BonusDrop;
    public final Supplier<Item> item;

    public GrowableDoublePlantBlock(int growRarity, int bonusDrop, Supplier<Item> item, Properties properties) {
        super(properties);
        this.item = item;
        GrowRarity = growRarity;
        BonusDrop = bonusDrop;
        registerDefaultState(stateDefinition.any().setValue(Age, 0).setValue(HALF, DoubleBlockHalf.LOWER));
    }

    public void onHarvest(BlockState currentState, Level level, BlockPos pos){
        int dropAmount = 1;
        if(BonusDrop > 0){
            dropAmount += level.random.nextInt(BonusDrop);
        }
        popResource(level, pos, new ItemStack(asItem(), dropAmount));
        level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1, 0.8f + level.random.nextFloat() * 0.4f);
        setAgeInLevel(1, currentState, level, pos);
    }

    @Override @ParametersAreNonnullByDefault
    public @NotNull InteractionResult use(BlockState currentState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        boolean isFull = isFull(currentState);
        if (!isFull && player.getItemInHand(hand).is(Items.BONE_MEAL)) {
            return InteractionResult.PASS;
        } else if (isFull && player.getItemInHand(hand).is(Items.BOWL)) {
            onHarvest(currentState, level, pos);
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return super.use(currentState, level, pos, player, hand, hitResult);
        }
    }

    @Override @ParametersAreNonnullByDefault
    public void randomTick(BlockState currentState, ServerLevel level, BlockPos pos, RandomSource random) {
        if(currentState.getValue(HALF) == DoubleBlockHalf.UPPER) {return;}

        int currentAge = currentState.getValue(Age);
        if (isSparse(currentAge) && ForgeHooks.onCropsGrowPre(level, pos, currentState,random.nextInt(5) == 0)) {
            setAgeInLevel(currentAge+1, currentState, level, pos);
            ForgeHooks.onCropsGrowPost(level, pos, currentState);
        }
    }

    protected void setAgeInLevel(int age, BlockState blockState, Level level, BlockPos pos){
        level.setBlock(pos, blockState.setValue(Age, age), 2);
        pos = (blockState.getValue(HALF) == DoubleBlockHalf.LOWER) ? pos.above() : pos.below();
        level.setBlock(pos, level.getBlockState(pos).setValue(Age, age), 2);
    }

    @ParametersAreNonnullByDefault
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState blockState) {
        if(isFull(blockState)) {return;}
        setAgeInLevel(blockState.getValue(Age) + 1, blockState, level, pos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> stateBuilder) {
        super.createBlockStateDefinition(stateBuilder);
        stateBuilder.add(Age);
    }

    @Override public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {return new ItemStack(item.get());}

    @Override @ParametersAreNonnullByDefault
    public boolean isRandomlyTicking(BlockState blockState) {return isSparse(blockState);}
}