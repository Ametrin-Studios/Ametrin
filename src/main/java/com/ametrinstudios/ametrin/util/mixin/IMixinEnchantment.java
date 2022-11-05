package com.ametrinstudios.ametrin.util.mixin;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

public interface IMixinEnchantment {
    EquipmentSlot[] getApplicableSlots();
    boolean hasEnchantment(LivingEntity entity);
}