package com.ametrinstudios.ametrin.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealSource;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.Tags;

public class AgeableBushBlock extends BushBlock implements IAgeablePlant {
    public final int GrowRarity;
    public final int BonusDrop;
    private static final VoxelShape SaplingShape = Block.box(3, 0, 3, 13, 8, 13);
    private static final VoxelShape MidGrowthShape = Block.box(1, 0, 1, 15, 16, 15);

    public AgeableBushBlock(int bonusDrop, int growRate, Properties properties) {
        super(properties);
        GrowRarity = growRate;
        BonusDrop = bonusDrop;
        registerDefaultState(stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
        return new ItemStack(asItem());
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (blockState.getValue(AGE) == 0) {
            return SaplingShape;
        } else {
            return blockState.getValue(AGE) < MAX_AGE ? MidGrowthShape : super.getShape(blockState, level, pos, context);
        }
    }

    public boolean isRandomlyTicking(BlockState blockState) {return isSparse(blockState);}

    @Override
    public void randomTick(BlockState blockState, ServerLevel level, BlockPos pos, RandomSource random) {
        int i = blockState.getValue(AGE);
        if (i < MAX_AGE && level.getRawBrightness(pos.above(), 0) >= 9 && CommonHooks.canCropGrow(level, pos, blockState, random.nextInt(GrowRarity) == 0)) {
            var newState = blockState.setValue(AGE, i + 1);
            level.setBlock(pos, newState, 2);
            CommonHooks.fireCropGrowPost(level, pos, blockState);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newState));
        }
    }

    public void onHarvest(BlockState blockState, Level level, BlockPos pos, Player player) {
        int dropAmount = 1;
        if(BonusDrop > 0){
            dropAmount += level.getRandom().nextInt(BonusDrop);
        }
        popResource(level, pos, new ItemStack(asItem(), dropAmount));
        level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1, 0.8f + level.getRandom().nextFloat() * 0.4f);
        var newState = blockState.setValue(AGE, 1);
        level.setBlock(pos, newState, 2);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, newState));
    }

    @Override
    protected InteractionResult  useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return isSparse(blockState) && itemStack.is(Tags.Items.FERTILIZERS)
                ? InteractionResult.PASS
                : super.useItemOn(itemStack, blockState, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(isSparse(blockState)){
            return super.useWithoutItem(blockState, level, pos, player, hitResult);
        }
        onHarvest(blockState, level, pos, player);
        return InteractionResult.SUCCESS;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(AGE);
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState blockState, BonemealSource source) {
        if(isFullyAged(blockState)) {return;}
        level.setBlock(pos, blockState.setValue(AGE, Math.min(MAX_AGE, blockState.getValue(AGE) + 1)), UPDATE_CLIENTS);
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {return true;}
    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {return 60;}
    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {return 100;}
}