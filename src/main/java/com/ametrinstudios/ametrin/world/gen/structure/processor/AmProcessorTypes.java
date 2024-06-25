package com.ametrinstudios.ametrin.world.gen.structure.processor;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public final class AmProcessorTypes {
    public static final StructureProcessorType<KeepStateRandomBlockSwapProcessor> KEEP_STATE_RANDOM_BLOCK_SWAP = register("keep_state_random_block_swap", (MapCodec<KeepStateRandomBlockSwapProcessor>) KeepStateRandomBlockSwapProcessor.CODEC);

    static <P extends StructureProcessor> StructureProcessorType<P> register(String name, MapCodec<P> codec) {
        return Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, name, () -> codec);
    }
}