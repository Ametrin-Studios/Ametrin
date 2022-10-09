package com.ametrinstudios.ametrin.util;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.Map;

public class Extensions {
    /**
     * call during {@link FMLCommonSetupEvent#enqueueWork}
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
}