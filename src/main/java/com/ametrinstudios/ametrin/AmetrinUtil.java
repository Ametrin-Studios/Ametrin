package com.ametrinstudios.ametrin;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class AmetrinUtil {
    public static final Logger LOGGER = LogUtils.getLogger();

    /**
     * blocks containing strings from this list try to use the plank texture in some cases look at the usages to find out where exactly
     */
    public static ArrayList<String> plankIndicators = new ArrayList<>();

    public static ResourceLocation location(String key) {return new ResourceLocation(Ametrin.ModID, key);}

    public static int ColorToInt(Color color){
        return color.getBlue() + (color.getGreen() * 256) + (color.getRed() * 65536);
    }

    public static boolean usePlankTexture(String name) {
        for(String indicator : plankIndicators){
            if(name.contains(indicator)){
                return true;
            }
        }
        return false;
    }
    public static boolean shouldAppendS(String name) {return (name.contains("brick") && !name.contains("bricks") || (name.contains("tile") && !name.contains("tiles")));}
    public static boolean isWood(String name) {return (name.contains("wood") || name.contains("hyphae"));}
    public static boolean isLog(String name) {return (name.contains("log") || name.contains("stem"));}
    public static boolean isWooden(String name) {return isLog(name) || isWood(name) || name.contains("plank") || usePlankTexture(name);}


    public static String getItemName(Item item) {return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).getPath();}
    public static String getBlockName(Block block) {return Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath();}
}