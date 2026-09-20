//package com.ametrinstudios.ametrin.data.provider.loot_table;
//
//import net.minecraft.core.HolderGetter;
//import net.minecraft.core.registries.Registries;
//import net.minecraft.data.loot.LootTableSubProvider;
//import net.minecraft.world.item.Instrument;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.enchantment.Enchantment;
//import net.minecraft.world.level.biome.Biome;
//import net.minecraft.world.level.levelgen.structure.Structure;
//
//public abstract class ExtendedLootSubProvider implements LootTableSubProvider {
//    protected final LootTableSubProvider.Context output;
//    protected final HolderGetter<Item> items;
//    protected final HolderGetter<Biome> biomes;
//    protected final HolderGetter<Enchantment> enchantments;
//    protected final HolderGetter<Instrument> instruments;
//    protected final HolderGetter<Structure> structures;
//
//    public ExtendedLootSubProvider(LootTableSubProvider.Context output) {
//        this.output = output;
//        this.items = output.lookup(Registries.ITEM);
//        this.biomes = output.lookup(Registries.BIOME);
//        this.enchantments = output.lookup(Registries.ENCHANTMENT);
//        this.instruments = output.lookup(Registries.INSTRUMENT);
//        this.structures = output.lookup(Registries.STRUCTURE);
//    }
//}
