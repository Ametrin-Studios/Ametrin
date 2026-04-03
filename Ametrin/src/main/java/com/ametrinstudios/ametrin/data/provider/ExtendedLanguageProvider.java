package com.ametrinstudios.ametrin.data.provider;

import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.LanguageProvider;

public abstract class ExtendedLanguageProvider extends LanguageProvider {
    public ExtendedLanguageProvider(PackOutput output, String mod_id, String locale) {
        super(output, mod_id, locale);
    }

    protected void add(ItemLike itemLike, String name) {
        add(itemLike.asItem(), name);
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

        public FamilyBuilder button(ItemLike button) {
            provider.add(button, name + " Button");
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
    }
}
