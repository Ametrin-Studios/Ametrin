package com.ametrinstudios.ametrin.datagen;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.data.loot.packs.VanillaLootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public abstract class ExtendedLootTableProvider extends LootTableProvider {
    protected final String modID;
    public ExtendedLootTableProvider(PackOutput packOutput, String modID) {
        super(packOutput, Set.of(), VanillaLootTableProvider.create(packOutput).getTables());
        this.modID = modID;
    }

    public abstract class ExtendedBlockLootSubProvider extends BlockLootSubProvider{
        private static final float[] LeavesSaplingChances = new float[] {0.05f, 0.0625f, 0.083333336f, 0.1f};

        protected ExtendedBlockLootSubProvider() {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags());
        }


        protected void dropLeaveLoot(Block block, SaplingBlock sapling){add(block, createLeavesDrops(block, sapling, LeavesSaplingChances));}
        protected void dropDoor(DoorBlock door){add(door, createDoorTable(door));}
        protected void dropOre(Block block, ItemLike drop){add(block, createOreDrop(block, drop.asItem()));}
        protected void dropDoublePlant(DoublePlantBlock plant){add(plant, createSinglePropConditionTable(plant, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));}
        protected void dropDoublePlantOther(DoublePlantBlock plant, ItemLike drop){add(plant, createSinglePropConditionTableDropOther(plant, drop, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));}

        protected void dropCampfire(CampfireBlock campfireBlock, ItemLike charcoal){
            add(campfireBlock, createSilkTouchDispatchTable(campfireBlock, applyExplosionCondition(campfireBlock, LootItem.lootTableItem(charcoal).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2))))));

        }
        protected  <T extends Comparable<T> & StringRepresentable> LootTable.Builder createSinglePropConditionTableDropOther(Block block, ItemLike dropItem, Property<T> property, T validValue){
            return LootTable.lootTable().withPool(applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(dropItem)
                            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                    .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, validValue))))));
        }

        @SafeVarargs
        protected final <T extends Block> void dropSelf(final T... blocks) {Arrays.stream(blocks).forEach(this::dropSelf);}
        protected  <T extends Block> void dropSelf(Iterator<T> blocks) {blocks.forEachRemaining(this::dropSelf);}
        @Override
        protected abstract @NotNull Iterable<Block> getKnownBlocks();
    }

    public abstract class ExtendedLootTableSubProvider implements LootTableSubProvider{
        protected LootPoolEntryContainer.Builder<?> item(ItemLike item, int weight, NumberProvider amount){
            return LootItem.lootTableItem(item).setWeight(weight).apply(SetItemCountFunction.setCount(amount));
        }
        protected LootPoolEntryContainer.Builder<?> enchantedItem(ItemLike item, int weight, NumberProvider enchant, NumberProvider amount){
            return LootItem.lootTableItem(item).setWeight(weight).apply(SetItemCountFunction.setCount(amount)).apply(EnchantWithLevelsFunction.enchantWithLevels(enchant));
        }
        protected LootPoolEntryContainer.Builder<?> enchantedItem(ItemLike item, int weight, NumberProvider amount){
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

    public abstract class ExtendedEntityLootSubProvider extends EntityLootSubProvider{

        protected ExtendedEntityLootSubProvider() {
            super(FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected abstract @NotNull Stream<EntityType<?>> getKnownEntityTypes();
    }

    protected static NumberProvider one() {return number(1);}
    protected static NumberProvider number(int amount) {return ConstantValue.exactly(amount);}
    protected static NumberProvider number(int min, int max) {return UniformGenerator.between(min, max);}
    protected static LootPool.Builder pool(NumberProvider rolls) {return LootPool.lootPool().setRolls(rolls);}

    @Override @NotNull
    public abstract List<SubProviderEntry> getTables();

    protected ResourceLocation baseLocation(String key) {return new ResourceLocation(modID, key);}
}