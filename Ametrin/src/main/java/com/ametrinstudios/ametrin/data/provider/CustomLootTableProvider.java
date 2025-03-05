package com.ametrinstudios.ametrin.data.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public final class CustomLootTableProvider extends LootTableProvider {
    public CustomLootTableProvider(PackOutput packOutput, List<SubProviderEntry> subProviders, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, Set.of(), subProviders, registries);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<SubProviderEntry> _subProviders;

        public Builder() {
            _subProviders = new ArrayList<>();
        }

        public Builder addBlockProvider(Function<HolderLookup.Provider, LootTableSubProvider> subProviderSupplier) {
            return addProvider(LootContextParamSets.BLOCK, subProviderSupplier);
        }

        public Builder addChestProvider(Function<HolderLookup.Provider, LootTableSubProvider> subProviderSupplier) {
            return addProvider(LootContextParamSets.CHEST, subProviderSupplier);
        }

        public Builder addEntityProvider(Function<HolderLookup.Provider, LootTableSubProvider> subProviderSupplier) {
            return addProvider(LootContextParamSets.ENTITY, subProviderSupplier);
        }

        public Builder addArcheologyProvider(Function<HolderLookup.Provider, LootTableSubProvider> subProviderSupplier) {
            return addProvider(LootContextParamSets.ARCHAEOLOGY, subProviderSupplier);
        }

        public Builder addFishingProvider(Function<HolderLookup.Provider, LootTableSubProvider> subProviderSupplier) {
            return addProvider(LootContextParamSets.FISHING, subProviderSupplier);
        }

        public Builder addProvider(ContextKeySet paramSet, Function<HolderLookup.Provider, LootTableSubProvider> subProviderSupplier) {
            return addProvider(new SubProviderEntry(subProviderSupplier, paramSet));
        }

        public Builder addProvider(SubProviderEntry subProviderEntry) {
            _subProviders.add(subProviderEntry);
            return this;
        }

        public CustomLootTableProvider build(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            if (_subProviders.isEmpty()) throw new IllegalStateException("Cannot create an empty LootTableProvider");
            return new CustomLootTableProvider(output, _subProviders, registries);
        }
    }
}