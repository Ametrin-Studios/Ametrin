package com.ametrinstudios.ametrin.mixin.util;

import net.minecraft.world.level.block.state.BlockBehaviour;

public interface IBlockBehaviorPropertiesMixin {
    BlockBehaviour.Properties copy();
}