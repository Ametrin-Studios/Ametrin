package com.ametrinstudios.ametrin.world.item.helper;

import com.ametrinstudios.ametrin.util.Extensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;

import java.awt.*;
import java.util.function.Supplier;

public class ItemRegisterHelper {
    public static Supplier<DeferredSpawnEggItem> spawnEgg(Supplier<? extends EntityType<? extends Mob>> type, Color backgroundColor, Color highlightColor, Item.Properties properties) {
        return ()-> new DeferredSpawnEggItem(type, Extensions.ColorToInt(backgroundColor), Extensions.ColorToInt(highlightColor), properties);
    }
}
