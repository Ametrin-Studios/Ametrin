package com.ametrinstudios.ametrin.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.common.CommonHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

public class AgeableDoublePlantBlock extends SimpleDoublePlantBlock implements BonemealableBlock, IAgeablePlant {
    public final int GrowRarity;
    public final int BonusDrop;
    public final Supplier<Item> item;

    public AgeableDoublePlantBlock(int growRarity, int bonusDrop, Supplier<Item> item, Properties properties) {
        super(properties);
        this.item = item;
        GrowRarity = growRarity;
        BonusDrop = bonusDrop;
        registerDefaultState(stateDefinition.any().setValue(AGE, 0).setValue(HALF, DoubleBlockHalf.LOWER));
    }

    public void onHarvest(BlockState blockState, Level level, BlockPos pos, Player player){
        int dropAmount = 1;
        if(BonusDrop > 0){
            dropAmount += level.random.nextInt(BonusDrop);
        }
        popResource(level, pos, new ItemStack(asItem(), dropAmount));
        level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1, 0.8f + level.random.nextFloat() * 0.4f);
        setAgeInLevel(1, blockState, level, pos, player);
    }

    @Override @NotNull @ParametersAreNonnullByDefault
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(itemStack.is(Items.BONE_MEAL) && isSparse(blockState)){
            return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }
        if (itemStack.is(Items.BOWL) && isFullyAged(blockState)) {
            onHarvest(blockState, level, pos, player);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return super.useItemOn(itemStack, blockState, level, pos, player, hand, hitResult);
    }


    @Override @ParametersAreNonnullByDefault
    public void randomTick(BlockState currentState, ServerLevel level, BlockPos pos, RandomSource random) {
        if(currentState.getValue(HALF) == DoubleBlockHalf.UPPER) { return; }

        int currentAge = currentState.getValue(AGE);
        if (isSparse(currentAge) && CommonHooks.canCropGrow(level, pos, currentState,random.nextInt(5) == 0)) {
            setAgeInLevel(currentAge+1, currentState, level, pos, true);
        }
    }

    protected void setAgeInLevel(int age, BlockState blockState, Level level, BlockPos pos, Player player){
        setAgeSingle(age, blockState, level, pos, player);

        var otherPos = (blockState.getValue(HALF) == DoubleBlockHalf.LOWER) ? pos.above() : pos.below();
        setAgeSingle(age, level.getBlockState(otherPos), level, otherPos, player);
    }
    private void setAgeSingle(int age, BlockState blockState, Level level, BlockPos pos, Player player) {
        var newState = blockState.setValue(AGE, age);
        level.setBlock(pos, newState, 2);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, newState));
    }

    protected void setAgeInLevel(int age, BlockState blockState, Level level, BlockPos pos, boolean triggerEvents){
        setAgeSingle(age, blockState, level, pos, triggerEvents, triggerEvents);

        var otherPos = (blockState.getValue(HALF) == DoubleBlockHalf.LOWER) ? pos.above() : pos.below();
        setAgeSingle(age, level.getBlockState(otherPos), level, otherPos, triggerEvents, triggerEvents);
    }
    private void setAgeSingle(int age, BlockState blockState, Level level, BlockPos pos, boolean fireChangeEvent, boolean fireGrowHook) {
        var newState = blockState.setValue(AGE, age);
        level.setBlock(pos, newState, 2);
        if(fireGrowHook) {
            CommonHooks.fireCropGrowPost(level, pos, newState);
        }
        if(fireChangeEvent) {
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newState));
        }
    }

    @ParametersAreNonnullByDefault
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState blockState) {
        if(isFullyAged(blockState)) {return;}
        setAgeInLevel(blockState.getValue(AGE) + 1, blockState, level, pos, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> stateBuilder) {
        super.createBlockStateDefinition(stateBuilder);
        stateBuilder.add(AGE);
    }

    @Override @ParametersAreNonnullByDefault
    public @NotNull ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) { return new ItemStack(item.get()); }

    @Override @ParametersAreNonnullByDefault
    public boolean isRandomlyTicking(BlockState blockState) { return isSparse(blockState); }
}