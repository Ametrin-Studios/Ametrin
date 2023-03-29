package com.ametrinstudios.ametrin_test.data.provider.loot;

import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import com.ametrinstudios.ametrin_test.world.TestBlocks;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

import static com.ametrinstudios.ametrin.data.LootTableProviderHelper.*;

public class TestLootTableSubProvider implements LootTableSubProvider {
    @Override
    public void generate(@NotNull BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        consumer.accept(AmetrinTestMod.locate("chests/test"), LootTable.lootTable()
                .withPool(pool(number(1, 3))
                        .add(item(TestBlocks.TEST_BLOCK.get(), 1, number(1, 3)))
                        .add(potion(1, Potions.FIRE_RESISTANCE, one()))
                        .add(suspiciousStew(1, one()))
                )
        );
    }
}
