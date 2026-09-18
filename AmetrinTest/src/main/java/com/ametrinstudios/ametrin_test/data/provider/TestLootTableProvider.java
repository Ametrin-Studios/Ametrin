package com.ametrinstudios.ametrin_test.data.provider;

import com.ametrinstudios.ametrin_test.data.provider.loot.TestBlockLootProvider;
import com.ametrinstudios.ametrin_test.data.provider.loot.TestChestLootProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public final class TestLootTableProvider {
    public static LootTableProvider create() {
        return new LootTableProvider(
                Set.of(),
                List.of(
                        new LootTableProvider.SubProviderEntry(TestBlockLootProvider::new, LootContextParamSets.BLOCK),
                        new LootTableProvider.SubProviderEntry(TestChestLootProvider::new, LootContextParamSets.CHEST)
                )
        );
    }
}
