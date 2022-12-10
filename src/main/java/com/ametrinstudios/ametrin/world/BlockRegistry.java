package com.ametrinstudios.ametrin.world;

import com.ametrinstudios.ametrin.util.mixin.IMixinBlockBehaviorProperties;
import com.ametrinstudios.ametrin.world.block.AgeableBushBlock;
import com.ametrinstudios.ametrin.world.block.FlammableStairPlankBlock;
import com.ametrinstudios.ametrin.world.gen.feature.tree.CustomTreeFeature;
import com.ametrinstudios.ametrin.world.gen.feature.tree.CustomTreeGrower;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

@SuppressWarnings("unused")
public abstract class BlockRegistry {
    protected static BlockBehaviour.Properties properties(Block parent) {return BlockBehaviour.Properties.copy(parent);}
    protected static BlockBehaviour.Properties properties(Material material) {return BlockBehaviour.Properties.of(material);}
    protected static BlockBehaviour.Properties properties(BlockBehaviour.Properties properties) {return ((IMixinBlockBehaviorProperties) properties).copy();}

    protected static Supplier<StairBlock> stair(StairBlock.Properties properties, Supplier<BlockState> base) {return ()-> new StairBlock(base, properties);}
    protected static Supplier<StairBlock> stair(Block parent) {return stair(properties(parent), parent::defaultBlockState);}
    @Deprecated(forRemoval = true) protected static Supplier<FlammableStairPlankBlock> flammableStair(FlammableStairPlankBlock.Properties properties, Supplier<BlockState> base) {return ()-> new FlammableStairPlankBlock(base, properties);}
    @Deprecated(forRemoval = true) protected static Supplier<FlammableStairPlankBlock> flammableStair(Block parent) {return flammableStair(properties(parent), parent::defaultBlockState);}
    protected static Supplier<SaplingBlock> sapling(Supplier<? extends CustomTreeFeature> tree) {return ()-> new SaplingBlock(new CustomTreeGrower(tree), properties(Blocks.OAK_SAPLING));}
    protected static Supplier<FlowerPotBlock> potted(Supplier<Block> main) {return ()-> new FlowerPotBlock(() -> (FlowerPotBlock)Blocks.FLOWER_POT, main, properties(Blocks.POTTED_OAK_SAPLING));}
    protected static Supplier<AgeableBushBlock> bush(int bonusDrop, int growRarity) {return ()-> new AgeableBushBlock(bonusDrop, growRarity, properties(Blocks.SWEET_BERRY_BUSH));}

    protected static Supplier<ButtonBlock> woodenButton() {return woodenButton(SoundType.WOOD, SoundEvents.WOODEN_BUTTON_CLICK_OFF, SoundEvents.WOODEN_BUTTON_CLICK_ON);}
    protected static Supplier<ButtonBlock> woodenButton(SoundType soundType, SoundEvent soundOffEvent, SoundEvent soundOnEvent) {return button(30, true, soundType, soundOffEvent, soundOnEvent);}
    protected static Supplier<ButtonBlock> stoneButton() {return button(20, false, SoundType.STONE, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON);}
    protected static Supplier<ButtonBlock> button(int ticksStayPressed, boolean arrowsCanPress, SoundType soundType, SoundEvent soundOffEvent, SoundEvent soundOnEvent){
        return ()-> new ButtonBlock(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(soundType), ticksStayPressed, arrowsCanPress, soundOffEvent, soundOnEvent);
    }

    protected static ToIntFunction<BlockState> litEmission(int lightLevel) {return (state)-> state.getValue(BlockStateProperties.LIT) ? lightLevel : 0;}
    protected static ToIntFunction<BlockState> litEmission(ToIntFunction<BlockState> lightLevel) {return (state)-> state.getValue(BlockStateProperties.LIT) ? lightLevel.applyAsInt(state) : 0;}
    protected static ToIntFunction<BlockState> emission(int lightLevel) {return (state)-> lightLevel;}
}