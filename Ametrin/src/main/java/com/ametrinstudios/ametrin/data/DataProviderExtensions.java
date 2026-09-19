package com.ametrinstudios.ametrin.data;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockItemTagId;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;

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
        return getItemResourceKey(item).identifier();
    }

    public static Identifier getBlockKey(Block block) {
        return getBlockResouceKey(block).identifier();
    }

    public static ResourceKey<Item> getItemResourceKey(Item item) {
        return BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow();
    }

    public static ResourceKey<Item> getItemResourceKey(ItemLike item) {
        return getItemResourceKey(item.asItem());
    }

    public static ResourceKey<Block> getBlockResouceKey(Block block) {
        return BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow();
    }

    public static BlockItemTagId getColorBlockItemTag(DyeColor color) {
        return new BlockItemTagId(getColorBlockTag(color), color.getDyedTag());
    }
    public static TagKey<Block> getColorBlockTag(DyeColor color) {
        return switch (color) {
            case DyeColor.WHITE -> Tags.Blocks.DYED_WHITE;
            case DyeColor.LIGHT_GRAY -> Tags.Blocks.DYED_LIGHT_GRAY;
            case DyeColor.GRAY -> Tags.Blocks.DYED_GRAY;
            case DyeColor.BLACK -> Tags.Blocks.DYED_BLACK;
            case DyeColor.BROWN -> Tags.Blocks.DYED_BROWN;
            case DyeColor.RED -> Tags.Blocks.DYED_RED;
            case DyeColor.ORANGE -> Tags.Blocks.DYED_ORANGE;
            case DyeColor.YELLOW -> Tags.Blocks.DYED_YELLOW;
            case DyeColor.LIME -> Tags.Blocks.DYED_LIME;
            case DyeColor.GREEN -> Tags.Blocks.DYED_GREEN;
            case DyeColor.CYAN -> Tags.Blocks.DYED_CYAN;
            case DyeColor.LIGHT_BLUE -> Tags.Blocks.DYED_LIGHT_BLUE;
            case DyeColor.BLUE -> Tags.Blocks.DYED_BLUE;
            case DyeColor.PURPLE -> Tags.Blocks.DYED_PURPLE;
            case DyeColor.MAGENTA -> Tags.Blocks.DYED_MAGENTA;
            case DyeColor.PINK -> Tags.Blocks.DYED_PINK;
        };
    }
}
