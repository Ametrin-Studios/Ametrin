package com.ametrinstudios.ametrin;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraftforge.registries.ForgeRegistries;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class AmetrinUtil {
    /**
     * blocks containing strings from this list try to use the plank texture in some cases look at the usages to find out where exactly
     */
    public static ArrayList<String> plankIndicators = new ArrayList<>();

    public static ResourceLocation location(String key) {return new ResourceLocation(Ametrin.ModID, key);}

    public static int ColorToInt(Color color) {return color.getBlue() + (color.getGreen() * 256) + (color.getRed() * 65536);}
    public static int ColorToIntWithAlpha(Color color) {return (color.getAlpha()) + (color.getBlue()*256) + (color.getGreen() * 65536) + (color.getRed() * 16777216);}

    public static int SecondsToTicks(int seconds) {return seconds * 20;}
    public static int MinutesToTicks(int minutes) {return SecondsToTicks(minutes * 60);}

    /**
     * use {@link AmetrinUtil#isPlank(String)} instead
     */
    @Deprecated(forRemoval = true) public static boolean usePlankTexture(String name) {
        return isPlank(name);
    }
    public static boolean isPlank(String name) {
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
    public static boolean isWooden(String name) {return isLog(name) || isWood(name) || name.contains("plank") || isPlank(name);}

    /**
     * use {@link ChunkPos#getWorldPosition()} instead
     */
    @Deprecated(forRemoval = true) public static BlockPos ChunkPosToBlockPos(ChunkPos chunkPos) {return ChunkPosToBlockPos(chunkPos, 0);}
    public static BlockPos ChunkPosToBlockPos(ChunkPos chunkPos, int y) {return new BlockPos(chunkPos.getMinBlockX(), y, chunkPos.getMinBlockZ());}

    public static BlockPos ChunkPosToBlockPosFromHeightMap(ChunkPos chunkPos, ChunkGenerator chunkGenerator, Heightmap.Types heightmapType, LevelHeightAccessor heightAccessor, RandomState randomState){
        BlockPos pos = chunkPos.getWorldPosition();
        return pos.atY(chunkGenerator.getBaseHeight(pos.getX(), pos.getZ(), heightmapType, heightAccessor, randomState));
    }

    public static String getItemName(Item item) {return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).getPath();}
    public static String getBlockName(Block block) {return Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath();}
}