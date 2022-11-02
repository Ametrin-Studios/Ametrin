package com.ametrinstudios.ametrin.util;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.function.Supplier;

public class Extensions {
    /**
     * call during {@link FMLCommonSetupEvent#enqueueWork}
     * all given blocks must have an axis property like {@link net.minecraft.world.level.block.RotatedPillarBlock}
     */
    public static void addStrippables(Map<Block, Block> strippables){
        AxeItem.STRIPPABLES = (new ImmutableMap.Builder<Block, Block>()).putAll(AxeItem.STRIPPABLES).putAll(strippables).build();
    }

    /**
     * call during {@link FMLCommonSetupEvent#enqueueWork}
     */
    public static void addBrewingRecipe(Potion startPotion, Item ingredient, Potion resultPotion){
        PotionBrewing.addMix(startPotion, ingredient, resultPotion);
    }

    /**
     * call during {@link FMLCommonSetupEvent#enqueueWork}
     */
    public static void addCompostable(ItemLike item, float chance){
        ComposterBlock.COMPOSTABLES.put(item.asItem(), chance);
    }
    public static void addFlowerPot(RegistryObject<? extends Block> plant, Supplier<? extends FlowerPotBlock> fullPot){
        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(plant.getId(), fullPot);
    }
}