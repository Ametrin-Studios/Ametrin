package com.ametrinstudios.ametrin_test.data.provider;

import com.ametrinstudios.ametrin.data.provider.ExtendedModelProvider;
import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import com.ametrinstudios.ametrin_test.registry.TestBlocks;
import com.ametrinstudios.ametrin_test.registry.TestItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

public final class TestModelProvider extends ExtendedModelProvider {
    public TestModelProvider(PackOutput output) {
        super(output, AmetrinTestMod.MOD_ID);
    }

    @Override
    protected void registerModels(@NotNull BlockModelGenerators blockModels, @NotNull ItemModelGenerators itemModels) {
        blockModels.createTrivialCube(TestBlocks.TEST_BLOCK.get());
        blockModels.woodProvider(TestBlocks.TEST_LOG.get()).log(TestBlocks.TEST_LOG.get());

        createCustomHeadCutout(blockModels, TestBlocks.TEST_SKULL.get(), TestBlocks.TEST_SKULL_WALL.get());
        createPlanePortalBlock(blockModels, TestBlocks.TEST_PORTAL.get());

        itemModels.generateFlatItem(TestItems.TEST_CATALYST.get(), ModelTemplates.FLAT_ITEM);
    }
}