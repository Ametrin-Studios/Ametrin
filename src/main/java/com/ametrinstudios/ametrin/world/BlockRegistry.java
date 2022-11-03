package com.ametrinstudios.ametrin.world;

import com.ametrinstudios.ametrin.mixin.util.IBlockBehaviorPropertiesMixin;
import com.ametrinstudios.ametrin.world.block.AgeableBushBlock;
import com.ametrinstudios.ametrin.world.block.FlammableLeavesBlock;
import com.ametrinstudios.ametrin.world.block.FlammableLogBlock;
import com.ametrinstudios.ametrin.world.block.FlammablePlankBlock;
import com.ametrinstudios.ametrin.world.gen.feature.tree.CustomTreeFeature;
import com.ametrinstudios.ametrin.world.gen.feature.tree.CustomTreeGrower;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public abstract class BlockRegistry {
    protected static BlockBehaviour.Properties properties(Block parent) {return BlockBehaviour.Properties.copy(parent);}
    protected static BlockBehaviour.Properties properties(Material material) {return BlockBehaviour.Properties.of(material);}
    protected static BlockBehaviour.Properties properties(BlockBehaviour.Properties properties) {return ((IBlockBehaviorPropertiesMixin) properties).copy();}

    @Deprecated(forRemoval = true) protected static Supplier<Block> block(BlockBehaviour.Properties properties) {return ()-> new Block(properties);}
    @Deprecated(forRemoval = true) protected static Supplier<Block> block(Block parent) {return block(properties(parent));}
    @Deprecated(forRemoval = true) protected static Supplier<DropExperienceBlock> xpDroppingBlock(IntProvider xp, BlockBehaviour.Properties properties) {return ()-> new DropExperienceBlock(properties, xp);}
    protected static Supplier<StairBlock> stair(StairBlock.Properties properties, Supplier<BlockState> base) {return ()-> new StairBlock(base, properties);}
    protected static Supplier<StairBlock> stair(Block parent) {return stair(properties(parent), parent::defaultBlockState);}
    @Deprecated(forRemoval = true) protected static Supplier<SlabBlock> slab(SlabBlock.Properties properties) {return ()-> new SlabBlock(properties);}
    @Deprecated(forRemoval = true) protected static Supplier<SlabBlock> slab(Block parent) {return slab(properties(parent));}
    @Deprecated(forRemoval = true) protected static Supplier<WallBlock> wall(WallBlock.Properties properties) {return ()-> new WallBlock(properties);}
    @Deprecated(forRemoval = true) protected static Supplier<WallBlock> wall(Block parent) {return wall(properties(parent));}
    @Deprecated(forRemoval = true) protected static Supplier<FenceBlock> fence(FenceBlock.Properties properties) {return ()-> new FenceBlock(properties);}
    @Deprecated(forRemoval = true) protected static Supplier<FenceBlock> fence(Block parent) {return fence(properties(parent));}
    @Deprecated(forRemoval = true) protected static Supplier<FenceGateBlock> fenceGate(FenceGateBlock.Properties properties) {return ()-> new FenceGateBlock(properties);}
    @Deprecated(forRemoval = true) protected static Supplier<FenceGateBlock> fenceGate(Block parent) {return fenceGate(properties(parent));}
    @Deprecated(forRemoval = true) protected static Supplier<PressurePlateBlock> pressurePlate(PressurePlateBlock.Sensitivity sensitivity, BlockBehaviour.Properties properties) {return ()-> new PressurePlateBlock(sensitivity, properties);}
    @Deprecated(forRemoval = true) protected static Supplier<PressurePlateBlock> pressurePlate(PressurePlateBlock.Sensitivity sensitivity, Block parent) {return (pressurePlate(sensitivity, properties(parent)));}
    @Deprecated(forRemoval = true) protected static Supplier<WoodButtonBlock> woodButton(BlockBehaviour.Properties properties) {return ()-> new WoodButtonBlock(properties);}
    @Deprecated(forRemoval = true) protected static Supplier<WoodButtonBlock> woodButton(Block parent) {return woodButton(properties(parent));}
    @Deprecated(forRemoval = true) protected static Supplier<StoneButtonBlock> stoneButton(BlockBehaviour.Properties properties) {return ()-> new StoneButtonBlock(properties);}
    @Deprecated(forRemoval = true) protected static Supplier<StoneButtonBlock> stoneButton(Block parent) {return stoneButton(properties(parent));}
    @Deprecated(forRemoval = true) protected static Supplier<FlammableLeavesBlock> leave(BlockBehaviour.Properties properties) {return ()-> new FlammableLeavesBlock(properties);}
    @Deprecated(forRemoval = true) protected static Supplier<FlammableLeavesBlock> leave(Block parent) {return leave(properties(parent));}
    protected static Supplier<SaplingBlock> sapling(Supplier<? extends CustomTreeFeature> tree) {return ()-> new SaplingBlock(new CustomTreeGrower(tree), properties(Blocks.OAK_SAPLING));}
    protected static Supplier<FlowerPotBlock> potted(Supplier<Block> main) {return ()-> new FlowerPotBlock(() -> (FlowerPotBlock)Blocks.FLOWER_POT, main, properties(Blocks.POTTED_OAK_SAPLING));}
    @Deprecated(forRemoval = true) protected static Supplier<DoorBlock> door(BlockBehaviour.Properties properties) {return ()-> new DoorBlock(properties);}
    @Deprecated(forRemoval = true) protected static Supplier<DoorBlock> door(Block parent) {return door(properties(parent));}
    @Deprecated(forRemoval = true) protected static Supplier<TrapDoorBlock> trapdoor(BlockBehaviour.Properties properties) {return ()-> new TrapDoorBlock(properties);}
    @Deprecated(forRemoval = true) protected static Supplier<TrapDoorBlock> trapdoor(Block parent) {return trapdoor(properties(parent));}
    @Deprecated(forRemoval = true) protected static Supplier<RotatedPillarBlock> rotatedPillar(BlockBehaviour.Properties properties) {return ()-> new RotatedPillarBlock(properties);}
    @Deprecated(forRemoval = true) protected static Supplier<RotatedPillarBlock> rotatedPillar(Block parent) {return rotatedPillar(properties(parent));}
    @Deprecated(forRemoval = true) protected static Supplier<FlammablePlankBlock> plank(BlockBehaviour.Properties properties) {return ()-> new FlammablePlankBlock(properties);}
    @Deprecated(forRemoval = true) protected static Supplier<FlammablePlankBlock> plank(Block parent) {return plank(properties(parent));}
    @Deprecated(forRemoval = true) protected static Supplier<FlammableLogBlock> flammableLog(BlockBehaviour.Properties properties) {return ()-> new FlammableLogBlock(properties);}
    @Deprecated(forRemoval = true) protected static Supplier<FlammableLogBlock> flammableLog(Block parent) {return flammableLog(properties(parent));}
    protected static Supplier<AgeableBushBlock> bush(int bonusDrop, int growRarity) {return ()-> new AgeableBushBlock(bonusDrop, growRarity, properties(Blocks.SWEET_BERRY_BUSH));}

    protected static ToIntFunction<BlockState> litEmission(int lightLevel) {return (state)-> state.getValue(BlockStateProperties.LIT) ? lightLevel : 0;}
    protected static ToIntFunction<BlockState> litEmission(ToIntFunction<BlockState> lightLevel) {return (state)-> state.getValue(BlockStateProperties.LIT) ? lightLevel.applyAsInt(state) : 0;}
    protected static ToIntFunction<BlockState> emission(int lightLevel) {return (state)-> lightLevel;}
}