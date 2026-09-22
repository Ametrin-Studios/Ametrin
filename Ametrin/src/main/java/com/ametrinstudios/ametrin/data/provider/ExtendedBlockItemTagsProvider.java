package com.ametrinstudios.ametrin.data.provider;

import com.ametrinstudios.ametrin.data.BlockTagProviderRule;
import com.ametrinstudios.ametrin.data.DataProviderExtensions;
import com.ametrinstudios.ametrin.util.ColorCollection;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.tags.BlockItemTagsProvider;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.ametrinstudios.ametrin.data.DataProviderExtensions.getBlockName;
import static com.ametrinstudios.ametrin.data.DataProviderExtensions.isWooden;

public abstract class ExtendedBlockItemTagsProvider extends BlockItemTagsProvider {
    public ArrayList<Block> excludedBlocks = new ArrayList<>();
    public ArrayList<BlockTagProviderRule> blockItemTagProviderRules = new ArrayList<>();

    @Override
    protected abstract void run();

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
            final var name = key.identifier().getPath();

            for (var provider : blockItemTagProviderRules) {
                provider.generate(holder, name);
            }

            if (name.contains("wool")) {
                tag(BlockTags.DAMPENS_VIBRATIONS, ItemTags.DAMPENS_VIBRATIONS).add(block);
            }

            if (block instanceof CarpetBlock) {
                tag(BlockTags.WOOL_CARPETS, ItemTags.WOOL_CARPETS).add(block);
            }
            if (block instanceof LeavesBlock) {
                tag(BlockTags.LEAVES, ItemTags.LEAVES).add(block);
            }
            if (block instanceof SaplingBlock) {
                tag(BlockTags.SAPLINGS, ItemTags.SAPLINGS).add(block);
            }
            if (block instanceof FlowerBlock) {
                tag(BlockTags.SMALL_FLOWERS, ItemTags.SMALL_FLOWERS).add(block);
            }
            if (name.contains("planks")) {
                tag(BlockTags.PLANKS, ItemTags.PLANKS).add(block);
            }
        });
    }

    public void tagColorCollection(ColorCollection<? extends Supplier<? extends Block>> items) {
        ColorCollection.zipApply(ColorCollection.VALUES, items, (color, block) -> tag(DataProviderExtensions.getColorBlockTag(color), color.getDyedTag()).add(block.get()));
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

        record BlockItemTagId(TagKey<Block> block, TagKey<Item> item) {}
        var tag = switch (variant) {
            case STAIRS ->
                    isWooden ? new BlockItemTagId(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS) : new BlockItemTagId(BlockTags.STAIRS, ItemTags.STAIRS);
            case SLAB ->
                    isWooden ? new BlockItemTagId(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS) : new BlockItemTagId(BlockTags.SLABS, ItemTags.SLABS);
            case WALL -> new BlockItemTagId(BlockTags.WALLS, ItemTags.WALLS);
            case FENCE, CUSTOM_FENCE -> {
                if (isWooden) {
                    yield new BlockItemTagId(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
                } else {
                    tag(BlockTags.FENCES, ItemTags.FENCES).add(block); // c:fences does not include this
                    yield new BlockItemTagId(Tags.Blocks.FENCES, Tags.Items.FENCES);
                }
            }
            case FENCE_GATE, CUSTOM_FENCE_GATE -> {
                if (isWooden) {
                    tag(BlockTags.FENCE_GATES, ItemTags.FENCE_GATES).add(block);
                    yield new BlockItemTagId(Tags.Blocks.FENCE_GATES_WOODEN, Tags.Items.FENCE_GATES_WOODEN);
                } else {
                    yield new BlockItemTagId(Tags.Blocks.FENCE_GATES, Tags.Items.FENCE_GATES);
                }
            }
            case BUTTON ->
                    isWooden ? new BlockItemTagId(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS) : new BlockItemTagId(BlockTags.BUTTONS, ItemTags.BUTTONS);
            case PRESSURE_PLATE ->
                    isWooden ? new BlockItemTagId(BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES) : null;
            case DOOR ->
                    isWooden ? new BlockItemTagId(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS) : new BlockItemTagId(BlockTags.DOORS, ItemTags.DOORS);
            case TRAPDOOR ->
                    isWooden ? new BlockItemTagId(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS) : new BlockItemTagId(BlockTags.TRAPDOORS, ItemTags.TRAPDOORS);
            case SIGN, WALL_SIGN -> new BlockItemTagId(BlockTags.SIGNS, ItemTags.SIGNS);
            default -> null;
        };

        if (tag != null) {
            tag(tag.block, tag.item).add(block);
        }
    }

    public static class BlockToItemConverter implements TagAppender<Block, Block> {
        private final TagAppender<Item, Item> itemAppender;

        public BlockToItemConverter(TagAppender<Item, Item> itemAppender) {
            this.itemAppender = itemAppender;
        }

        public TagAppender<Block, Block> add(Block block) {
            this.itemAppender.add(Objects.requireNonNull(block.asItem()));
            return this;
        }

        public TagAppender<Block, Block> addOptional(Block block) {
            this.itemAppender.addOptional(Objects.requireNonNull(block.asItem()));
            return this;
        }

        private static TagKey<Item> blockTagToItemTag(TagKey<Block> tagKey) {
            return TagKey.create(Registries.ITEM, tagKey.location());
        }

        @Override
        public TagAppender<Block, Block> addTag(TagKey<Block> tagKey) {
            this.itemAppender.addTag(blockTagToItemTag(tagKey));
            return this;
        }

        @Override
        public TagAppender<Block, Block> addOptionalTag(TagKey<Block> tagKey) {
            this.itemAppender.addOptionalTag(blockTagToItemTag(tagKey));
            return this;
        }

        @Override
        public TagAppender<Block, Block> add(TagEntry entry) {
            itemAppender.add(entry);
            return this;
        }

        @Override
        public TagAppender<Block, Block> replace(boolean value) {
            itemAppender.replace(value);
            return this;
        }

        @Override
        public TagAppender<Block, Block> remove(Block block) {
            itemAppender.remove(block.asItem());
            return this;
        }

        @Override
        public TagAppender<Block, Block> remove(TagKey<Block> tag) {
            itemAppender.remove(blockTagToItemTag(tag));
            return this;
        }
    }
}
