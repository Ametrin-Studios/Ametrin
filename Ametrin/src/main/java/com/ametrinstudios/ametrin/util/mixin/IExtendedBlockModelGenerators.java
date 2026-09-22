package com.ametrinstudios.ametrin.util.mixin;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;

public interface IExtendedBlockModelGenerators {
    default BlockModelGenerators.BlockFamilyProvider family(Block fullBlock, TexturedModel model) {
        throw new AssertionError("Implemented via Mixin");
    }

    default BlockModelGenerators.BlockFamilyProvider family(Block fullBlock, Function<Block, TexturedModel> model) {
        return family(fullBlock, model.apply(fullBlock));
    }

    default BlockModelGenerators.BlockFamilyProvider familyWithExistingFullBlock(Block fullBlock, TextureMapping mapping) {
        throw new AssertionError("Implemented via Mixin");
    }

    default BlockModelGenerators.BlockFamilyProvider familyWithExistingFullBlock(Block fullBlock, Function<Block, TextureMapping> mapping) {
        return familyWithExistingFullBlock(fullBlock, mapping.apply(fullBlock));
    }
}
