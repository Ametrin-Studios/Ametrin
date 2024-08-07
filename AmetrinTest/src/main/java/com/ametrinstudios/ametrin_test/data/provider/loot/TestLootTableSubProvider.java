package com.ametrinstudios.ametrin_test.data.provider.loot;

import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import com.ametrinstudios.ametrin_test.registry.TestBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

import static com.ametrinstudios.ametrin.data.LootTableProviderHelper.*;

public record TestLootTableSubProvider(HolderLookup.Provider registries) implements LootTableSubProvider {
    @Override
    public void generate(@NotNull BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
//        var enchantmentRegistryLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

        consumer.accept(ResourceKey.create(Registries.LOOT_TABLE, AmetrinTestMod.locate("chests/test")), LootTable.lootTable()
                .withPool(pool(number(1, 3))
                        .add(item(TestBlocks.TEST_BLOCK.get(), 1, number(1, 3)))
                        .add(tag(ItemTags.SWORDS, 1, number(2)))
                        .add(potion(1, Potions.FIRE_RESISTANCE, one()))
                        .add(suspiciousStew(1, one()))
                )
        );
    }
}
