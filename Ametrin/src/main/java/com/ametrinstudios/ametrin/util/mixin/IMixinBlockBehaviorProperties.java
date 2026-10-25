package com.ametrinstudios.ametrin.util.mixin;

import net.minecraft.resources.DependantName;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Optional;

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

    default void overrideDrops(DependantName<Block, Optional<ResourceKey<LootTable>>> drops) {
        throw new AssertionError("Implemented via Mixin");
    }

    default void overrideDescriptionId(DependantName<Block, String> descriptionId) {
        throw new AssertionError("Implemented via Mixin");
    }
}