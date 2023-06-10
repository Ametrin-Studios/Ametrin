package com.ametrinstudios.ametrin.util.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Optional;
import java.util.function.Supplier;

public interface IMixinBlockBehaviorProperties {
    BlockBehaviour.Properties copy();
    void SetOffsetFunction(Optional<BlockBehaviour.OffsetFunction> func);
    void SetFeatureFlagSet(FeatureFlagSet featureFlags);
    void SetLootTableSupplier(Supplier<ResourceLocation> lootTable);
}