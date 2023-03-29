package com.ametrinstudios.ametrin.data.provider;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;

import java.util.List;
import java.util.Set;

public class CustomLootTableProvider extends LootTableProvider {
    public CustomLootTableProvider(PackOutput packOutput, List<SubProviderEntry> providers) {
        super(packOutput, Set.of(), providers);
    }
}