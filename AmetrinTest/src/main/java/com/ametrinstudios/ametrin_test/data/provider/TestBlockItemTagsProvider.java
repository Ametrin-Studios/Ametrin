package com.ametrinstudios.ametrin_test.data.provider;

import com.ametrinstudios.ametrin.data.provider.ExtendedBlockItemTagsProvider;
import com.ametrinstudios.ametrin_test.registry.TestBlocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;

public abstract class TestBlockItemTagsProvider extends ExtendedBlockItemTagsProvider {

    @Override
    protected void run() {
        runRules(TestBlocks.REGISTER);
        tag(BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN)
                .add(TestBlocks.TEST_LOG.get())
        ;
    }
}
