package com.ametrinstudios.ametrin_test.data.provider;

import com.ametrinstudios.ametrin.data.provider.ExtendedBlockTagsProvider;
import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import com.ametrinstudios.ametrin_test.registry.TestBlocks;
import com.ametrinstudios.ametrin_test.registry.TestTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class TestBlockTagsProvider extends ExtendedBlockTagsProvider {
    public TestBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, AmetrinTestMod.MOD_ID);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        new TestBlockItemTagsProvider() {
            @Override
            protected @NotNull TagAppender<Block, Block> tag(@NotNull TagKey<Block> blockTag, @NotNull TagKey<Item> itemTag) {
                return TestBlockTagsProvider.this.tag(blockTag);
            }
        }.run();

        runRules(TestBlocks.REGISTER);

        tag(TestTags.Blocks.TEST_PORTAL_FRAMES).add(
                TestBlocks.TEST_BLOCK.get()
        );
    }
}
