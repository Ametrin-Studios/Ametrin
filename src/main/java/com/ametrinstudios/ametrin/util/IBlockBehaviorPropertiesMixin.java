package com.ametrinstudios.ametrin.util;

import net.minecraft.world.level.block.state.BlockBehaviour;

public interface IBlockBehaviorPropertiesMixin {
    BlockBehaviour.Properties fullCopy();
}