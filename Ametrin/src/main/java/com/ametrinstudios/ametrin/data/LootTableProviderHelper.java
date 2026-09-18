package com.ametrinstudios.ametrin.data;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.level.storage.loot.entries.UniformContainerBase;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

public final class LootTableProviderHelper {
    public static UniformContainerBase.Builder<?> item(ItemLike item) {
        return LootItem.lootTableItem(item);
    }

    public static UniformContainerBase.Builder<?> item(ItemLike item, int weight, Holder<ContextIntProvider> amount) {
        return item(item).setWeight(weight).apply(SetItemCountFunction.setCount(amount));
    }

    public static UniformContainerBase.Builder<?> tag(HolderSet<Item> tagKey, int weight, Holder<ContextIntProvider> amount) {
        return TagEntry.expandTag(tagKey).setWeight(weight).apply(SetItemCountFunction.setCount(amount));
    }

    public static UniformContainerBase.Builder<?> enchantedItem(ItemLike item, int weight, Holder<ContextIntProvider> enchant, Holder<ContextIntProvider> amount, HolderGetter<Enchantment> provider) {
        return LootItem.lootTableItem(item).setWeight(weight).apply(SetItemCountFunction.setCount(amount)).apply(EnchantWithLevelsFunction.enchantWithLevels(provider, enchant));
    }

    public static UniformContainerBase.Builder<?> enchantedItem(ItemLike item, int weight, Holder<ContextIntProvider> amount, HolderGetter<Enchantment> provider) {
        return LootItem.lootTableItem(item).setWeight(weight).apply(SetItemCountFunction.setCount(amount)).apply(EnchantRandomlyFunction.randomApplicableEnchantment(provider));
    }

    public static UniformContainerBase.Builder<?> suspiciousStew(int weight, Holder<ContextIntProvider> amount) {
        return LootItem.lootTableItem(Items.SUSPICIOUS_STEW).setWeight(weight).apply(SetItemCountFunction.setCount(amount)).apply(SetStewEffectFunction.stewEffect().withEffect(MobEffects.NIGHT_VISION, number(7, 10)).withEffect(MobEffects.JUMP_BOOST, number(7, 10)).withEffect(MobEffects.WEAKNESS, number(6, 8)).withEffect(MobEffects.BLINDNESS, number(5, 7)).withEffect(MobEffects.POISON, number(10, 20)).withEffect(MobEffects.SATURATION, number(7, 10)));
    }

    public static UniformContainerBase.Builder<?> potion(int weight, Holder<Potion> potion, Holder<ContextIntProvider> amount) {
        return LootItem.lootTableItem(Items.POTION).setWeight(weight).apply(SetItemCountFunction.setCount(amount)).apply(SetPotionFunction.setPotion(potion));
    }

    public static UniformContainerBase.Builder<?> splashPotion(int weight, Holder<Potion> potion, Holder<ContextIntProvider> amount) {
        return LootItem.lootTableItem(Items.SPLASH_POTION).setWeight(weight).apply(SetItemCountFunction.setCount(amount)).apply(SetPotionFunction.setPotion(potion));
    }

    public static UniformContainerBase.Builder<?> lingeringPotion(int weight, Holder<Potion> potion, Holder<ContextIntProvider> amount) {
        return LootItem.lootTableItem(Items.LINGERING_POTION).setWeight(weight).apply(SetItemCountFunction.setCount(amount)).apply(SetPotionFunction.setPotion(potion));
    }

    public static Holder<ContextIntProvider> one() {
        return number(1);
    }

    @Deprecated // use the one in ContextIntProviders directly
    public static Holder<ContextIntProvider> number(int amount) {
        return ContextIntProviders.exactly(amount);
    }

    @Deprecated // use the one in ContextIntProviders directly
    public static Holder<ContextIntProvider> number(int min, int max) {
        return ContextIntProviders.between(min, max);
    }

    public static LootPool.Builder pool(Holder<ContextIntProvider> rolls) {
        return LootPool.lootPool().setRolls(rolls);
    }
}
