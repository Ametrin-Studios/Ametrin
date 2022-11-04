package com.ametrinstudios.ametrin.util.mixin;

import net.minecraft.world.entity.EquipmentSlot;

public interface IMixinEnchantment {
    EquipmentSlot[] getApplicableSlots();
}