package com.ametrinstudios.ametrin.data;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;

@FunctionalInterface
public interface ItemTagProviderRule {
    void run(DeferredHolder<Item, ? extends Item> item, String name);
}