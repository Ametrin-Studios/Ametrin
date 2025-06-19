package com.ametrinstudios.ametrin.data.provider;

import com.ametrinstudios.ametrin.data.BlockTagProviderRule;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.ametrinstudios.ametrin.data.DataProviderExtensions.getBlockName;

public abstract class ExtendedBlockTagsProvider extends BlockTagsProvider {
    public ArrayList<Block> excludedBlocks = new ArrayList<>();
    public ArrayList<BlockTagProviderRule> blockTagProviderRules = new ArrayList<>();

    public ExtendedBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String modID) {
        super(output, registries, modID);
    }

    protected void runRules(DeferredRegister.Blocks blockRegistry) {
        runRules(blockRegistry.getEntries().stream().map(Supplier::get));
    }

    protected void runRules(Stream<? extends Block> blocks) {
        blocks.forEach(block -> {
            if (excludedBlocks.contains(block)) {
                return;
            }
            final var name = getBlockName(block);

            for (BlockTagProviderRule provider : blockTagProviderRules) {
                provider.generate(block, name);
            }

            if (block instanceof FlowerPotBlock) {
                tag(BlockTags.FLOWER_POTS).add(block);
            }
            if (block instanceof FireBlock) {
                tag(BlockTags.FIRE).add(block);
            }
            if (block instanceof CampfireBlock) {
                tag(BlockTags.CAMPFIRES).add(block);
            }
            if (block instanceof StandingSignBlock) {
                tag(BlockTags.STANDING_SIGNS).add(block);
            }
            if (block instanceof WallSignBlock) {
                tag(BlockTags.WALL_SIGNS).add(block);
            }
            if (block instanceof CeilingHangingSignBlock) {
                tag(BlockTags.CEILING_HANGING_SIGNS).add(block);
            }
            if (block instanceof WallHangingSignBlock) {
                tag(BlockTags.WALL_HANGING_SIGNS).add(block);
            }
            if (block instanceof CauldronBlock) {
                tag(BlockTags.CAULDRONS).add(block);
            }
            if (block.defaultBlockState().canBeReplaced()) {
                tag(BlockTags.REPLACEABLE).add(block);
            }
        });
    }
}