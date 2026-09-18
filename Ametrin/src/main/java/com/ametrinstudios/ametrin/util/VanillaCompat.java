package com.ametrinstudios.ametrin.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;

@SuppressWarnings("unused")
public final class VanillaCompat {

    // covered by neo constructor ?
//    public static void addFlowerPot(Identifier plant, Supplier<? extends FlowerPotBlock> fullPot) {
//        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(plant, fullPot);
//    }

    public interface Flammable {
        static void add(Block block, int encouragement, int flammability) {
            ((FireBlock) Blocks.FIRE).setFlammable(block, encouragement, flammability);
        }

        static void addLog(Block log) {
            add(log, 5, 5);
        }

        static void addPlank(Block plank) {
            add(plank, 5, 20);
        }

        static void addLeave(Block leave) {
            add(leave, 30, 60);
        }

        static void addPlant(Block plant) {
            add(plant, 60, 100);
        }

        static void addWool(Block wool) {
            add(wool, 30, 60);
        }

        static void addCarpet(Block carpet) {
            add(carpet, 60, 20);
        }
    }
}