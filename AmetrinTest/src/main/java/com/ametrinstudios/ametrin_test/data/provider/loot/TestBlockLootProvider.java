package com.ametrinstudios.ametrin_test.data.provider.loot;

import com.ametrinstudios.ametrin.data.provider.loot_table.ExtendedBlockLootSubProvider;
import com.ametrinstudios.ametrin_test.registry.TestBlocks;
import com.ametrinstudios.ametrin_test.registry.TestItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public final class TestBlockLootProvider extends ExtendedBlockLootSubProvider {
    public TestBlockLootProvider(HolderLookup.Provider registries) {
        super(registries);
    }

    @Override
    protected void generate() {
        dropSelf(
                TestBlocks.TEST_BLOCK,
                TestBlocks.TEST_LOG
        );

        dropOther(TestBlocks.TEST_SKULL, TestItems.TEST_SKULL);
        dropOther(TestBlocks.TEST_SKULL_WALL, TestItems.TEST_SKULL);
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return TestBlocks.REGISTER.getEntries().stream().map(s->(Block)s.get()).toList();
    }
}
