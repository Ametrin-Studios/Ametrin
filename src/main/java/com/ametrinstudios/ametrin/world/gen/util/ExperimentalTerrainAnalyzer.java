package com.ametrinstudios.ametrin.world.gen.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;

import java.util.function.Predicate;

/**
 * everything is now {@link TerrainAnalyzer}
 */
@Deprecated(forRemoval = true)
public class ExperimentalTerrainAnalyzer {
    /**
     * @return the average height between the corner points and weather the difference is larger than the threshold, always true on {@link FlatLevelSource}
     */
    public static Pair<Float, Boolean> isFlatEnough(BlockPos pos, Vec3i size, int padding, int threshold, ChunkGenerator generator, LevelHeightAccessor heightAccessor, RandomState randomState){
        if(generator instanceof FlatLevelSource) {return Pair.of((float)generator.getBaseHeight(pos.getX(), pos.getZ(), Heightmap.Types.OCEAN_FLOOR_WG, heightAccessor, randomState), true);}

        int x1 = pos.getX()+padding;
        int x2 = pos.getX()+size.getX()-padding;
        int z1 = pos.getZ()+padding;
        int z2 = pos.getZ()+size.getZ()-padding;


        int height1 = generator.getBaseHeight(x1, z1, Heightmap.Types.OCEAN_FLOOR_WG, heightAccessor, randomState);
        int height2 = generator.getBaseHeight(x2, z1, Heightmap.Types.OCEAN_FLOOR_WG, heightAccessor, randomState);
        int height3 = generator.getBaseHeight(x2, z2, Heightmap.Types.OCEAN_FLOOR_WG, heightAccessor, randomState);
        int height4 = generator.getBaseHeight(x1, z2, Heightmap.Types.OCEAN_FLOOR_WG, heightAccessor, randomState);

        float averageHeight = (height1+height2+height3+height4)/4f;
        float averageHeightDifference = (Math.abs(averageHeight-height1)+Math.abs(averageHeight-height2)+Math.abs(averageHeight-height3)+Math.abs(averageHeight-height4))/4f;
        return Pair.of(averageHeight, averageHeightDifference < threshold);
    }

    public static boolean isUnderwater(BlockPos pos, ChunkGenerator generator, int depth, LevelHeightAccessor heightAccessor, RandomState randomState) {
        return getBlockAt(pos.above(depth), generator, heightAccessor, randomState).is(Blocks.WATER);
    }

    public static boolean areNearbyBiomesValid(BiomeSource biomeSource, BlockPos pos, ChunkGenerator generator, int radius, Predicate<Holder<Biome>> biomePredicate, RandomState randomState){
        for(Holder<Biome> biome : biomeSource.getBiomesWithin(pos.getX(), generator.getSeaLevel(), pos.getZ(), radius, randomState.sampler())) {
            if (!biomePredicate.test(biome)) {return false;}
        }
        return true;
    }

    public static BlockState getBlockAt(BlockPos pos, ChunkGenerator generator, LevelHeightAccessor heightAccessor, RandomState randomState) {return generator.getBaseColumn(pos.getX(), pos.getZ(), heightAccessor, randomState).getBlock(pos.getY());}
}