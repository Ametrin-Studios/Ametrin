package com.ametrinstudios.ametrin.data.provider;

import com.ametrinstudios.ametrin.data.DataProviderExtensions;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.tags.BlockItemTagsProvider;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockItemTagId;
import net.minecraft.tags.BlockItemTags;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.ametrinstudios.ametrin.data.DataProviderExtensions.*;

public abstract class ExtendedBlockItemTagsProvider extends BlockItemTagsProvider {
    @Deprecated
    public ArrayList<ResourceKey<Block>> excludedBlocks = new ArrayList<>();
    @Deprecated
    public ArrayList<BlockItemTagProviderRule> blockItemTagProviderRules = new ArrayList<>();

    protected ExtendedBlockItemTagsProvider(Function<BlockItemTagId, CombinedAppender> tagSupplier) {
        super(tagSupplier);
    }

    @Override
    protected abstract void run();

    protected void runRules(DeferredRegister.Blocks blockRegistry) {
        runRules(blockRegistry.getEntries().stream());
    }

    protected void runRules(Stream<DeferredHolder<Block, ? extends Block>> blocks) {
        blocks.forEach(holder -> {
            final var key = holder.getKey();

            if (excludedBlocks.contains(key)) {
                return;
            }

            final var name = key.identifier().getPath();
            final var block = holder.get();
            final var blockItemId = BlockItemId.create(key.identifier(), getItemKey(holder.get().asItem()));

            for (var provider : blockItemTagProviderRules) {
                provider.generate(holder, blockItemId);
            }

            if (name.contains("wool")) {
                tag(BlockItemTags.DAMPENS_VIBRATIONS).add(blockItemId);
            }

            if (block instanceof LeavesBlock) {
                tag(BlockItemTags.LEAVES).add(blockItemId);
            }
            if (block instanceof SaplingBlock) {
                tag(BlockItemTags.SAPLINGS).add(blockItemId);
            }
            if (block instanceof FlowerBlock) {
                tag(BlockItemTags.SMALL_FLOWERS).add(blockItemId);
            }
            if (name.contains("planks")) {
                tag(BlockItemTags.PLANKS).add(blockItemId);
            }
        });
    }

    public void tagColorCollection(ColorCollection<BlockItemId> items) {
        ColorCollection.zipApply(ColorCollection.VALUES, items, (color, item) -> tag(DataProviderExtensions.getColorBlockItemTag(color)).add(item));
    }

    public void tagBlockFamily(BlockFamily family) {
        family.getVariants().forEach((variant, block) -> {
            tagVariant(variant, getBlockItemId(block));
        });
    }

    public void tagBlockFamilyIgnoring(BlockFamily family, Set<BlockFamily.Variant> ignored) {
        family.getVariants().forEach((variant, block) -> {
            if (ignored.contains(variant)) return;
            tagVariant(variant, getBlockItemId(block));
        });
    }

    public void tagVariant(BlockFamily.Variant variant, BlockItemId id) {
        var isWooden = isWooden(id.block().identifier().getPath());
        var tag = switch (variant) {
            case STAIRS -> isWooden ? BlockItemTags.WOODEN_STAIRS : BlockItemTags.STAIRS;
            case SLAB -> isWooden ? BlockItemTags.WOODEN_SLABS : BlockItemTags.SLABS;
            case WALL -> BlockItemTags.WALLS;
            case FENCE, CUSTOM_FENCE -> {
                if (isWooden) {
                    yield BlockItemTags.WOODEN_FENCES;
                } else {
                    tag(BlockItemTags.FENCES).add(id); // c:fences does not include this
                    yield new BlockItemTagId(Tags.Blocks.FENCES, Tags.Items.FENCES);
                }
            }
            case FENCE_GATE, CUSTOM_FENCE_GATE -> {
                if (isWooden) {
                    tag(BlockItemTags.FENCE_GATES).add(id);
                    yield new BlockItemTagId(Tags.Blocks.FENCE_GATES_WOODEN, Tags.Items.FENCE_GATES_WOODEN);
                } else {
                    yield new BlockItemTagId(Tags.Blocks.FENCE_GATES, Tags.Items.FENCE_GATES);
                }
            }
            case BUTTON -> isWooden ? BlockItemTags.WOODEN_BUTTONS : BlockItemTags.BUTTONS;
            case PRESSURE_PLATE -> isWooden ? BlockItemTags.WOODEN_PRESSURE_PLATES : null;
            case CARPET -> BlockItemTags.WOOL_CARPETS;
            case DOOR -> isWooden ? BlockItemTags.WOODEN_DOORS : BlockItemTags.DOORS;
            case TRAPDOOR -> isWooden ? BlockItemTags.WOODEN_TRAPDOORS : BlockItemTags.TRAPDOORS;
            case HANGING_SIGN, CUSTOM_HANGING_SIGN -> BlockItemTags.HANGING_SIGNS;
            case LOG, STRIPPED_LOG -> BlockItemTags.LOGS;
            case SIGN, WALL_SIGN -> BlockItemTags.SIGNS;
//            case WALL_HANGING_SIGN, CUSTOM_WALL_HANGING_SIGN -> null;
            default -> null;
        };

        if (tag != null) {
            tag(tag).add(id);
        }
    }
}
