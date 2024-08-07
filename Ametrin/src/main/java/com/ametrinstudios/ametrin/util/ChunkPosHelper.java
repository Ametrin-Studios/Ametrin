package com.ametrinstudios.ametrin.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;

@SuppressWarnings("unused")
public final class ChunkPosHelper {
    public static BlockPos chunkPosToBlockPos(ChunkPos chunkPos, int y) { return new BlockPos(chunkPos.getMinBlockX(), y, chunkPos.getMinBlockZ()); }

    public static BlockPos chunkPosToBlockPosFromHeightMap(ChunkPos chunkPos, Heightmap.Types heightmapType, ChunkGenerator chunkGenerator, LevelHeightAccessor heightAccessor, RandomState randomState){
        var pos = chunkPos.getWorldPosition();
        return pos.atY(chunkGenerator.getBaseHeight(pos.getX(), pos.getZ(), heightmapType, heightAccessor, randomState));
    }
}
