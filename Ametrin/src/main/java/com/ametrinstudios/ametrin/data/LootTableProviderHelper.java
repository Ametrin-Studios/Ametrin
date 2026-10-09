package com.ametrinstudios.ametrin.data;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.level.storage.loot.entries.UniformContainerBase;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.providers.number.ints.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

public final class LootTableProviderHelper {
    public static UniformContainerBase.Builder<?> empty(int weight) {
        return EmptyLootItem.emptyItem().setWeight(weight);
    }

    public static UniformContainerBase.Builder<?> item(ItemLike item) {
        return LootItem.lootTableItem(item);
    }

    public static UniformContainerBase.Builder<?> item(ItemLike item, Holder<ContextIntProvider> amount) {
        return applySetCount(item(item), amount);
    }

    @Deprecated
    public static UniformContainerBase.Builder<?> item(ItemLike item, int weight, Holder<ContextIntProvider> amount) {
        return item(item, amount).setWeight(weight);
    }

    @Deprecated
    public static UniformContainerBase.Builder<?> tag(HolderSet<Item> tagKey, int weight, Holder<ContextIntProvider> amount) {
        return tag(tagKey, amount).setWeight(weight);
    }

    public static UniformContainerBase.Builder<?> tag(HolderSet<Item> tagKey, Holder<ContextIntProvider> amount) {
        return applySetCount(TagEntry.expandTag(tagKey), amount);
    }

    public static UniformContainerBase.Builder<?> enchantedItem(ItemLike item, Holder<ContextIntProvider> amount, HolderGetter<Enchantment> provider, Holder<ContextIntProvider> levels) {
        return item(item, amount).apply(EnchantWithLevelsFunction.enchantWithLevels(provider, levels));
    }

    public static UniformContainerBase.Builder<?> enchantedItem(ItemLike item, Holder<ContextIntProvider> amount, HolderGetter<Enchantment> provider) {
        return item(item, amount).apply(EnchantRandomlyFunction.randomApplicableEnchantment(provider));
    }

    public static UniformContainerBase.Builder<?> suspiciousStew(Holder<ContextIntProvider> amount) {
        return item(Items.SUSPICIOUS_STEW, amount).apply(SetStewEffectFunction.stewEffect().withEffect(MobEffects.NIGHT_VISION, number(7, 10)).withEffect(MobEffects.JUMP_BOOST, number(7, 10)).withEffect(MobEffects.WEAKNESS, number(6, 8)).withEffect(MobEffects.BLINDNESS, number(5, 7)).withEffect(MobEffects.POISON, number(10, 20)).withEffect(MobEffects.SATURATION, number(7, 10)));
    }

    public static UniformContainerBase.Builder<?> potion(Holder<Potion> potion, Holder<ContextIntProvider> amount) {
        return item(Items.POTION, amount).apply(SetPotionFunction.setPotion(potion));
    }

    public static UniformContainerBase.Builder<?> splashPotion(Holder<Potion> potion, Holder<ContextIntProvider> amount) {
        return item(Items.SPLASH_POTION, amount).apply(SetPotionFunction.setPotion(potion));
    }

    public static UniformContainerBase.Builder<?> lingeringPotion(Holder<Potion> potion, Holder<ContextIntProvider> amount) {
        return item(Items.LINGERING_POTION, amount).apply(SetPotionFunction.setPotion(potion));
    }

    public static UniformContainerBase.Builder<?> applySetCount(UniformContainerBase.Builder<?> builder, Holder<ContextIntProvider> amount) {
        if (!(amount instanceof Holder.Direct(
                ContextIntProvider provider, DataComponentMap _
        ) && provider instanceof ConstantValue(int value) && value == 1)) {
            builder.apply(SetItemCountFunction.setCount(amount));
        }
        return builder;
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

    @Deprecated
    public static LootPool.Builder pool(Holder<ContextIntProvider> rolls) {
        return LootPool.lootPool().setRolls(rolls);
    }
}
