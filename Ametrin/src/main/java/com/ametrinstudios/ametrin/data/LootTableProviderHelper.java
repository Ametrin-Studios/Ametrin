package com.ametrinstudios.ametrin.data;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public final class LootTableProviderHelper {
    public static LootPoolSingletonContainer.Builder<?> empty(int weight) {
        return EmptyLootItem.emptyItem().setWeight(weight);
    }

    public static LootPoolSingletonContainer.Builder<?> item(ItemLike item) {
        return LootItem.lootTableItem(item);
    }

    @Deprecated
    public static LootPoolSingletonContainer.Builder<?> item(ItemLike item, int weight) {
        return item(item).setWeight(weight);
    }

    public static LootPoolSingletonContainer.Builder<?> item(ItemLike item, int weight, NumberProvider amount) {
        var builder = item(item).setWeight(weight);
        if (!(amount instanceof ConstantValue(float value) && value == 1)) {
            builder.apply(SetItemCountFunction.setCount(amount));
        }
        return builder;
    }

    public static LootPoolSingletonContainer.Builder<?> tag(TagKey<Item> tagKey) {
        return TagEntry.expandTag(tagKey);
    }

    public static LootPoolSingletonContainer.Builder<?> tag(TagKey<Item> tagKey, int weight, NumberProvider amount) {
        return tag(tagKey).setWeight(weight).apply(SetItemCountFunction.setCount(amount));
    }

    public static LootPoolSingletonContainer.Builder<?> enchantedItem(ItemLike item, int weight, NumberProvider enchant, NumberProvider amount, HolderLookup.Provider provider) {
        return item(item, weight, amount).apply(EnchantWithLevelsFunction.enchantWithLevels(provider, enchant));
    }

    public static LootPoolSingletonContainer.Builder<?> enchantedItem(ItemLike item, int weight, NumberProvider amount, HolderLookup.Provider provider) {
        return item(item, weight, amount).apply(EnchantRandomlyFunction.randomApplicableEnchantment(provider));
    }

    public static LootPoolSingletonContainer.Builder<?> suspiciousStew(int weight, NumberProvider amount) {
        return item(Items.SUSPICIOUS_STEW, weight, amount).apply(SetStewEffectFunction.stewEffect().withEffect(MobEffects.NIGHT_VISION, number(7, 10)).withEffect(MobEffects.JUMP_BOOST, number(7, 10)).withEffect(MobEffects.WEAKNESS, number(6, 8)).withEffect(MobEffects.BLINDNESS, number(5, 7)).withEffect(MobEffects.POISON, number(10, 20)).withEffect(MobEffects.SATURATION, number(7, 10)));
    }

    public static LootPoolSingletonContainer.Builder<?> potion(int weight, Holder<Potion> potion, NumberProvider amount) {
        return item(Items.POTION, weight, amount).apply(SetPotionFunction.setPotion(potion));
    }

    public static LootPoolSingletonContainer.Builder<?> splashPotion(int weight, Holder<Potion> potion, NumberProvider amount) {
        return item(Items.SPLASH_POTION, weight, amount).apply(SetPotionFunction.setPotion(potion));
    }

    public static LootPoolSingletonContainer.Builder<?> lingeringPotion(int weight, Holder<Potion> potion, NumberProvider amount) {
        return item(Items.LINGERING_POTION, weight, amount).apply(SetPotionFunction.setPotion(potion));
    }

    public static NumberProvider one() {
        return number(1);
    }

    public static NumberProvider number(int amount) {
        return ConstantValue.exactly(amount);
    }

    public static NumberProvider number(int min, int max) {
        return UniformGenerator.between(min, max);
    }

    public static LootPool.Builder pool(NumberProvider rolls) {
        return LootPool.lootPool().setRolls(rolls);
    }
}