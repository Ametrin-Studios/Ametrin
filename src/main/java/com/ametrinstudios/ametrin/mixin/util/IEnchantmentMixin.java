package com.ametrinstudios.ametrin.mixin.util;

import net.minecraft.world.entity.EquipmentSlot;

public interface IEnchantmentMixin {
    EquipmentSlot[] getApplicableSlots();
}