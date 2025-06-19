package com.ametrinstudios.ametrin.data.provider;

import com.ametrinstudios.ametrin.data.BlockTagProviderRule;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.BlockItemTagsProvider;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.ametrinstudios.ametrin.data.DataProviderExtensions.getBlockName;
import static com.ametrinstudios.ametrin.data.DataProviderExtensions.isWooden;

public abstract class ExtendedBlockItemTagsProvider extends BlockItemTagsProvider {
    public ArrayList<Block> excludedBlocks = new ArrayList<>();
    public ArrayList<BlockTagProviderRule> blockItemTagProviderRules = new ArrayList<>();

    @Override
    protected abstract void run();

    @Override
    @ParametersAreNonnullByDefault
    protected abstract @NotNull TagAppender<Block, Block> tag(@NotNull TagKey<Block> blockTag, @NotNull TagKey<Item> itemTag);

    protected void runRules(DeferredRegister.Blocks blockRegistry) {
        runRules(blockRegistry.getEntries().stream().map(Supplier::get));
    }

    protected void runRules(Stream<? extends Block> blocks) {
        blocks.forEach(block -> {
            if (excludedBlocks.contains(block)) {
                return;
            }
            final var name = getBlockName(block);

            for (var provider : blockItemTagProviderRules) {
                provider.generate(block, name);
            }

            if (block instanceof StairBlock) {
                if (isWooden(name)) {
                    tag(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS).add(block);
                } else {
                    tag(BlockTags.STAIRS, ItemTags.STAIRS).add(block);
                }
            }
            if (block instanceof SlabBlock) {
                if (isWooden(name)) {
                    tag(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS).add(block);
                } else {
                    tag(BlockTags.SLABS, ItemTags.SLABS).add(block);
                }
            }
            if (block instanceof WallBlock) {
                tag(BlockTags.WALLS, ItemTags.WALLS).add(block);
            }
            if (block instanceof FenceBlock) {
                if (isWooden(name)) {
                    tag(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES).add(block);
                } else {
                    tag(BlockTags.FENCES, ItemTags.FENCES).add(block);
                }
            }
            if (block instanceof FenceGateBlock) {
                if (isWooden(name)) {
                    tag(Tags.Blocks.FENCE_GATES_WOODEN, Tags.Items.FENCE_GATES_WOODEN).add(block);
                } else {
                    tag(BlockTags.FENCE_GATES, ItemTags.FENCE_GATES).add(block);
                }
            }
            if (block instanceof ButtonBlock) {
                if (isWooden(name)) {
                    tag(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS).add(block);
                } else {
                    tag(BlockTags.BUTTONS, ItemTags.BUTTONS).add(block);
                }
            }
            if (block instanceof PressurePlateBlock) {
                if (isWooden(name)) {
                    tag(BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES).add(block);
                }
            }
            if (block instanceof DoorBlock) {
                if (isWooden(name)) {
                    tag(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS).add(block);
                } else {
                    tag(BlockTags.DOORS, ItemTags.DOORS).add(block);
                }
            }
            if (block instanceof TrapDoorBlock) {
                if (isWooden(name)) {
                    tag(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS).add(block);
                } else {
                    tag(BlockTags.TRAPDOORS, ItemTags.TRAPDOORS).add(block);
                }
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

    public static class BlockToItemConverter implements TagAppender<Block, Block> {
        private final TagAppender<Item, Item> itemAppender;

        public BlockToItemConverter(TagAppender<Item, Item> itemAppender) {
            this.itemAppender = itemAppender;
        }

        public @NotNull TagAppender<Block, Block> add(Block block) {
            this.itemAppender.add(Objects.requireNonNull(block.asItem()));
            return this;
        }

        public @NotNull TagAppender<Block, Block> addOptional(Block block) {
            this.itemAppender.addOptional(Objects.requireNonNull(block.asItem()));
            return this;
        }

        private static TagKey<Item> blockTagToItemTag(TagKey<Block> tagKey) {
            return TagKey.create(Registries.ITEM, tagKey.location());
        }

        @Override
        public @NotNull TagAppender<Block, Block> addTag(@NotNull TagKey<Block> tagKey) {
            this.itemAppender.addTag(blockTagToItemTag(tagKey));
            return this;
        }

        @Override
        public @NotNull TagAppender<Block, Block> addOptionalTag(@NotNull TagKey<Block> tagKey) {
            this.itemAppender.addOptionalTag(blockTagToItemTag(tagKey));
            return this;
        }

        @Override
        public @NotNull TagAppender<Block, Block> add(@NotNull TagEntry entry) {
            itemAppender.add(entry);
            return this;
        }

        @Override
        public @NotNull TagAppender<Block, Block> replace(boolean value) {
            itemAppender.replace(value);
            return this;
        }

        @Override
        public @NotNull TagAppender<Block, Block> remove(Block block) {
            itemAppender.remove(block.asItem());
            return this;
        }

        @Override
        public @NotNull TagAppender<Block, Block> remove(@NotNull TagKey<Block> tag) {
            itemAppender.remove(blockTagToItemTag(tag));
            return this;
        }
    }
}
