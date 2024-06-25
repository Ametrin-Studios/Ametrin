package com.ametrinstudios.ametrin.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

@Deprecated(forRemoval = true)
public final class VanillaCompat {

    /**
     * @param chance
     * Cake: 1
     * Bread: 0.85
     * Apple/Flower: 0.65
     * Vines: 0.5
     * Sapling/Seed: 0.3
     */
    @Deprecated(forRemoval = true)
    public static void addCompostable(ItemLike item, float chance){
    }

    /**
     * Registers FlowerPots
     */
    @Deprecated(forRemoval = true)
    public static void addFlowerPot(ResourceLocation plant, Supplier<? extends FlowerPotBlock> fullPot){
        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(plant, fullPot);
    }

    /**
     * Allows blocks to be converted by an axe (e.g. oak log to stripped oak log)
     * both blocks need to have the {@link RotatedPillarBlock#AXIS} state (stupid minecraft...)
     */
    @Deprecated(forRemoval = true)
    public static void addStrippable(Block log, Block strippedLog){
    }

    /**
     * Allows blocks to be converted by a shovel (e.g. grass block to path block)
     */

    @Deprecated(forRemoval = true)
    public static void addFlattenable(Block block, BlockState flattenedBlockState){
    }

    /**
     * Allows blocks to be converted by a shovel (e.g. grass block to path block)
     */
    @Deprecated(forRemoval = true)
    public static void addFlattenable(Block block, Block flattenedBlock){
    }

    public interface Flammable{
        static void add(Block block, int encouragement, int flammability){
            ((FireBlock)Blocks.FIRE).setFlammable(block, encouragement, flammability);
        }
        static void addLog(Block log) {add(log, 5, 5);}
        static void addPlank(Block plank) {add(plank, 5, 20);}
        static void addLeave(Block leave) {add(leave, 30, 60);}
        static void addPlant(Block plant) {add(plant, 60, 100);}
        static void addWool(Block wool) {add(wool, 30, 60);}
        static void addCarpet(Block carpet) {add(carpet, 60, 20);}
    }
}