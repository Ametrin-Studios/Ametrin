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

import static net.minecraft.world.level.storage.loot.providers.number.ConstantValue.exactly;
import static net.minecraft.world.level.storage.loot.providers.number.UniformGenerator.between;

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

    @Deprecated
    public static LootPoolSingletonContainer.Builder<?> item(ItemLike item, int weight, NumberProvider amount) {
        return item(item, amount).setWeight(weight);
    }

    public static LootPoolSingletonContainer.Builder<?> item(ItemLike item, NumberProvider amount) {
        var builder = item(item);
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

    public static LootPoolSingletonContainer.Builder<?> enchantedItem(ItemLike item, NumberProvider amount, HolderLookup.Provider provider, NumberProvider levels) {
        return item(item, amount).apply(EnchantWithLevelsFunction.enchantWithLevels(provider, levels));
    }

    public static LootPoolSingletonContainer.Builder<?> enchantedItem(ItemLike item, NumberProvider amount, HolderLookup.Provider provider) {
        return item(item, amount).apply(EnchantRandomlyFunction.randomApplicableEnchantment(provider));
    }

    public static LootPoolSingletonContainer.Builder<?> suspiciousStew(NumberProvider amount) {
        return item(Items.SUSPICIOUS_STEW, amount).apply(SetStewEffectFunction.stewEffect().withEffect(MobEffects.NIGHT_VISION, between(7, 10)).withEffect(MobEffects.JUMP_BOOST, between(7, 10)).withEffect(MobEffects.WEAKNESS, between(6, 8)).withEffect(MobEffects.BLINDNESS, between(5, 7)).withEffect(MobEffects.POISON, between(10, 20)).withEffect(MobEffects.SATURATION, between(7, 10)));
    }

    public static LootPoolSingletonContainer.Builder<?> potion(Holder<Potion> potion, NumberProvider amount) {
        return item(Items.POTION, amount).apply(SetPotionFunction.setPotion(potion));
    }

    public static LootPoolSingletonContainer.Builder<?> splashPotion(Holder<Potion> potion, NumberProvider amount) {
        return item(Items.SPLASH_POTION, amount).apply(SetPotionFunction.setPotion(potion));
    }

    public static LootPoolSingletonContainer.Builder<?> lingeringPotion(Holder<Potion> potion, NumberProvider amount) {
        return item(Items.LINGERING_POTION, amount).apply(SetPotionFunction.setPotion(potion));
    }

    public static NumberProvider one() {
        return exactly(1);
    }

    @Deprecated
    public static NumberProvider number(int amount) {
        return exactly(amount);
    }

    @Deprecated
    public static NumberProvider number(int min, int max) {
        return between(min, max);
    }

    @Deprecated
    public static LootPool.Builder pool(NumberProvider rolls) {
        return LootPool.lootPool().setRolls(rolls);
    }
}