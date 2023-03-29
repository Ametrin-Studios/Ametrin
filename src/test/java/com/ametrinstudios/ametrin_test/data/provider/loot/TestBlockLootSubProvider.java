package com.ametrinstudios.ametrin_test.data.provider.loot;

import com.ametrinstudios.ametrin.data.provider.loot_table.ExtendedBlockLootSubProvider;
import com.ametrinstudios.ametrin_test.world.TestBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class TestBlockLootSubProvider extends ExtendedBlockLootSubProvider {
    @Override
    protected void generate() {
        dropSelf(TestBlocks.TEST_BLOCK.get());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return TestBlocks.REGISTRY.getEntries().stream().map(RegistryObject::get).toList();
    }
}
