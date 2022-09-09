package com.ametrinstudios.ametrin.datagen;

import net.minecraft.world.level.block.Block;

@FunctionalInterface
public interface BlockStateProviderRule {
    boolean generate(Block block, String name, String texture);
}