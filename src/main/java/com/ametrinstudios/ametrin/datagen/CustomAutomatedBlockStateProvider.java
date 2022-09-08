package com.ametrinstudios.ametrin.datagen;

import net.minecraft.world.level.block.Block;

@FunctionalInterface
public interface CustomAutomatedBlockStateProvider {
    boolean block(Block block, String name, String texture);
}