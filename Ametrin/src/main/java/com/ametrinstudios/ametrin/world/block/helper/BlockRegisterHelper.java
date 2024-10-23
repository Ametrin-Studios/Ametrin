package com.ametrinstudios.ametrin.world.block.helper;

import com.ametrinstudios.ametrin.world.block.AgeableBushBlock;
import com.ametrinstudios.ametrin.world.block.PortalBlock;
import com.ametrinstudios.ametrin.world.dimension.portal.PortalData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static com.ametrinstudios.ametrin.world.block.helper.BlockBehaviourPropertiesHelper.copyProperties;

@SuppressWarnings("unused")
public final class BlockRegisterHelper {
    private BlockRegisterHelper() {}

    //return an instance instead of a supplier, let the consumer wrap it, this makes them more robust against cyclic references

    public static Function<BlockBehaviour.Properties, PortalBlock> portalBlock(PortalData data, int lightLevel){
        return portalBlock(data, SoundType.GLASS, lightLevel);
    }

    public static Function<BlockBehaviour.Properties, PortalBlock> portalBlock(PortalData data, SoundType soundType, int lightLevel) {
        return properties -> new PortalBlock(data, properties.strength(-1).noCollission().lightLevel((state) -> lightLevel).noLootTable().randomTicks().sound(soundType));
    }

    public static StairBlock stair(Supplier<Block> parent) {
        return stair(parent.get());
    }
    public static StairBlock stair(Block parent) {
        return stair(parent, copyProperties(parent));
    }
    public static StairBlock stair(Block base, StairBlock.Properties properties) {
        return stair(base.defaultBlockState(), properties);
    }
    public static StairBlock stair(Supplier<BlockState> base, StairBlock.Properties properties) {
        return stair(base.get(), properties);
    }
    public static StairBlock stair(BlockState base, StairBlock.Properties properties) {
        return new StairBlock(base, properties);
    }

    private static final Supplier<FlowerPotBlock> EMPTY_POT_SUPPLIER = ()-> (FlowerPotBlock) Blocks.FLOWER_POT;
    public static FlowerPotBlock potted(Supplier<Block> main) {
        return new FlowerPotBlock(
                EMPTY_POT_SUPPLIER, main,
                copyProperties(Blocks.POTTED_OAK_SAPLING) //All potted plants have the same properties
        );
    }
    public static AgeableBushBlock bush(int bonusDrop, int growRarity) {
        return new AgeableBushBlock(bonusDrop, growRarity, copyProperties(Blocks.SWEET_BERRY_BUSH));
    }

    public static SaplingBlock sapling(TreeGrower treeGrower) {
        // all saplings have the same props
        return new SaplingBlock(treeGrower, copyProperties(Blocks.OAK_SAPLING));
    }

    public static ButtonBlock woodenButton() { return woodenButton(BlockSetType.OAK); }
    public static ButtonBlock woodenButton(BlockSetType type) { return button(type, 30, true); }
    public static ButtonBlock stoneButton() { return stoneButton(BlockSetType.STONE); }
    public static ButtonBlock stoneButton(BlockSetType type) { return button(type, 20, false); }
    public static ButtonBlock button(BlockSetType type, int ticksStayPressed, boolean arrowsCanPress){
        return new ButtonBlock(type, ticksStayPressed, BlockBehaviour.Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY));
    }

    public static FenceGateBlock fenceGate(WoodType type, BlockBehaviour.Properties properties){
        return new FenceGateBlock(type, properties);
    }
    public static FenceGateBlock fenceGate(BlockBehaviour.Properties properties, SoundEvent openSound, SoundEvent closeSound){
        return new FenceGateBlock(properties, openSound, closeSound);
    }

    public static DoorBlock door(BlockBehaviour.Properties properties, BlockSetType type){
        return new DoorBlock(type, properties);
    }
    public static TrapDoorBlock trapDoor(BlockBehaviour.Properties properties, BlockSetType type){
        return new TrapDoorBlock(type, properties);
    }

    public static PressurePlateBlock woodenPressurePlate(MapColor mapColor, BlockSetType type) {
        return pressurePlate(basePressurePlateProperties(mapColor, NoteBlockInstrument.BASS).ignitedByLava(), type);
    }
    public static PressurePlateBlock stonePressurePlate(MapColor mapColor, BlockSetType type) {
        return pressurePlate(basePressurePlateProperties(mapColor, NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops(), type);
    }
    public static PressurePlateBlock pressurePlate(BlockBehaviour.Properties properties, BlockSetType type){
        return new PressurePlateBlock(type, properties);
    }

    public static BlockBehaviour.Properties basePressurePlateProperties(DyeColor mapColor, NoteBlockInstrument instrument){
        return internalPressurePlateProperties(instrument).mapColor(mapColor);
    }
    public static BlockBehaviour.Properties basePressurePlateProperties(MapColor  mapColor, NoteBlockInstrument instrument){
        return internalPressurePlateProperties(instrument).mapColor(mapColor);
    }
    public static BlockBehaviour.Properties basePressurePlateProperties(Function<BlockState, MapColor>  mapColor, NoteBlockInstrument instrument){
        return internalPressurePlateProperties(instrument).mapColor(mapColor);
    }
    private static BlockBehaviour.Properties internalPressurePlateProperties(NoteBlockInstrument instrument){
        return BlockBehaviour.Properties.of().forceSolidOn().instrument(instrument).noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY);
    }

    public static ToIntFunction<BlockState> litEmission(int lightLevel) {return (state)-> state.getValue(BlockStateProperties.LIT) ? lightLevel : 0;}
    public static ToIntFunction<BlockState> litEmission(ToIntFunction<BlockState> lightLevel) {return (state)-> state.getValue(BlockStateProperties.LIT) ? lightLevel.applyAsInt(state) : 0;}
    public static ToIntFunction<BlockState> emission(int lightLevel) {return (state)-> lightLevel;}
}
