package com.ametrinstudios.ametrin.datagen;

import net.minecraft.world.level.block.Block;

@FunctionalInterface
public interface BlockTagProviderRule {
    void generate(Block block, String name);
}