package com.ametrinstudios.ametrin_test.data.provider;

import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BlockItemTagsProvider;
import net.minecraft.references.ItemIds;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public final class TestItemTagsProvider extends ItemTagsProvider {
    public TestItemTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTagProvider) {
        super(packOutput, lookupProvider, AmetrinTestMod.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        new TestBlockItemTagsProvider(tags -> BlockItemTagsProvider.wrapForItems(tag(tags.item()))).run();

        tag(ItemTags.BEACON_PAYMENT_ITEMS).remove(ItemIds.IRON_INGOT);
    }
}
