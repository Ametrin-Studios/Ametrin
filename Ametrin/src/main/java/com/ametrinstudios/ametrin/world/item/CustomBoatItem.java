package com.ametrinstudios.ametrin.world.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractBoat;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.Item;

@Deprecated(forRemoval = true)
public class CustomBoatItem extends BoatItem {
    public CustomBoatItem(EntityType<? extends AbstractBoat> entityType, Item.Properties properties) {
        super(entityType, properties);
    }

    public CustomBoatItem(EntityType<? extends AbstractBoat> entityType) {
        this(entityType, new Properties().stacksTo(1));
    }
}
