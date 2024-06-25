package com.ametrinstudios.ametrin.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;

import java.awt.*;

public class Extensions {
    public static int ColorToInt(Color color) { return color.getBlue() + (color.getGreen() * 256) + (color.getRed() * 65536); }
    public static int ColorToIntWithAlpha(Color color) { return (color.getAlpha()) + (color.getBlue()*256) + (color.getGreen() * 65536) + (color.getRed() * 16777216); }

    public static int SecondsToTicks(int seconds) { return seconds * 20; }
    public static int MinutesToTicks(int minutes) { return SecondsToTicks(minutes * 60); }

    public static BlockPos ChunkPosToBlockPos(ChunkPos chunkPos, int y) { return new BlockPos(chunkPos.getMinBlockX(), y, chunkPos.getMinBlockZ()); }

    public static BlockPos ChunkPosToBlockPosFromHeightMap(ChunkPos chunkPos, Heightmap.Types heightmapType, ChunkGenerator chunkGenerator, LevelHeightAccessor heightAccessor, RandomState randomState){
        var pos = chunkPos.getWorldPosition();
        return pos.atY(chunkGenerator.getBaseHeight(pos.getX(), pos.getZ(), heightmapType, heightAccessor, randomState));
    }
}
