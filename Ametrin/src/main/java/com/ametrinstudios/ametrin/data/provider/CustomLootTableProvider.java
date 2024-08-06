package com.ametrinstudios.ametrin.data.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class CustomLootTableProvider extends LootTableProvider {
    public CustomLootTableProvider(PackOutput packOutput, List<SubProviderEntry> subProviders, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, Set.of(), subProviders, registries);
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{
        private final List<SubProviderEntry> _subProviders;

        public Builder(){
             _subProviders = new ArrayList<>();
        }

        public Builder addBlockProvider(Function<HolderLookup.Provider, LootTableSubProvider> subProviderSupplier){
            return addProvider(subProviderSupplier, LootContextParamSets.BLOCK);
        }
        public Builder addChestProvider(Function<HolderLookup.Provider, LootTableSubProvider> subProviderSupplier){
            return addProvider(subProviderSupplier, LootContextParamSets.CHEST);
        }
        public Builder addEntityProvider(Function<HolderLookup.Provider, LootTableSubProvider> subProviderSupplier){
            return addProvider(subProviderSupplier, LootContextParamSets.ENTITY);
        }
        public Builder addArcheologyProvider(Function<HolderLookup.Provider, LootTableSubProvider> subProviderSupplier){
            return addProvider(subProviderSupplier, LootContextParamSets.ARCHAEOLOGY);
        }
        public Builder addFishingProvider(Function<HolderLookup.Provider, LootTableSubProvider> subProviderSupplier){
            return addProvider(subProviderSupplier, LootContextParamSets.FISHING);
        }
        public Builder addProvider(Function<HolderLookup.Provider, LootTableSubProvider> subProviderSupplier, LootContextParamSet paramSet){
            return addProvider(new SubProviderEntry(subProviderSupplier, paramSet));
        }

        public Builder addProvider(SubProviderEntry subProviderEntry){
            _subProviders.add(subProviderEntry);
            return this;
        }

        public CustomLootTableProvider build(PackOutput output, CompletableFuture<HolderLookup.Provider> registries){
            if(_subProviders.isEmpty()) throw new IllegalStateException("Cannot run an empty LootTableProvider");
            return new CustomLootTableProvider(output, _subProviders, registries);
        }
    }
}