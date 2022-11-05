package com.ametrinstudios.ametrin.mixin;

import com.ametrinstudios.ametrin.util.mixin.IMixinEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Enchantment.class)
public abstract class MixinEnchantment implements IMixinEnchantment {
    @Shadow EquipmentSlot[] slots;
    @Override
    public EquipmentSlot[] getApplicableSlots() {return slots;}
    @Override
    public boolean hasEnchantment(LivingEntity entity){
        for(EquipmentSlot slot : slots) {
            ItemStack itemStack = entity.getItemBySlot(slot);
            if (!itemStack.isEmpty() && itemStack.getEnchantmentLevel((Enchantment) (Object)this) > 0) {
                return true;
            }
        }
        return false;
    }
}