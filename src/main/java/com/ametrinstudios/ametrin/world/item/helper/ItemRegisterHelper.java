package com.ametrinstudios.ametrin.world.item.helper;

import com.ametrinstudios.ametrin.util.ColorHelper;
import com.ametrinstudios.ametrin.world.item.CustomBoatItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;

import java.awt.*;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class ItemRegisterHelper {
    public static DeferredSpawnEggItem spawnEgg(Supplier<? extends EntityType<? extends Mob>> type, Color backgroundColor, Color highlightColor) {
        return spawnEgg(type, backgroundColor, highlightColor, new Item.Properties());
    }
    public static DeferredSpawnEggItem spawnEgg(Supplier<? extends EntityType<? extends Mob>> type, Color backgroundColor, Color highlightColor, Item.Properties properties) {
        return new DeferredSpawnEggItem(type, ColorHelper.ColorToInt(backgroundColor), ColorHelper.ColorToInt(highlightColor), properties);
    }

    public static CustomBoatItem boat(Boat.Type type){
        return new CustomBoatItem(false, type);
    }
    public static CustomBoatItem chestBoat(Boat.Type type){
        return new CustomBoatItem(true, type);
    }

    public static CustomBoatItem boat(EnumProxy<Boat.Type> type){
        return boat(type.getValue());
    }
    public static CustomBoatItem chestBoat(EnumProxy<Boat.Type> type){
        return chestBoat(type.getValue());
    }
}
