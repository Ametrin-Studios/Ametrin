package com.ametrinstudios.ametrin.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public abstract class ExtendedLootTableProvider extends LootTableProvider {
    public ExtendedLootTableProvider(DataGenerator generator) {super(generator);}

    protected static NumberProvider one() {return ConstantValue.exactly(1);}
    protected static NumberProvider lootNumber(int amount) {return ConstantValue.exactly(amount);}
    protected static NumberProvider lootNumber(int minAmount, int maxAmount) {return UniformGenerator.between(minAmount, maxAmount);}

    protected static LootPool.Builder lootPool(NumberProvider rolls) {return LootPool.lootPool().setRolls(rolls);}
}