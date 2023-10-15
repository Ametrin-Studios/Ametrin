package com.ametrinstudios.ametrin.world.item;

import com.ametrinstudios.ametrin.util.Extensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.common.ForgeSpawnEggItem;

import java.awt.*;
import java.util.function.Supplier;

@Deprecated(forRemoval = true)
public class ColorSpawnEgg extends ForgeSpawnEggItem {
    public ColorSpawnEgg(Supplier<? extends EntityType<? extends Mob>> type, Color backgroundColor, Color highlightColor, Properties properties) {
        super(type, Extensions.ColorToInt(backgroundColor), Extensions.ColorToInt(highlightColor), properties);
    }
}