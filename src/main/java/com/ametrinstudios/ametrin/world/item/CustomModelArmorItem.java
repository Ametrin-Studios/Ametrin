package com.ametrinstudios.ametrin.world.item;

import com.ametrinstudios.ametrin.util.CustomModelArmor;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class CustomModelArmorItem extends CustomArmorItem {

    public CustomModelArmorItem(CustomModelArmor armor, EquipmentSlot slot, Properties properties) {
        super(armor, slot, properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot slot, HumanoidModel<?> original) {
                if(((CustomModelArmor)Armor).getModel(slot) == null) {return original;}
                return ((CustomModelArmor)Armor).getModel(slot);
            }

            @Override
            public @NotNull Model getGenericArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot slot, HumanoidModel<?> original) {
                HumanoidModel<?> replacement = getHumanoidArmorModel(livingEntity, itemStack, slot, original);
                if (replacement == original){
                    return original;
                }

                setModelProperties(replacement, original, slot);
                return replacement;
            }

            @SuppressWarnings("unchecked")
            public static <T extends LivingEntity> void setModelProperties(HumanoidModel<?> replacement, HumanoidModel<T> original, EquipmentSlot slot){
                original.copyPropertiesTo((HumanoidModel<T>) replacement);
                replacement.head.visible = slot == EquipmentSlot.HEAD;
                replacement.hat.visible = slot == EquipmentSlot.HEAD;
                replacement.body.visible = slot == EquipmentSlot.CHEST || slot == EquipmentSlot.LEGS;
                replacement.rightArm.visible = slot == EquipmentSlot.CHEST;
                replacement.leftArm.visible = slot == EquipmentSlot.CHEST;
                replacement.rightLeg.visible = slot == EquipmentSlot.LEGS || slot == EquipmentSlot.FEET;
                replacement.leftLeg.visible = slot == EquipmentSlot.LEGS || slot == EquipmentSlot.FEET;
            }
        });
    }
}