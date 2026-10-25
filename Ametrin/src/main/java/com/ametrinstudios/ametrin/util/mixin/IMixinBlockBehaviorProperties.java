package com.ametrinstudios.ametrin.util.mixin;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.function.Supplier;

public interface IMixinBlockBehaviorProperties {
    default BlockBehaviour.Properties copy() {
        throw new AssertionError("Implemented via Mixin");
    }

    default void setOffsetFunction(BlockBehaviour.OffsetFunction func) {
        throw new AssertionError("Implemented via Mixin");
    }

    default void setFeatureFlagSet(FeatureFlagSet featureFlags) {
        throw new AssertionError("Implemented via Mixin");
    }

    default void overrideDrops(ResourceKey<LootTable> drops) {
        throw new AssertionError("Implemented via Mixin");
    }

    default void overrideLootTableSupplier(Supplier<ResourceKey<LootTable>> lootTableSupplier) {
        throw new AssertionError("Implemented via Mixin");
    }
}