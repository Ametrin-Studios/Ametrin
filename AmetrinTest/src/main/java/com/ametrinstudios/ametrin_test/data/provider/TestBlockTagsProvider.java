package com.ametrinstudios.ametrin_test.data.provider;

import com.ametrinstudios.ametrin.data.provider.ExtendedBlockTagsProvider;
import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import com.ametrinstudios.ametrin_test.registry.TestBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public final class TestBlockTagsProvider extends ExtendedBlockTagsProvider {
    public TestBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, registries, AmetrinTestMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        runRules(TestBlocks.REGISTER);

        tag(BlockTags.LOGS_THAT_BURN).add(
                TestBlocks.TEST_LOG.get()
        );
    }
}
