package com.ametrinstudios.ametrin_test.data.provider;

import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class TestItemTagsProvider extends ItemTagsProvider {
    public TestItemTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTagProvider) {
        super(packOutput, lookupProvider, AmetrinTestMod.MOD_ID);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        new TestBlockItemTagsProvider() {
            @Override
            protected @NotNull TagAppender<Block, Block> tag(@NotNull TagKey<Block> blockTag, @NotNull TagKey<Item> itemTag) {
                return new BlockToItemConverter(TestItemTagsProvider.this.tag(itemTag));
            }
        }.run();

        tag(ItemTags.BEACON_PAYMENT_ITEMS).remove(Items.IRON_INGOT);
    }
}
