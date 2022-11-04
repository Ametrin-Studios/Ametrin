package com.ametrinstudios.ametrin.util.mixin;

import net.minecraft.world.level.block.state.BlockBehaviour;

public interface IMixinBlockBehaviorProperties {
    BlockBehaviour.Properties copy();
}