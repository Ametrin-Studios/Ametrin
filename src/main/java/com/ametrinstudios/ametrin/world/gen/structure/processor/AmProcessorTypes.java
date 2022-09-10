package com.ametrinstudios.ametrin.world.gen.structure.processor;

import com.ametrinstudios.ametrin.AmetrinUtil;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class AmProcessorTypes {
    public static final StructureProcessorType<KeepStateRandomBlockSwapProcessor> BlockSwap = ()-> KeepStateRandomBlockSwapProcessor.CODEC;

    public static void register() {
        register("underwater", BlockSwap);
    }

    private static <P extends StructureProcessor> void register(String key, StructureProcessorType<P> processorType) {
        Registry.register(Registry.STRUCTURE_PROCESSOR, AmetrinUtil.location(key), processorType);
    }
}