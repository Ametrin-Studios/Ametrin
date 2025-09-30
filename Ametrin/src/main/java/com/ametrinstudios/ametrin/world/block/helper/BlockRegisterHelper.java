package com.ametrinstudios.ametrin.world.block.helper;

import com.ametrinstudios.ametrin.world.block.PortalBlock;
import com.ametrinstudios.ametrin.world.dimension.portal.PortalData;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static com.ametrinstudios.ametrin.world.block.helper.BlockBehaviourPropertiesHelper.copyProperties;

@SuppressWarnings("unused")
public final class BlockRegisterHelper {
    private BlockRegisterHelper() { }

    public static final int STONE_BUTTON_TICKS_PRESSED = 20;
    public static final int WOOD_BUTTON_TICKS_PRESSED = 30;

    public static Function<BlockBehaviour.Properties, PortalBlock> portalBlock(PortalData data, int lightLevel) {
        return portalBlock(data, SoundType.GLASS, lightLevel);
    }

    public static Function<BlockBehaviour.Properties, PortalBlock> portalBlock(PortalData data, SoundType soundType, int lightLevel) {
        return properties -> new PortalBlock(data, properties.strength(-1).noCollision().lightLevel((state) -> lightLevel).noLootTable().randomTicks().sound(soundType));
    }

    public static Function<BlockBehaviour.Properties, StairBlock> stair(Supplier<BlockState> parent) {
        return properties -> new StairBlock(parent.get(), properties);
    }

    private static final Supplier<FlowerPotBlock> EMPTY_POT_SUPPLIER = () -> (FlowerPotBlock) Blocks.FLOWER_POT;
    public static Function<BlockBehaviour.Properties, FlowerPotBlock> potted(Supplier<Block> main) {
        return properties -> new FlowerPotBlock(
                EMPTY_POT_SUPPLIER, main,
                properties
        );
    }
    public static BlockBehaviour.Properties buttonProperties() {
        return BlockBehaviour.Properties.of().noCollision().strength(0.5f).pushReaction(PushReaction.DESTROY);
    }

    public static BlockBehaviour.Properties bushProperties() {
        return copyProperties(Blocks.SWEET_BERRY_BUSH);
    }

    public static BlockBehaviour.Properties pottedProperties() {
        return copyProperties(Blocks.POTTED_OAK_SAPLING);
    }

    public static BlockBehaviour.Properties saplingProperties() {
        return copyProperties(Blocks.OAK_SAPLING);
    }

    public static BlockBehaviour.Properties woodenPressurePlateProperties() {
        return pressurePlateProperties().instrument(NoteBlockInstrument.BASS);
    }

    public static BlockBehaviour.Properties stonePressurePlateProperties() {
        return pressurePlateProperties().instrument(NoteBlockInstrument.BASEDRUM);
    }

    public static BlockBehaviour.Properties pressurePlateProperties() {
        return BlockBehaviour.Properties.of().forceSolidOn().noCollision().strength(0.5F).pushReaction(PushReaction.DESTROY);
    }

    public static ToIntFunction<BlockState> litEmission(int lightLevel) {
        return (state) -> state.getValue(BlockStateProperties.LIT) ? lightLevel : 0;
    }

    public static ToIntFunction<BlockState> litEmission(ToIntFunction<BlockState> lightLevel) {
        return (state) -> state.getValue(BlockStateProperties.LIT) ? lightLevel.applyAsInt(state) : 0;
    }

    public static ToIntFunction<BlockState> emission(int lightLevel) {
        return (state) -> lightLevel;
    }
}
