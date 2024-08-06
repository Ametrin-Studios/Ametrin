package com.ametrinstudios.ametrin.util;

import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class VanillaCompat {
    private static final Map<Block, Block> _strippableRequests = new HashMap<>();
    private static boolean _pushed = false;

    /**
     * Registers FlowerPots
     */
    public static void addFlowerPot(ResourceLocation plant, Supplier<? extends FlowerPotBlock> fullPot){
        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(plant, fullPot);
    }
    public static void addFlowerPot(DeferredBlock<? extends Block> plant, Supplier<? extends FlowerPotBlock> fullPot){
        addFlowerPot(plant.getId(), fullPot);
    }

    /**
     * Allows blocks to be converted by an axe (e.g. oak log to stripped oak log)
     * both blocks need to have the {@link RotatedPillarBlock#AXIS} state (stupid minecraft...)
     * call during {@link FMLCommonSetupEvent}
     */
    public static void addStrippable(Block log, Block strippedLog){
        if(_pushed){
            throw new UnsupportedOperationException("Strippables must be registered during FMLCommonSetupEvent");
        }
        _strippableRequests.put(log, strippedLog);
    }

    /**
     * Allows blocks to be converted by a shovel (e.g. grass block to path block)
     */

    @Deprecated(forRemoval = true)
    private static void addFlattenable(Block block, BlockState flattenedBlockState){
    }

    /**
     * Allows blocks to be converted by a shovel (e.g. grass block to path block)
     */
    @Deprecated(forRemoval = true)
    private static void addFlattenable(Block block, Block flattenedBlock) {
    }

    public interface Flammable {
        static void add(Block block, int encouragement, int flammability) {
            ((FireBlock)Blocks.FIRE).setFlammable(block, encouragement, flammability);
        }
        static void addLog(Block log) { add(log, 5, 5); }
        static void addPlank(Block plank) { add(plank, 5, 20); }
        static void addLeave(Block leave) { add(leave, 30, 60); }
        static void addPlant(Block plant) { add(plant, 60, 100); }
        static void addWool(Block wool) { add(wool, 30, 60); }
        static void addCarpet(Block carpet) { add(carpet, 60, 20); }
    }


    @ApiStatus.Internal
    public static void pushRequests(){
        _pushed = true;
        AxeItem.STRIPPABLES = (new ImmutableMap.Builder<Block, Block>()).putAll(AxeItem.STRIPPABLES).putAll(_strippableRequests).build();
    }
}