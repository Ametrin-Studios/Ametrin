package com.ametrinstudios.ametrin.world.item;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;

public class CustomModelArmorItem extends CustomArmorItem {
    public CustomModelArmorItem(Holder<ArmorMaterial> p_323783_, Type p_266831_, Properties p_40388_) {
        super(p_323783_, p_266831_, p_40388_);
    }

//    public CustomModelArmorItem(CustomModelArmor armor, ArmorItem.Type type, Properties properties) {
//        super(armor, type, properties);
//    }
//
//    @Override
//    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
//        consumer.accept(new IClientItemExtensions() {
//
//            @Override
//            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot slot, HumanoidModel<?> original) {
//                if(((CustomModelArmor)Armor).getModel(slot) == null) {return original;}
//                return ((CustomModelArmor)Armor).getModel(slot);
//            }
//
//            @Override
//            public @NotNull Model getGenericArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot slot, HumanoidModel<?> original) {
//                HumanoidModel<?> replacement = getHumanoidArmorModel(livingEntity, itemStack, slot, original);
//                if (replacement == original){
//                    return original;
//                }
//
//                setModelProperties(replacement, original, slot);
//                return replacement;
//            }
//
//            @SuppressWarnings("unchecked")
//            public static <T extends LivingEntity> void setModelProperties(HumanoidModel<?> replacement, HumanoidModel<T> original, EquipmentSlot slot){
//                original.copyPropertiesTo((HumanoidModel<T>) replacement);
//                replacement.head.visible = slot == EquipmentSlot.HEAD;
//                replacement.hat.visible = slot == EquipmentSlot.HEAD;
//                replacement.body.visible = slot == EquipmentSlot.CHEST || slot == EquipmentSlot.LEGS;
//                replacement.rightArm.visible = slot == EquipmentSlot.CHEST;
//                replacement.leftArm.visible = slot == EquipmentSlot.CHEST;
//                replacement.rightLeg.visible = slot == EquipmentSlot.LEGS || slot == EquipmentSlot.FEET;
//                replacement.leftLeg.visible = slot == EquipmentSlot.LEGS || slot == EquipmentSlot.FEET;
//            }
//        });
//    }
}