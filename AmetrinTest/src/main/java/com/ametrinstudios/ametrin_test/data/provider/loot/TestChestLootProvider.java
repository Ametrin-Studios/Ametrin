package com.ametrinstudios.ametrin_test.data.provider.loot;

import com.ametrinstudios.ametrin.data.provider.loot_table.ExtendedLootSubProvider;
import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import com.ametrinstudios.ametrin_test.registry.TestBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.storage.loot.LootTable;

import static com.ametrinstudios.ametrin.data.LootTableProviderHelper.*;
import static net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders.between;
import static net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders.exactly;

public final class TestChestLootProvider extends ExtendedLootSubProvider {
    public TestChestLootProvider(Context output) {
        super(output);
    }

    @Override
    public void run() {
        output.accept(ResourceKey.create(Registries.LOOT_TABLE, AmetrinTestMod.locate("chests/test")), LootTable.lootTable()
                .withPool(pool(between(1, 3))
                        .add(item(TestBlocks.TEST_BLOCK.get(), 1, between(1, 3)))
                        .add(tag(items.getOrThrow(ItemTags.SWORDS), 1, exactly(2)))
                        .add(potion(1, Potions.FIRE_RESISTANCE, one()))
                        .add(suspiciousStew(1, one()))
                )
        );
    }
}
