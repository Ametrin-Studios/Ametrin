package com.ametrinstudios.ametrin.world.block.helper;

import com.ametrinstudios.ametrin.util.mixin.IMixinBlockBehaviorProperties;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.util.function.Function;

public class BlockBehaviourPropertiesHelper {
    private BlockBehaviourPropertiesHelper() {}
    public static BlockBehaviour.Properties CopyProperties(BlockBehaviour.Properties properties) {return ((IMixinBlockBehaviorProperties) properties).copy();}
    public static BlockBehaviour.Properties CopyProperties(BlockBehaviour parent) {return BlockBehaviour.Properties.copy(parent);}
    public static BlockBehaviour.Properties PropertiesOf(Material material) {return BlockBehaviour.Properties.of(material);}
    public static BlockBehaviour.Properties PropertiesOf(Material material, DyeColor dyeColor) {return BlockBehaviour.Properties.of(material, dyeColor);}
    public static BlockBehaviour.Properties PropertiesOf(Material material, MaterialColor color) {return BlockBehaviour.Properties.of(material, color);}
    public static BlockBehaviour.Properties PropertiesOf(Material material, Function<BlockState, MaterialColor> color) {return BlockBehaviour.Properties.of(material, color);}
}
