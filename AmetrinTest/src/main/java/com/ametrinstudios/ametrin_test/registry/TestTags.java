package com.ametrinstudios.ametrin_test.registry;

import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public final class TestTags {
    public interface Blocks {
        TagKey<Block> TEST_PORTAL_FRAMES = create("test_portal_frames");

        private static TagKey<Block> create(String key) {
            return BlockTags.create(AmetrinTestMod.locate(key));
        }
    }

    public interface Biomes {
        TagKey<Biome> TEST_BIOMES = create("test_biomes");

        private static TagKey<Biome> create(String key) {
            return TagKey.create(Registries.BIOME, AmetrinTestMod.locate(key));
        }
    }
}
