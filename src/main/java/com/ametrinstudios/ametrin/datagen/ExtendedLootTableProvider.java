package com.ametrinstudios.ametrin.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.data.loot.packs.VanillaLootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class ExtendedLootTableProvider extends LootTableProvider {
    protected final String modID;
    private final List<SubProviderEntry> tables;
    public ExtendedLootTableProvider(PackOutput packOutput, String modID, final List<SubProviderEntry> tables) {
        super(packOutput, Set.of(), VanillaLootTableProvider.create(packOutput).getTables());
        this.modID = modID;
        this.tables = tables;
    }

    public abstract class ExtendedBlockLootSubProvider extends BlockLootSubProvider{
        protected ExtendedBlockLootSubProvider() {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags());
        }

        @SafeVarargs
        protected final <T extends Block> void dropSelf(final T... blocks) {Arrays.stream(blocks).forEach(this::dropSelf);}
        protected  <T extends Block> void dropSelf(Iterator<T> blocks) {blocks.forEachRemaining(this::dropSelf);}
        @Override
        protected abstract @NotNull Iterable<Block> getKnownBlocks();
    }

    public abstract class ExtendedLootTableSubProvider implements LootTableSubProvider{
        protected LootPoolEntryContainer.Builder<?> item(Item item, int weight, NumberProvider amount){
            return LootItem.lootTableItem(item).setWeight(weight).apply(SetItemCountFunction.setCount(amount));
        }
        protected LootPoolEntryContainer.Builder<?> enchantedItem(Item item, int weight, NumberProvider enchant, NumberProvider amount){
            return LootItem.lootTableItem(item).setWeight(weight).apply(SetItemCountFunction.setCount(amount)).apply(EnchantWithLevelsFunction.enchantWithLevels(enchant));
        }
        protected LootPoolEntryContainer.Builder<?> enchantedItem(Item item, int weight, NumberProvider amount){
            return LootItem.lootTableItem(item).setWeight(weight).apply(SetItemCountFunction.setCount(amount)).apply(EnchantRandomlyFunction.randomApplicableEnchantment());
        }
        protected LootPoolEntryContainer.Builder<?> suspiciousStew(int weight, NumberProvider amount){
            return LootItem.lootTableItem(Items.SUSPICIOUS_STEW).setWeight(weight).apply(SetItemCountFunction.setCount(amount)).apply(SetStewEffectFunction.stewEffect().withEffect(MobEffects.NIGHT_VISION, number(7, 10)).withEffect(MobEffects.JUMP, number(7, 10)).withEffect(MobEffects.WEAKNESS, number(6, 8)).withEffect(MobEffects.BLINDNESS, number(5, 7)).withEffect(MobEffects.POISON, number(10, 20)).withEffect(MobEffects.SATURATION, number(7, 10)));
        }
        protected LootPoolEntryContainer.Builder<?> potion(int weight, Potion potion, NumberProvider amount){
            return LootItem.lootTableItem(Items.POTION).setWeight(weight).apply(SetItemCountFunction.setCount(amount)).apply(SetPotionFunction.setPotion(potion));
        }
        protected LootPoolEntryContainer.Builder<?> splashPotion(int weight, Potion potion, NumberProvider amount){
            return LootItem.lootTableItem(Items.SPLASH_POTION).setWeight(weight).apply(SetItemCountFunction.setCount(amount)).apply(SetPotionFunction.setPotion(potion));
        }
        protected LootPoolEntryContainer.Builder<?> lingeringPotion(int weight, Potion potion, NumberProvider amount){
            return LootItem.lootTableItem(Items.LINGERING_POTION).setWeight(weight).apply(SetItemCountFunction.setCount(amount)).apply(SetPotionFunction.setPotion(potion));
        }

        protected ResourceLocation location(String key) {return baseLocation("chests/" + key);}
    }

    protected static NumberProvider one() {return ConstantValue.exactly(1);}
    protected static NumberProvider number(int amount) {return ConstantValue.exactly(amount);}
    protected static NumberProvider number(int minAmount, int maxAmount) {return UniformGenerator.between(minAmount, maxAmount);}
    protected static LootPool.Builder pool(NumberProvider rolls) {return LootPool.lootPool().setRolls(rolls);}

    @Override @Nonnull
    public List<SubProviderEntry> getTables() {return tables;}

    protected ResourceLocation baseLocation(String key) {return new ResourceLocation(modID, key);}
}