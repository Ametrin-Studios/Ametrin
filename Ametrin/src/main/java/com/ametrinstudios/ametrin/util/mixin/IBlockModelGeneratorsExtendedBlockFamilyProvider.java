package com.ametrinstudios.ametrin.util.mixin;

import net.minecraft.client.data.models.BlockModelGenerators;

public interface IBlockModelGeneratorsExtendedBlockFamilyProvider {
    default BlockModelGenerators blockModels() {
        throw new AssertionError("Implemented via Mixin");
    }
}
