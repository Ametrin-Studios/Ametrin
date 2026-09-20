package com.ametrinstudios.ametrin.mixin;

import com.ametrinstudios.ametrin.util.mixin.IExtendedBlockModelGenerators;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockModelGenerators.class)
public abstract class MixinBlockModelGenerators implements IExtendedBlockModelGenerators {
    @Override
    public BlockModelGenerators.BlockFamilyProvider family(Block fullBlock, TexturedModel model) {
        return ((BlockModelGenerators) (Object) this).new BlockFamilyProvider(model.getMapping()).fullBlock(fullBlock, model.getTemplate());
    }

    @Override
    public BlockModelGenerators.BlockFamilyProvider familyWithExistingFullBlock(Block fullBlock, TextureMapping mapping) {
        var provider = ((BlockModelGenerators) (Object) this).new BlockFamilyProvider(mapping);
        provider.fullBlock = BlockModelGenerators.plainModel(ModelLocationUtils.getModelLocation(fullBlock));
        return provider;
    }
}
