package com.ametrinstudios.ametrin.data.provider.loot_table;

import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableSubProvider;
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
import net.minecraft.world.level.storage.loot.predicates.MatchBlock;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public abstract class ExtendedBlockLootSubProvider extends BlockLootSubProvider {
    protected ExtendedBlockLootSubProvider(LootTableSubProvider.Context output) {
        this(Set.of(), output);
    }

    protected ExtendedBlockLootSubProvider(Set<Item> explosionResistant, LootTableSubProvider.Context output) {
        super(explosionResistant, FeatureFlags.REGISTRY.allFlags(), output);
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
        add(campfireBlock, createSilkTouchDispatchTable(campfireBlock, applyExplosionCondition(campfireBlock, LootItem.lootTableItem(charcoal).apply(SetItemCountFunction.setCount(ContextIntProviders.exactly(2))))));
    }

    protected <T extends Comparable<T> & StringRepresentable> LootTable.Builder createSinglePropConditionTableDropOther(Block block, ItemLike drop, Property<T> property, T value) {
        return LootTable.lootTable().withPool(applyExplosionCondition(block, LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
                .add(LootItem.lootTableItem(drop)
                        .when(MatchBlock.blockMatches(blocks, block,
                                StatePropertiesPredicate.Builder.properties().hasProperty(property, value)
                        ))
                ))
        );
    }

    protected final void dropSelf(final Block... blocks) {
        dropSelf(Arrays.stream(blocks));
    }

    @SafeVarargs
    protected final void dropSelf(final DeferredBlock<? extends Block>... blocks) {
        dropSelf(Arrays.stream(blocks).map(DeferredHolder::get));
    }

    protected void dropSelf(Stream<? extends Block> blocks) {
        blocks.forEach(this::dropSelf);
    }

    protected void dropSelf(final DeferredBlock<? extends Block> block) {
        dropSelf(block.get());
    }

    @Override
    protected abstract @NotNull Iterable<Block> getKnownBlocks();
}