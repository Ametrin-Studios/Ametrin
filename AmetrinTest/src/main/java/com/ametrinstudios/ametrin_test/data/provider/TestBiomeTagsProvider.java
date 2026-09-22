package com.ametrinstudios.ametrin_test.data.provider;

import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import com.ametrinstudios.ametrin_test.registry.TestTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public final class TestBiomeTagsProvider extends BiomeTagsProvider {

    public TestBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, AmetrinTestMod.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(TestTags.Biomes.TEST_BIOMES).addTag(Tags.Biomes.IS_AQUATIC).remove(Biomes.RIVER);
    }
}
