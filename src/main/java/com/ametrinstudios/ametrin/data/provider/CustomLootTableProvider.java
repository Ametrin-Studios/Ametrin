package com.ametrinstudios.ametrin.data.provider;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class CustomLootTableProvider extends LootTableProvider {
    public CustomLootTableProvider(PackOutput packOutput, List<SubProviderEntry> providers) {
        super(packOutput, Set.of(), providers);
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, @NotNull ValidationContext validationContext) {
        map.forEach((resourceLocation, lootTable) -> LootTables.validate(validationContext, resourceLocation, lootTable));
    }

    public static Builder Builder(){
        return new Builder();
    }

    public static class Builder{
        private final List<SubProviderEntry> SubProvider;

        public Builder(){
             SubProvider = new ArrayList<>();
        }

        public Builder AddBlockProvider(Supplier<LootTableSubProvider> subProviderSupplier){
            return AddProvider(new SubProviderEntry(subProviderSupplier, LootContextParamSets.BLOCK));
        }
        public Builder AddChestProvider(Supplier<LootTableSubProvider> subProviderSupplier){
            return AddProvider(new SubProviderEntry(subProviderSupplier, LootContextParamSets.CHEST));
        }
        public Builder AddArcheologyProvider(Supplier<LootTableSubProvider> subProviderSupplier){
            return AddProvider(new SubProviderEntry(subProviderSupplier, LootContextParamSets.ARCHAEOLOGY));
        }
        public Builder AddFishingProvider(Supplier<LootTableSubProvider> subProviderSupplier){
            return AddProvider(new SubProviderEntry(subProviderSupplier, LootContextParamSets.FISHING));
        }
        public Builder AddProvider(Supplier<LootTableSubProvider> subProviderSupplier, LootContextParamSet paramSet){
            return AddProvider(new SubProviderEntry(subProviderSupplier, paramSet));
        }

        public Builder AddProvider(SubProviderEntry subProviderEntry){
            SubProvider.add(subProviderEntry);
            return this;
        }

        public CustomLootTableProvider Build(PackOutput output){
            if(SubProvider.isEmpty()) throw new IllegalStateException("Cannot run an empty LootTableProvider");
            return new CustomLootTableProvider(output, SubProvider);
        }
    }
}