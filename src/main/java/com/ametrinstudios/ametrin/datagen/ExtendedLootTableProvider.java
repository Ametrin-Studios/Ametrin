package com.ametrinstudios.ametrin.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.packs.VanillaLootTableProvider;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Set;

public abstract class ExtendedLootTableProvider extends LootTableProvider {
    public ExtendedLootTableProvider(PackOutput packOutput) {
        super(packOutput, Set.of(), VanillaLootTableProvider.create(packOutput).getTables());
    }

    protected static NumberProvider one() {return ConstantValue.exactly(1);}
    protected static NumberProvider lootNumber(int amount) {return ConstantValue.exactly(amount);}
    protected static NumberProvider lootNumber(int minAmount, int maxAmount) {return UniformGenerator.between(minAmount, maxAmount);}

    protected static LootPool.Builder lootPool(NumberProvider rolls) {return LootPool.lootPool().setRolls(rolls);}
}