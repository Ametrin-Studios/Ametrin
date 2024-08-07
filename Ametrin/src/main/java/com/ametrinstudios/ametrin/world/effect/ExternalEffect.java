package com.ametrinstudios.ametrin.world.effect;

import com.ametrinstudios.ametrin.util.ColorHelper;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import java.awt.*;

public class ExternalEffect extends MobEffect {
    public ExternalEffect(MobEffectCategory category, Color color) {
        super(category, ColorHelper.colorToInt(color));
    }

    public ExternalEffect(MobEffectCategory category, Color color, ParticleOptions particle) {
        super(category, ColorHelper.colorToInt(color), particle);
    }
}