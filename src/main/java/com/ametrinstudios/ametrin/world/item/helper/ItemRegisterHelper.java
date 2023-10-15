package com.ametrinstudios.ametrin.world.item.helper;

import com.ametrinstudios.ametrin.util.Extensions;
import com.ametrinstudios.ametrin.world.entity.boat.BoatVariant;
import com.ametrinstudios.ametrin.world.entity.boat.BoatVariants;
import com.ametrinstudios.ametrin.world.entity.boat.CustomBoatType;
import com.ametrinstudios.ametrin.world.item.CustomBoatItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;

import java.awt.*;
import java.util.function.Supplier;

public class ItemRegisterHelper {
    public static Supplier<CustomBoatItem> chestBoat(CustomBoatType type) {return boat(type, BoatVariants.CHEST);}
    public static Supplier<CustomBoatItem> boat(CustomBoatType type) {return boat(type, BoatVariants.DEFAULT);}
    public static Supplier<CustomBoatItem> boat(CustomBoatType type, BoatVariant<?> variant) {return ()-> new CustomBoatItem(type, variant);}
    public static Supplier<ForgeSpawnEggItem> spawnEgg(Supplier<? extends EntityType<? extends Mob>> type, Color backgroundColor, Color highlightColor, Item.Properties properties) {
        return ()-> new ForgeSpawnEggItem(type, Extensions.ColorToInt(backgroundColor), Extensions.ColorToInt(highlightColor), properties);
    }
}
