package com.ametrinstudios.ametrin.world.block.helper;

import com.ametrinstudios.ametrin.util.mixin.IMixinBlockBehaviorProperties;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BlockBehaviourPropertiesHelper {
    public static BlockBehaviour.Properties CopyProperties(BlockBehaviour.Properties properties) {return ((IMixinBlockBehaviorProperties) properties).copy();}
    public static BlockBehaviour.Properties CopyProperties(BlockBehaviour parent) {return BlockBehaviour.Properties.copy(parent);}
}