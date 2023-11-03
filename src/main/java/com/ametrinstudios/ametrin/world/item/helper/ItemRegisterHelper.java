package com.ametrinstudios.ametrin.world.item.helper;

import com.ametrinstudios.ametrin.util.Extensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;

import java.awt.*;
import java.util.function.Supplier;

public class ItemRegisterHelper {
    public static Supplier<ForgeSpawnEggItem> spawnEgg(Supplier<? extends EntityType<? extends Mob>> type, Color backgroundColor, Color highlightColor, Item.Properties properties) {
        return ()-> new ForgeSpawnEggItem(type, Extensions.ColorToInt(backgroundColor), Extensions.ColorToInt(highlightColor), properties);
    }
}
