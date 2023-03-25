package com.ametrinstudios.ametrin.world;

import com.ametrinstudios.ametrin.util.mixin.IMixinBlockBehaviorProperties;
import com.ametrinstudios.ametrin.world.block.AgeableBushBlock;
import com.ametrinstudios.ametrin.world.gen.feature.tree.CustomTreeFeature;
import com.ametrinstudios.ametrin.world.gen.feature.tree.CustomTreeGrower;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;


@SuppressWarnings("unused") @Deprecated(forRemoval = true)
public abstract class BlockRegistry {
    protected static BlockBehaviour.Properties properties(Block parent) {return BlockBehaviour.Properties.copy(parent);}
    protected static BlockBehaviour.Properties properties(Material material) {return BlockBehaviour.Properties.of(material);}
    protected static BlockBehaviour.Properties properties(BlockBehaviour.Properties properties) {return ((IMixinBlockBehaviorProperties) properties).copy();}

    protected static Supplier<StairBlock> stair(StairBlock.Properties properties, Supplier<BlockState> base) {return ()-> new StairBlock(base, properties);}
    protected static Supplier<StairBlock> stair(Block parent) {return stair(properties(parent), parent::defaultBlockState);}

    protected static Supplier<SaplingBlock> sapling(Supplier<? extends CustomTreeFeature> tree) {return ()-> new SaplingBlock(new CustomTreeGrower(tree), properties(Blocks.OAK_SAPLING));}
    protected static Supplier<FlowerPotBlock> potted(Supplier<Block> main) {return ()-> new FlowerPotBlock(() -> (FlowerPotBlock)Blocks.FLOWER_POT, main, properties(Blocks.POTTED_OAK_SAPLING));}
    protected static Supplier<AgeableBushBlock> bush(int bonusDrop, int growRarity) {return ()-> new AgeableBushBlock(bonusDrop, growRarity, properties(Blocks.SWEET_BERRY_BUSH));}

    protected static Supplier<ButtonBlock> woodenButton() {return woodenButton(BlockSetType.OAK);}
    protected static Supplier<ButtonBlock> woodenButton(BlockSetType type) {return button(type, 30, true);}
    protected static Supplier<ButtonBlock> stoneButton() {return stoneButton(BlockSetType.STONE);}
    protected static Supplier<ButtonBlock> stoneButton(BlockSetType type) {return button(type, 20, false);}
    protected static Supplier<ButtonBlock> button(BlockSetType type, int ticksStayPressed, boolean arrowsCanPress){
        return ()-> new ButtonBlock(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().strength(0.5F), type, ticksStayPressed, arrowsCanPress);
    }

    protected static Supplier<FenceGateBlock> fenceGate(BlockBehaviour.Properties properties) {return fenceGate(properties, WoodType.OAK);}
    protected static Supplier<FenceGateBlock> fenceGate(BlockBehaviour.Properties properties, WoodType type){
        return ()-> new FenceGateBlock(properties, type);
    }
    protected static Supplier<DoorBlock> door(BlockBehaviour.Properties properties) {return door(properties, BlockSetType.OAK);}
    protected static Supplier<DoorBlock> door(BlockBehaviour.Properties properties, BlockSetType type){
        return ()-> new DoorBlock(properties, type);
    }
    protected static Supplier<TrapDoorBlock> trapDoor(BlockBehaviour.Properties properties) {return trapDoor(properties, BlockSetType.OAK);}
    protected static Supplier<TrapDoorBlock> trapDoor(BlockBehaviour.Properties properties, BlockSetType type){
        return ()-> new TrapDoorBlock(properties, type);
    }

    protected static Supplier<PressurePlateBlock> woodenPressurePlate(Material material, MaterialColor color, BlockSetType type) {return pressurePlate(PressurePlateBlock.Sensitivity.EVERYTHING, material, color, type);}
    protected static Supplier<PressurePlateBlock> stonePressurePlate(Material material, MaterialColor color, BlockSetType type) {return pressurePlate(PressurePlateBlock.Sensitivity.MOBS, material, color, type);}
    protected static Supplier<PressurePlateBlock> woodenPressurePlate(Block base, BlockSetType type) {return pressurePlate(PressurePlateBlock.Sensitivity.EVERYTHING, base, type);}
    protected static Supplier<PressurePlateBlock> stonePressurePlate(Block base, BlockSetType type) {return pressurePlate(PressurePlateBlock.Sensitivity.MOBS, base, type);}
    protected static Supplier<PressurePlateBlock> pressurePlate(PressurePlateBlock.Sensitivity sensitivity, Block base, BlockSetType type){
        return pressurePlate(sensitivity, base.defaultBlockState().getMaterial(), base.defaultMaterialColor(), type);
    }
    protected static Supplier<PressurePlateBlock> pressurePlate(PressurePlateBlock.Sensitivity sensitivity, Material material, MaterialColor color, BlockSetType type){
        return pressurePlate(sensitivity, BlockBehaviour.Properties.of(material, color).noCollission().strength(0.5F), type);
    }
    protected static Supplier<PressurePlateBlock> pressurePlate(PressurePlateBlock.Sensitivity sensitivity, BlockBehaviour.Properties properties, BlockSetType type){
        return ()-> new PressurePlateBlock(sensitivity, properties, type);
    }

    protected static ToIntFunction<BlockState> litEmission(int lightLevel) {return (state)-> state.getValue(BlockStateProperties.LIT) ? lightLevel : 0;}
    protected static ToIntFunction<BlockState> litEmission(ToIntFunction<BlockState> lightLevel) {return (state)-> state.getValue(BlockStateProperties.LIT) ? lightLevel.applyAsInt(state) : 0;}
    protected static ToIntFunction<BlockState> emission(int lightLevel) {return (state)-> lightLevel;}
}