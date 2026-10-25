package com.ametrinstudios.ametrin.data.provider;

import com.ametrinstudios.ametrin.data.BlockTagProviderRule;
import com.ametrinstudios.ametrin.data.DataProviderExtensions;
import com.ametrinstudios.ametrin.util.ColorCollection;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.ametrinstudios.ametrin.data.DataProviderExtensions.getBlockName;
import static com.ametrinstudios.ametrin.data.DataProviderExtensions.isWooden;

public abstract class ExtendedBlockTagsProvider extends BlockTagsProvider {
    public ArrayList<Block> excludedBlocks = new ArrayList<>();
    public ArrayList<BlockTagProviderRule> blockTagProviderRules = new ArrayList<>();

    public ExtendedBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String modID, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, registries, modID, existingFileHelper);
    }

    protected void runRules(DeferredRegister.Blocks blockRegistry) {
        runRules(blockRegistry.getEntries().stream());
    }

    protected void runRules(Stream<DeferredHolder<Block, ? extends Block>> blocks) {
        blocks.forEach(holder -> {
            final var block = holder.get();

            if (excludedBlocks.contains(block)) {
                return;
            }

            final var key = holder.getKey();
            final var name = key.location().getPath();

            for (BlockTagProviderRule provider : blockTagProviderRules) {
                provider.generate(holder, name);
            }

            if (name.contains("wool")) {
                tag(BlockTags.DAMPENS_VIBRATIONS).add(block);
            }
            if (block instanceof CarpetBlock) {
                tag(BlockTags.WOOL_CARPETS).add(block);
            }
            if (block instanceof LeavesBlock) {
                tag(BlockTags.LEAVES).add(block);
            }
            if (block instanceof SaplingBlock) {
                tag(BlockTags.SAPLINGS).add(block);
            }
            if (block instanceof FlowerBlock) {
                tag(BlockTags.SMALL_FLOWERS).add(block);
            }
            if (name.contains("planks")) {
                tag(BlockTags.PLANKS).add(block);
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

    public void tagColorCollection(ColorCollection<? extends Supplier<? extends Block>> blocks) {
        ColorCollection.zipApply(ColorCollection.VALUES, blocks, (color, block) -> tag(DataProviderExtensions.getColorBlockTag(color)).add(block.get()));
    }

    public void tagBlockFamily(BlockFamily family) {
        family.getVariants().forEach(this::tagVariant);
    }

    public void tagBlockFamilyIgnoring(BlockFamily family, Set<BlockFamily.Variant> ignored) {
        family.getVariants().forEach((variant, block) -> {
            if (ignored.contains(variant)) return;
            tagVariant(variant, block);
        });
    }

    public void tagVariant(BlockFamily.Variant variant, Block block) {
        var isWooden = isWooden(getBlockName(block));

        TagKey<Block> tag = switch (variant) {
            case STAIRS -> isWooden ? BlockTags.WOODEN_STAIRS : BlockTags.STAIRS;
            case SLAB -> isWooden ? BlockTags.WOODEN_SLABS : BlockTags.SLABS;
            case WALL -> BlockTags.WALLS;
            case FENCE, CUSTOM_FENCE -> {
                if (isWooden) {
                    yield BlockTags.WOODEN_FENCES;
                } else {
                    tag(BlockTags.FENCES).add(block); // c:fences does not include this
                    yield Tags.Blocks.FENCES;
                }
            }
            case FENCE_GATE, CUSTOM_FENCE_GATE -> {
                if (isWooden) {
                    tag(BlockTags.FENCE_GATES).add(block);
                    yield Tags.Blocks.FENCE_GATES_WOODEN;
                } else {
                    yield Tags.Blocks.FENCE_GATES;
                }
            }
            case BUTTON -> isWooden ? BlockTags.WOODEN_BUTTONS : BlockTags.BUTTONS;
            case PRESSURE_PLATE -> isWooden ? BlockTags.WOODEN_PRESSURE_PLATES : null;
            case DOOR -> isWooden ? BlockTags.WOODEN_DOORS : BlockTags.DOORS;
            case TRAPDOOR -> isWooden ? BlockTags.WOODEN_TRAPDOORS : BlockTags.TRAPDOORS;
            case SIGN, WALL_SIGN -> BlockTags.SIGNS;
            default -> null;
        };

        if (tag != null) {
            tag(tag).add(block);
        }
    }
}
