package com.ametrinstudios.ametrin.util.mixin;

import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Optional;

public interface IMixinBlockBehaviorProperties {
    BlockBehaviour.Properties copy();
    void SetOffsetFunction(Optional<BlockBehaviour.OffsetFunction> func);
    void SetFeatureFlagSet(FeatureFlagSet featureFlags);
}