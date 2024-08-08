package com.ametrinstudios.ametrin.data.provider.loot_table;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

@SuppressWarnings("unused")
public abstract class ExtendedBlockLootSubProvider extends BlockLootSubProvider {
    protected ExtendedBlockLootSubProvider(HolderLookup.Provider registries) {
        this(Set.of(), registries);
    }
    protected ExtendedBlockLootSubProvider(Set<Item> explosionResistant, HolderLookup.Provider registries) {
        super(explosionResistant, FeatureFlags.REGISTRY.allFlags(), registries);
    }

    protected void dropOther(DeferredBlock<? extends Block> block, ItemLike other) {
        dropOther(block.get(), other);
    }
    protected void dropLeaveLoot(Block block, SaplingBlock sapling) {
        add(block, createLeavesDrops(block, sapling, NORMAL_LEAVES_SAPLING_CHANCES));
    }
    protected void dropDoor(Block door) {
        add(door, createDoorTable(door));
    }
    protected void dropOre(Block block, ItemLike drop) {
        add(block, createOreDrop(block, drop.asItem()));
    }
    protected void dropDoublePlant(Block plant) {
        add(plant, createSinglePropConditionTable(plant, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
    }

    protected void dropDoublePlantOther(Block plant, ItemLike drop) {
        add(plant, createSinglePropConditionTableDropOther(plant, drop, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
    }

    protected void dropCampfire(Block campfireBlock, ItemLike charcoal) {
        add(campfireBlock, createSilkTouchDispatchTable(campfireBlock, applyExplosionCondition(campfireBlock, LootItem.lootTableItem(charcoal).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2))))));
    }

    protected <T extends Comparable<T> & StringRepresentable> LootTable.Builder createSinglePropConditionTableDropOther(Block block, ItemLike dropItem, Property<T> property, T validValue) {
        return LootTable.lootTable().withPool(applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(dropItem)
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, validValue))))));
    }

    protected final void dropSelf(final Block... blocks) {
        dropSelf(Arrays.stream(blocks).iterator());
    }

    @SafeVarargs
    protected final void dropSelf(final DeferredBlock<? extends Block>... blocks) {
        dropSelf(Arrays.stream(blocks).map(DeferredHolder::get).iterator());
    }

    protected  void dropSelf(Iterator<? extends Block> blocks) {
        blocks.forEachRemaining(this::dropSelf);
    }

    protected void dropSelf(final DeferredBlock<? extends Block> block) {
        dropSelf(block.get());
    }
    @Override
    protected abstract @NotNull Iterable<Block> getKnownBlocks();
}