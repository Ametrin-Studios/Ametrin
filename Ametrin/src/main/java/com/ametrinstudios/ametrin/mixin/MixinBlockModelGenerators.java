package com.ametrinstudios.ametrin.mixin;

import com.ametrinstudios.ametrin.util.mixin.IExtendedBlockModelGenerators;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Function;

@Mixin(BlockModelGenerators.class)
public abstract class MixinBlockModelGenerators implements IExtendedBlockModelGenerators {

    @Override
    public BlockModelGenerators.BlockFamilyProvider familyWithExistingFullBlock(Block fullBlock, TextureMapping mapping) {
        var provider = ((BlockModelGenerators)(Object)this).new BlockFamilyProvider(mapping);
        provider.fullBlock = BlockModelGenerators.plainModel(ModelLocationUtils.getModelLocation(fullBlock));
        return provider;
    }

    @Override
    public BlockModelGenerators.BlockFamilyProvider familyWithExistingFullBlock(Block fullBlock, Function<Block, TextureMapping> mapping) {
        return familyWithExistingFullBlock(fullBlock, mapping.apply(fullBlock));
    }
}
