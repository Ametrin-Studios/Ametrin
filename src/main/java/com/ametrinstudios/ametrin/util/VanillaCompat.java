package com.ametrinstudios.ametrin.util;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.function.Supplier;

/**
 * all methods should be called during {@link FMLCommonSetupEvent#enqueueWork}
 */
public class VanillaCompat {
    /**
     * all given blocks must have an axis property like {@link net.minecraft.world.level.block.RotatedPillarBlock}
     */
    @Deprecated(forRemoval = true) public static void addStrippables(Map<Block, Block> strippables){
        AxeItem.STRIPPABLES = (new ImmutableMap.Builder<Block, Block>()).putAll(AxeItem.STRIPPABLES).putAll(strippables).build();
    }

    public static void addBrewingRecipe(Potion startPotion, Item ingredient, Potion resultPotion){
        PotionBrewing.addMix(startPotion, ingredient, resultPotion);
    }

    /**
     * @param chance
     * Cake: 1
     * Bread: 0.85
     * Apple/Flower: 0.65
     * Vines: 0.5
     * Sapling/Seed: 0.3
     */
    public static void addCompostable(ItemLike item, float chance){
        ComposterBlock.COMPOSTABLES.put(item.asItem(), chance);
    }
    public static void addFlowerPot(RegistryObject<? extends Block> plant, Supplier<? extends FlowerPotBlock> fullPot){
        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(plant.getId(), fullPot);
    }

    public static void addStrippable(Block log, Block strippedLog){
        //AxeItem.STRIPPABLES = Maps.newHashMap(AxeItem.STRIPPABLES);
        AxeItem.STRIPPABLES.put(log, strippedLog);
    }

    public static void addFlattenable(Block block, BlockState flattenedBlock){
        //ShovelItem.FLATTENABLES = Maps.newHashMap(ShovelItem.FLATTENABLES);
        ShovelItem.FLATTENABLES.put(block, flattenedBlock);
    }

    public static void addFlammable(Block block, int encouragement, int flammability){
        ((FireBlock)Blocks.FIRE).setFlammable(block, encouragement, flammability);
    }
    public static void addFlammableLog(Block log) {addFlammable(log, 5, 5);}
    public static void addFlammablePlank(Block plank) {addFlammable(plank, 5, 20);}
    public static void addFlammableLeave(Block leave) {addFlammable(leave, 30, 60);}
    public static void addFlammablePlant(Block plant) {addFlammable(plant, 60, 100);}
    public static void addFlammableWool(Block wool) {addFlammable(wool, 30, 60);}
    public static void addFlammableCarpet(Block carpet) {addFlammable(carpet, 60, 20);}
}