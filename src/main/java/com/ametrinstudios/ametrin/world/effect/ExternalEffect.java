package com.ametrinstudios.ametrin.world.effect;

import com.ametrinstudios.ametrin.util.Extensions;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ExternalEffect extends MobEffect {
    public ExternalEffect(MobEffectCategory category, Color color) {super(category, Extensions.ColorToInt(color));}

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        return super.applyEffectTick(entity, amplifier);
    }
}