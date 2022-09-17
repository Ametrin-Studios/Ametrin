package com.ametrinstudios.ametrin.world.item;

import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class ItemNameDoubleHighBlockItem extends DoubleHighBlockItem {
    public ItemNameDoubleHighBlockItem(Block block, Properties properties) {super(block, properties);}

    @Override @NotNull
    public String getDescriptionId() {return this.getOrCreateDescriptionId();}
}