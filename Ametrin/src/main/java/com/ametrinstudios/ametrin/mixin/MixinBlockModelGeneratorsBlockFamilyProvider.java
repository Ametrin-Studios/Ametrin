package com.ametrinstudios.ametrin.mixin;

import com.ametrinstudios.ametrin.util.mixin.IBlockModelGeneratorsExtendedBlockFamilyProvider;
import net.minecraft.client.data.models.BlockModelGenerators;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockModelGenerators.BlockFamilyProvider.class)
public abstract class MixinBlockModelGeneratorsBlockFamilyProvider implements IBlockModelGeneratorsExtendedBlockFamilyProvider {
    @Override @Accessor("this$0")
    public abstract BlockModelGenerators blockModels();
}
