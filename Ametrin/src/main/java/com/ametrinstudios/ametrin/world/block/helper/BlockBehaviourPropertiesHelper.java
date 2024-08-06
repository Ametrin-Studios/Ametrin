package com.ametrinstudios.ametrin.world.block.helper;

import com.ametrinstudios.ametrin.util.mixin.IMixinBlockBehaviorProperties;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class BlockBehaviourPropertiesHelper {
    private BlockBehaviourPropertiesHelper() {}

    public static BlockBehaviour.Properties copyProperties(BlockBehaviour.Properties properties) { return ((IMixinBlockBehaviorProperties) properties).copy(); }
    public static BlockBehaviour.Properties copyProperties(BlockBehaviour parent) { return BlockBehaviour.Properties.ofFullCopy(parent); }
    public static BlockBehaviour.Properties properties() { return BlockBehaviour.Properties.of(); }
}
