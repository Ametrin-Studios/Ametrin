package com.ametrinstudios.ametrin.util;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

public class BannerBuilder {
    private final Item _bannerItem;
    private final HolderGetter<BannerPattern> lookup;
    private final BannerPatternLayers.Builder _layers = new BannerPatternLayers.Builder();

    public BannerBuilder(Item item, HolderGetter<BannerPattern> lookup) {
        _bannerItem = item;
        this.lookup = lookup;
    }

    public BannerBuilder addPattern(ResourceKey<BannerPattern> pattern, DyeColor color) {
        return addPattern(lookup.get(pattern).orElseThrow(), color);
    }

    public BannerBuilder addPattern(Holder<BannerPattern> pattern, DyeColor color) {
        _layers.add(pattern, color);
        return this;
    }

    public ItemStackTemplate build() {
        return new ItemStackTemplate(_bannerItem, DataComponentPatch.builder().set(DataComponents.BANNER_PATTERNS, _layers.build()).build());
    }
}
