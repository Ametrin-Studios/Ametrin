package com.ametrinstudios.ametrin.util;

import net.minecraft.world.level.block.state.BlockBehaviour;

public interface IMixinBlockBehaviorProperties {
    BlockBehaviour.Properties fullCopy();
}