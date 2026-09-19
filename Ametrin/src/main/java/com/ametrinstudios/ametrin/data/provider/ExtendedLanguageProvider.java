package com.ametrinstudios.ametrin.data.provider;

import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;

public abstract class ExtendedLanguageProvider extends LanguageProvider {
    public ExtendedLanguageProvider(PackOutput output, String mod_id, String locale) {
        super(output, mod_id, locale);
        if(locale.contains("-")) throw new IllegalStateException("locals should contain _ not -");
    }

    protected void add(ItemLike itemLike, String name) {
        add(itemLike.asItem(), name);
    }

    public void addHumanized(DeferredHolder<Item, ? extends Item> item) {
        addHumanized(item.get(), item.getId().getPath());
    }

    public void addHumanized(TagKey<?> tag) {
        add(tag, humanize(tag.location().getPath()));
    }

    public void addHumanized(ItemLike block, String id) {
        add(block, humanize(id));
    }

    public static String humanize(String id) {
        var nameBuffer = id.toCharArray();
        var wasSpace = true;
        for (var i = 0; i < nameBuffer.length; i++) {
            if (nameBuffer[i] == '_' || nameBuffer[i] == '/') {
                nameBuffer[i] = ' ';
            } else if (wasSpace) {
                nameBuffer[i] = Character.toUpperCase(nameBuffer[i]);
            }
            wasSpace = nameBuffer[i] == ' ';
        }

        return new String(nameBuffer);
    }

    public FamilyBuilder family(String name) {
        return new FamilyBuilder(this, name);
    }

    public static final class FamilyBuilder {
        private final ExtendedLanguageProvider provider;
        private final String name;

        public FamilyBuilder(ExtendedLanguageProvider provider, String name) {
            this.provider = provider;
            this.name = name;
        }

        public FamilyBuilder family(BlockFamily family) {
            family.getVariants().forEach((variant, block) -> {
                switch (variant) {
                    case BUTTON -> button(block);
                    case STAIRS -> stairs(block);
                    case SLAB -> slab(block);
                    case WALL -> wall(block);
                    case FENCE, CUSTOM_FENCE -> fence(block);
                    case FENCE_GATE, CUSTOM_FENCE_GATE -> fenceGate(block);
                }
            });

            return this;
        }

        public FamilyBuilder stairs(ItemLike stairs) {
            provider.add(stairs, name + " Stairs");
            return this;
        }

        public FamilyBuilder slab(ItemLike slab) {
            provider.add(slab, name + " Slab");
            return this;
        }

        public FamilyBuilder wall(ItemLike slab) {
            provider.add(slab, name + " Wall");
            return this;
        }

        public FamilyBuilder fence(ItemLike slab) {
            provider.add(slab, name + " Fence");
            return this;
        }

        public FamilyBuilder fenceGate(ItemLike slab) {
            provider.add(slab, name + " Fence Gate");
            return this;
        }

        public FamilyBuilder button(ItemLike button) {
            provider.add(button, name + " Button");
            return this;
        }

        public FamilyBuilder bars(ItemLike bars) {
            provider.add(bars, name + " Bars");
            return this;
        }

        public FamilyBuilder chain(ItemLike chain) {
            provider.add(chain, name + " Chain");
            return this;
        }

        public FamilyBuilder grate(ItemLike grate) {
            provider.add(grate, name + " Grate");
            return this;
        }
    }
}
