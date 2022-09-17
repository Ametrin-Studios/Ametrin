package com.ametrinstudios.ametrin.datagen;

import net.minecraft.world.item.Item;

@FunctionalInterface
public interface ItemModelProviderRule {
    boolean generate(Item block, String name, String texture);
}