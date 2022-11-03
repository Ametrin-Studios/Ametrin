package com.ametrinstudios.ametrin.mixin;

import com.ametrinstudios.ametrin.mixin.util.IEnchantmentMixin;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin implements IEnchantmentMixin {
    @Shadow EquipmentSlot[] slots;

    @Override
    public EquipmentSlot[] getApplicableSlots() {return slots;}
}