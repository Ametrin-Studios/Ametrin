package com.ametrinstudios.ametrin.world.item;

import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.level.block.Block;

// is handled by ItemProperties now
@Deprecated(forRemoval = true)
public class ItemNameDoubleHighBlockItem extends DoubleHighBlockItem {
    public ItemNameDoubleHighBlockItem(Block block, Properties properties) { super(block, properties); }
}