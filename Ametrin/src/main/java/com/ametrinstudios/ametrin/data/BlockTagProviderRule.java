package com.ametrinstudios.ametrin.data;

import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;

@FunctionalInterface
public interface BlockTagProviderRule {
    void generate(DeferredHolder<Block, ? extends Block> block, String name);
}