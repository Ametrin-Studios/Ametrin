package com.ametrinstudios.ametrin.world.item.helper;

import com.ametrinstudios.ametrin.world.entity.boat.BoatVariant;
import com.ametrinstudios.ametrin.world.entity.boat.BoatVariants;
import com.ametrinstudios.ametrin.world.entity.boat.CustomBoatType;
import com.ametrinstudios.ametrin.world.item.CustomBoatItem;

import java.util.function.Supplier;

// TODO: rename -> ItemRegisterHelper
public class ItemRegistryHelper {
    private static Supplier<CustomBoatItem> chestBoat(CustomBoatType type) {return boat(type, BoatVariants.CHEST);}
    private static Supplier<CustomBoatItem> boat(CustomBoatType type) {return boat(type, BoatVariants.DEFAULT);}
    private static Supplier<CustomBoatItem> boat(CustomBoatType type, BoatVariant<?> variant) {return ()-> new CustomBoatItem(type, variant);}

}
