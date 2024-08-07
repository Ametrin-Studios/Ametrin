package com.ametrinstudios.ametrin_test.data.provider;

import com.ametrinstudios.ametrin.data.provider.ExtendedItemTagsProvider;
import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import com.ametrinstudios.ametrin_test.registry.TestItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public final class TestItemTagsProvider extends ExtendedItemTagsProvider {
    public TestItemTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, lookupProvider, blockTagProvider, AmetrinTestMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        runRules(TestItems.REGISTER);

        copy(BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN);
    }
}
