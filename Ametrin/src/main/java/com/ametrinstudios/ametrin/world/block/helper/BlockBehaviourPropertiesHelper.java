package com.ametrinstudios.ametrin.world.block.helper;

import net.minecraft.world.level.block.state.BlockBehaviour;

@Deprecated
public final class BlockBehaviourPropertiesHelper {
    private BlockBehaviourPropertiesHelper() { }

    public static BlockBehaviour.Properties copyProperties(BlockBehaviour.Properties properties) {
        return properties.copy();
    }

    public static BlockBehaviour.Properties copyProperties(BlockBehaviour parent) {
        return BlockBehaviour.Properties.ofFullCopy(parent);
    }

    public static BlockBehaviour.Properties properties() {
        return BlockBehaviour.Properties.of();
    }
}
