package com.ametrinstudios.ametrin;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Objects;

public class AmUtil {
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ArrayList<String> plankIndicators = new ArrayList<>();

    public static ResourceLocation location(String key) {return new ResourceLocation(Ametrin.ModID, key);}

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


    public static String getItemName(Item item) {return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).getPath();}
    public static String getBlockName(Block block) {return Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath();}
}