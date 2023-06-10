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
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.function.Predicate;

public class TerrainAnalyzer{

    /**
     * @return the average height between the corner points and weather the difference is larger than the threshold, always true on {@link FlatLevelSource}
     */
    public static Pair<Float, Boolean> isFlatEnough(BlockPos pos, Vec3i size, int padding, int threshold, Context context){
        if(context.generator instanceof FlatLevelSource) {return Pair.of((float)context.generator().getBaseHeight(pos.getX(), pos.getZ(), Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor(), context.randomState()), true);}

        int x1 = pos.getX()+padding;
        int x2 = pos.getX()+size.getX()-padding;
        int z1 = pos.getZ()+padding;
        int z2 = pos.getZ()+size.getZ()-padding;


        int height1 = context.generator().getBaseHeight(x1, z1, Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor(), context.randomState());
        int height2 = context.generator().getBaseHeight(x2, z1, Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor(), context.randomState());
        int height3 = context.generator().getBaseHeight(x2, z2, Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor(), context.randomState());
        int height4 = context.generator().getBaseHeight(x1, z2, Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor(), context.randomState());

        float averageHeight = (height1+height2+height3+height4)/4f;
        float averageHeightDifference = (Math.abs(averageHeight-height1)+Math.abs(averageHeight-height2)+Math.abs(averageHeight-height3)+Math.abs(averageHeight-height4))/4f;
        return Pair.of(averageHeight, averageHeightDifference < threshold);
    }

    public static boolean isUnderwater(BlockPos pos, int depth, Context context) {
        return getBlockAt(pos.above(depth), context).is(Blocks.WATER);
    }

    public static boolean isGroundLevelAbove(BlockPos pos, int height, Context context){
        return isGroundLevelAbove(pos.getX(), pos.getZ(), height, context);
    }
    public static boolean isGroundLevelAbove(int x, int z, int height, Context context){
        return context.generator().getBaseHeight(x, z, Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor(), context.randomState()) > height;
    }

    public static boolean isGroundLevelBelow(BlockPos pos, int height, Context context){
        return isGroundLevelBelow(pos.getX(), pos.getZ(), height, context);
    }
    public static boolean isGroundLevelBelow(int x, int z, int height, Context context){
        return context.generator().getBaseHeight(x, z, Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor(), context.randomState()) < height;
    }

    public static boolean areNearbyBiomesValid(BlockPos pos, int radius, Predicate<Holder<Biome>> biomePredicate, BiomeSource biomeSource, ChunkGenerator generator, RandomState randomState){
        for(Holder<Biome> biome : biomeSource.getBiomesWithin(pos.getX(), generator.getSeaLevel(), pos.getZ(), radius, randomState.sampler())) {
            if (!biomePredicate.test(biome)) {return false;}
        }
        return true;
    }

    public static BlockState getBlockAt(BlockPos pos, Context context) {return getBlockAt(pos.getX(), pos.getY(), pos.getZ(), context);}
    public static BlockState getBlockAt(int x, int y, int z, Context context) {return context.generator().getBaseColumn(x, z, context.heightAccessor(), context.randomState()).getBlock(y);}

    public static Context context(ChunkGenerator generator, LevelHeightAccessor heightAccessor, RandomState randomState) {
        return new Context(generator, heightAccessor, randomState);
    }
    public static Context context(Structure.GenerationContext generationContext) {
        return context(generationContext.chunkGenerator(), generationContext.heightAccessor(), generationContext.randomState());
    }

    public record Context(ChunkGenerator generator, LevelHeightAccessor heightAccessor, RandomState randomState) {}
}