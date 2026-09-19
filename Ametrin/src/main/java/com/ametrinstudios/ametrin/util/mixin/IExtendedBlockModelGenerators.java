package com.ametrinstudios.ametrin.util.mixin;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;

public interface IExtendedBlockModelGenerators {
    default BlockModelGenerators.BlockFamilyProvider familyWithExistingFullBlock(Block fullBlock, TextureMapping mapping) {
        throw new AssertionError("Implemented via Mixin");
    }

    default BlockModelGenerators.BlockFamilyProvider familyWithExistingFullBlock(Block fullBlock, Function<Block, TextureMapping> mapping) {
        throw new AssertionError("Implemented via Mixin");
    }
}
