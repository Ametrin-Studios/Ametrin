package com.ametrinstudios.ametrin.data;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.HashSet;

public final class DataProviderExtensions {
    /**
     * blocks containing strings from this list try to use the plank texture in some cases look at the usages to find out where exactly
     */
    private static final HashSet<String> plankIndicators = new HashSet<>();


    public static void addPlankIndicator(String indicator) {
        plankIndicators.add(indicator);
    }

    public static boolean isPlank(String name) {
        for (var indicator : plankIndicators) {
            if (name.contains(indicator)) return true;
        }
        return false;
    }

    public static boolean shouldAppendS(String name) {
        return name.matches(".*brick(?!s).*|.*tile(?!s).*");
    }

    public static boolean isLog(String name) {
        return name.matches(".*(log|stem).*");
    }

    public static boolean isWood(String name) {
        return name.matches(".*(wood|hyphae).*");
    }

    public static boolean isWooden(String name) {
        return isLog(name) || isWood(name) || name.contains("plank") || isPlank(name);
    }

    public static String getItemName(Item item) {
        return getItemKey(item).getPath();
    }

    public static String getBlockName(Block block) {
        return getBlockKey(block).getPath();
    }

    public static Identifier getItemKey(Item item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    public static Identifier getBlockKey(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }
}
