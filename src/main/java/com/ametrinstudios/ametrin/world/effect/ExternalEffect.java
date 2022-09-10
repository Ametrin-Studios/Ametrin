package com.ametrinstudios.ametrin.world.effect;

import com.ametrinstudios.ametrin.AmetrinUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ExternalEffect extends MobEffect {
    protected ExternalEffect(MobEffectCategory category, Color color) {super(category, AmetrinUtil.ColorToInt(color));}

    @Override
    public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {}
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {return true;}
}