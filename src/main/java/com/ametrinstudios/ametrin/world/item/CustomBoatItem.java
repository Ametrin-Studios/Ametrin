package com.ametrinstudios.ametrin.world.item;

import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BoatItem;

public class CustomBoatItem extends BoatItem {

    public CustomBoatItem(boolean hasChest, Boat.Type type, Properties properties) {
        super(hasChest, type, properties);
    }
    public CustomBoatItem(boolean hasChest, Boat.Type type){
        this(hasChest, type, new Properties().stacksTo(1));
    }

    public boolean isChestBoat() {
        return hasChest;
    }
}
