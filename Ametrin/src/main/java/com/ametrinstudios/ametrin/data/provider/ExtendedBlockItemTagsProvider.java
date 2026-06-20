package com.ametrinstudios.ametrin.data.provider;

import com.ametrinstudios.ametrin.data.BlockTagProviderRule;
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
import java.util.function.Function;
import java.util.stream.Stream;

import static com.ametrinstudios.ametrin.data.DataProviderExtensions.isWooden;

public abstract class ExtendedBlockItemTagsProvider extends BlockItemTagsProvider {
    public ArrayList<ResourceKey<Block>> excludedBlocks = new ArrayList<>();
    public ArrayList<BlockTagProviderRule> blockItemTagProviderRules = new ArrayList<>();

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
            final var blockItemId = BlockItemId.create(key.identifier(), key.identifier());

            for (var provider : blockItemTagProviderRules) {
                provider.generate(holder, name);
            }

            if (block instanceof StairBlock) {
                if (isWooden(name)) {
                    tag(BlockItemTags.WOODEN_STAIRS).add(blockItemId);
                } else {
                    tag(BlockItemTags.STAIRS).add(blockItemId);
                }
            }
            if (block instanceof SlabBlock) {
                if (isWooden(name)) {
                    tag(BlockItemTags.WOODEN_SLABS).add(blockItemId);
                } else {
                    tag(BlockItemTags.SLABS).add(blockItemId);
                }
            }
            if (block instanceof WallBlock) {
                tag(BlockItemTags.WALLS).add(blockItemId);
            }
            if (block instanceof FenceBlock) {
                if (isWooden(name)) {
                    tag(BlockItemTags.WOODEN_FENCES).add(blockItemId); // c:fences/wooden includes this
                } else {
                    tag(BlockItemTags.FENCES).add(blockItemId); // c:fences does not include this
                    tag(new BlockItemTagId(Tags.Blocks.FENCES, Tags.Items.FENCES)).add(blockItemId);
                }
            }
            if (block instanceof FenceGateBlock) {
                if (isWooden(name)) {
                    // those tags don't pull from each other
                    // minecraft:fence_gates are only wooden fence gates and is contained in furnace fuels
                    tag(new BlockItemTagId(Tags.Blocks.FENCE_GATES_WOODEN, Tags.Items.FENCE_GATES_WOODEN)).add(blockItemId);
                    tag(BlockItemTags.FENCE_GATES).add(blockItemId);
                } else {
                    tag(new BlockItemTagId(Tags.Blocks.FENCE_GATES, Tags.Items.FENCE_GATES)).add(blockItemId);
                }
            }
            if (block instanceof ButtonBlock) {
                if (isWooden(name)) {
                    tag(BlockItemTags.WOODEN_BUTTONS).add(blockItemId);
                } else {
                    tag(BlockItemTags.BUTTONS).add(blockItemId);
                }
            }
            if (block instanceof PressurePlateBlock) {
                if (isWooden(name)) {
                    tag(BlockItemTags.WOODEN_PRESSURE_PLATES).add(blockItemId);
                }
            }
            if (block instanceof DoorBlock) {
                if (isWooden(name)) {
                    tag(BlockItemTags.WOODEN_DOORS).add(blockItemId);
                } else {
                    tag(BlockItemTags.DOORS).add(blockItemId);
                }
            }
            if (block instanceof TrapDoorBlock) {
                if (isWooden(name)) {
                    tag(BlockItemTags.WOODEN_TRAPDOORS).add(blockItemId);
                } else {
                    tag(BlockItemTags.TRAPDOORS).add(blockItemId);
                }
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

//    public static class BlockToItemConverter implements TagAppender<Block, Block> {
//        private final TagAppender<Item, Item> itemAppender;
//
//        public BlockToItemConverter(TagAppender<Item, Item> itemAppender) {
//            this.itemAppender = itemAppender;
//        }
//
//        public @NotNull TagAppender<Block, Block> add(Block block) {
//            this.itemAppender.add(Objects.requireNonNull(block.asItem()));
//            return this;
//        }
//
//        public @NotNull TagAppender<Block, Block> addOptional(Block block) {
//            this.itemAppender.addOptional(Objects.requireNonNull(block.asItem()));
//            return this;
//        }
//
//        private static TagKey<Item> blockTagToItemTag(TagKey<Block> tagKey) {
//            return TagKey.create(Registries.ITEM, tagKey.location());
//        }
//
//        @Override
//        public @NotNull TagAppender<Block, Block> addTag(@NotNull TagKey<Block> tagKey) {
//            this.itemAppender.addTag(blockTagToItemTag(tagKey));
//            return this;
//        }
//
//        @Override
//        public @NotNull TagAppender<Block, Block> addOptionalTag(@NotNull TagKey<Block> tagKey) {
//            this.itemAppender.addOptionalTag(blockTagToItemTag(tagKey));
//            return this;
//        }
//
//        @Override
//        public @NotNull TagAppender<Block, Block> add(@NotNull TagEntry entry) {
//            itemAppender.add(entry);
//            return this;
//        }
//
//        @Override
//        public @NotNull TagAppender<Block, Block> replace(boolean value) {
//            itemAppender.replace(value);
//            return this;
//        }
//
//        @Override
//        public @NotNull TagAppender<Block, Block> remove(Block block) {
//            itemAppender.remove(block.asItem());
//            return this;
//        }
//
//        @Override
//        public @NotNull TagAppender<Block, Block> remove(@NotNull TagKey<Block> tag) {
//            itemAppender.remove(blockTagToItemTag(tag));
//            return this;
//        }
//    }
}
