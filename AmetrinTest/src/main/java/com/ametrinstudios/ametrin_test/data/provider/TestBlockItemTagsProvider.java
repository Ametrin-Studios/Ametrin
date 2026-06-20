package com.ametrinstudios.ametrin_test.data.provider;

import com.ametrinstudios.ametrin.data.provider.ExtendedBlockItemTagsProvider;
import com.ametrinstudios.ametrin_test.registry.TestBlocks;
import net.minecraft.references.BlockItemId;
import net.minecraft.tags.BlockItemTagId;
import net.minecraft.tags.BlockItemTags;

import java.util.function.Function;

public class TestBlockItemTagsProvider extends ExtendedBlockItemTagsProvider {

    protected TestBlockItemTagsProvider(Function<BlockItemTagId, CombinedAppender> tagSupplier) {
        super(tagSupplier);
    }

    @Override
    protected void run() {
        runRules(TestBlocks.REGISTER);
        tag(BlockItemTags.LOGS_THAT_BURN)
                .add(BlockItemId.create(TestBlocks.TEST_LOG.getId(), TestBlocks.TEST_LOG.getId()))
        ;
    }
}
