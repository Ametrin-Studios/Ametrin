package com.ametrinstudios.ametrin.world.gen.util;

import com.ametrinstudios.ametrin.AmetrinUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;

import java.util.function.Predicate;

public class TerrainAnalyzer {
    public static final Settings defaultCheckSettings = new Settings(1, 3, 3);

    public static boolean isFlatEnough(ChunkPos chunkPos, ChunkGenerator chunkGenerator, LevelHeightAccessor heightAccessor, RandomState randomState) {return isFlatEnough(chunkPos, chunkGenerator, defaultCheckSettings, heightAccessor, randomState);}

    public static boolean isFlatEnough(ChunkPos chunkPos, ChunkGenerator chunkGenerator, Settings settings, LevelHeightAccessor heightAccessor, RandomState randomState){
        return isFlatEnough(ChunkPosToBlockPos(chunkPos, Heightmap.Types.WORLD_SURFACE_WG, chunkGenerator, heightAccessor, randomState), chunkGenerator, settings, heightAccessor, randomState);
    }
    public static boolean isFlatEnough(BlockPos pos, ChunkGenerator chunkGenerator, Settings settings, LevelHeightAccessor heightAccessor, RandomState randomState){
        if(getBlockAt(pos.below(), chunkGenerator, heightAccessor, randomState).is(Blocks.WATER)) {return false;}

        int columSpreading = settings.columSpreading();
        if(isColumBlocked(pos.east(columSpreading), settings, chunkGenerator, heightAccessor, randomState)) {return false;}
        if(isColumBlocked(pos.west(columSpreading), settings, chunkGenerator, heightAccessor, randomState)) {return false;}
        if(isColumBlocked(pos.south(columSpreading), settings, chunkGenerator, heightAccessor, randomState)) {return false;}
        return !isColumBlocked(pos.north(columSpreading), settings, chunkGenerator, heightAccessor, randomState);
    }

    public static boolean isUnderwater(ChunkPos chunkPos, ChunkGenerator chunkGenerator, int depth, LevelHeightAccessor heightAccessor, RandomState randomState) {
        return getBlockAt(ChunkPosToBlockPos(chunkPos, Heightmap.Types.OCEAN_FLOOR_WG, chunkGenerator, heightAccessor, randomState).above(depth), chunkGenerator, heightAccessor, randomState).is(Blocks.WATER);
    }

    public static boolean isGroundLevelAbove(ChunkPos chunkPos, ChunkGenerator chunkGenerator, int height, LevelHeightAccessor heightAccessor, RandomState randomState){
        return getSurfaceLevelAt(chunkPos, Heightmap.Types.WORLD_SURFACE_WG, chunkGenerator, heightAccessor, randomState) > height;
    }

    public static boolean isGroundLevelBelow(ChunkPos chunkPos, ChunkGenerator chunkGenerator, int height, LevelHeightAccessor heightAccessor, RandomState randomState){
        return getSurfaceLevelAt(chunkPos, Heightmap.Types.WORLD_SURFACE_WG, chunkGenerator, heightAccessor, randomState) < height;
    }

    public static boolean areNearbyBiomesValid(BiomeSource biomeSource, ChunkPos chunkPos, ChunkGenerator generator, int radius, Predicate<Holder<Biome>> validBiome, RandomState randomState){
        for(Holder<Biome> biome : biomeSource.getBiomesWithin(chunkPos.getMiddleBlockX(), generator.getSeaLevel(), chunkPos.getMiddleBlockZ(), radius, randomState.sampler())) {
            if (!validBiome.test(biome)) {return false;}
        }
        return true;
    }

    protected static boolean isColumBlocked(BlockPos pos, Settings settings, ChunkGenerator chunkGenerator, LevelHeightAccessor heightAccessor, RandomState randomState){
        if(!isDownwardsFree(pos, settings.stepSize(), settings.steps(), chunkGenerator, heightAccessor, randomState)){
            return isUpwardsBlocked(pos, settings.stepSize(), settings.steps(), chunkGenerator, heightAccessor, randomState);
        }
        return true;
    }

    protected static boolean isUpwardsBlocked(BlockPos pos, int stepSize, int steps, ChunkGenerator chunkGenerator, LevelHeightAccessor heightAccessor, RandomState randomState){
        for(int i = 1; i <= steps; i++){
            if(!getBlockAt(pos.above(i * stepSize), chunkGenerator, heightAccessor, randomState).isAir()) {return true;}
        }
        return false;
    }

    protected static boolean isDownwardsFree(BlockPos pos, int stepSize, int steps, ChunkGenerator chunkGenerator, LevelHeightAccessor heightAccessor, RandomState randomState){
        for(int i = 1; i <= steps; i++){
            if(getBlockAt(pos.below(i * stepSize), chunkGenerator, heightAccessor, randomState).isAir()) {return true;}
        }
        return false;
    }

    protected static BlockState getBlockAt(int x, int y, int z, ChunkGenerator chunkGenerator, LevelHeightAccessor heightAccessor, RandomState randomState) {return chunkGenerator.getBaseColumn(x, z, heightAccessor, randomState).getBlock(y);}
    protected static BlockState getBlockAt(BlockPos pos, ChunkGenerator chunkGenerator, LevelHeightAccessor heightAccessor, RandomState randomState) {return chunkGenerator.getBaseColumn(pos.getX(), pos.getZ(), heightAccessor, randomState).getBlock(pos.getY());}
    protected static BlockPos ChunkPosToBlockPos(ChunkPos chunkPos, Heightmap.Types heightmapType, ChunkGenerator chunkGenerator, LevelHeightAccessor heightAccessor, RandomState randomState) {return AmetrinUtil.ChunkPosToBlockPosFromHeightMap(chunkPos, chunkGenerator, heightmapType, heightAccessor, randomState);}

    protected static int getSurfaceLevelAt(ChunkPos pos, Heightmap.Types heightmapType, ChunkGenerator chunkGenerator, LevelHeightAccessor heightAccessor, RandomState randomState){
        return ChunkPosToBlockPos(pos, heightmapType, chunkGenerator, heightAccessor, randomState).getY();
    }
    protected static int getSurfaceLevelAt(BlockPos pos, Heightmap.Types heightmapType, ChunkGenerator chunkGenerator, LevelHeightAccessor heightAccessor, RandomState randomState){
        return chunkGenerator.getBaseHeight(pos.getX(), pos.getZ(), heightmapType, heightAccessor, randomState);
    }

    public record Settings(int steps, int stepSize, int columSpreading) {}
}