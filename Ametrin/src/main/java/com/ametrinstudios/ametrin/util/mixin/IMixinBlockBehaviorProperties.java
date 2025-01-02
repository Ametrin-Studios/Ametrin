package com.ametrinstudios.ametrin.util.mixin;

import net.minecraft.resources.DependantName;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Optional;

public interface IMixinBlockBehaviorProperties {
    BlockBehaviour.Properties copy();

    void setOffsetFunction(BlockBehaviour.OffsetFunction func);

    void setFeatureFlagSet(FeatureFlagSet featureFlags);

    void overrideDrops(DependantName<Block, Optional<ResourceKey<LootTable>>> drops);
}