package com.ametrinstudios.ametrin.world.item;

import com.ametrinstudios.ametrin.AmetrinUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.common.ForgeSpawnEggItem;

import java.awt.*;
import java.util.function.Supplier;

public class ColorSpawnEgg extends ForgeSpawnEggItem {
    public ColorSpawnEgg(Supplier<? extends EntityType<? extends Mob>> type, Color backgroundColor, Color highlightColor, Properties properties) {
        super(type, AmetrinUtil.ColorToInt(backgroundColor), AmetrinUtil.ColorToInt(highlightColor), properties);
    }
}