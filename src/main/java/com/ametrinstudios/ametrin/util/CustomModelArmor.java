package com.ametrinstudios.ametrin.util;

public class CustomModelArmor extends Armor {
//    protected final HumanoidModel<? extends LivingEntity> armorModel, legModel;
//
//    public CustomModelArmor(@Nullable HumanoidModel<? extends LivingEntity> armor, @Nullable HumanoidModel<? extends LivingEntity> legs, ArmorMaterial material, String textureLayer1, String textureLayer2){
//        super(material, textureLayer1, textureLayer2);
//        armorModel = armor;
//        legModel = legs;
//    }
//
//    public static CustomModelArmor of(@Nullable HumanoidModel<? extends LivingEntity> armor, @Nullable HumanoidModel<? extends LivingEntity> legs, String modID, ArmorMaterial material, boolean singleTexture){
//        if(singleTexture){
//            String texture = new ResourceLocation(modID, "textures/model/armor/" + material.getName() + ".png").toString();
//            return new CustomModelArmor(armor, legs, material, texture, texture);
//        }
//
//        return new CustomModelArmor(armor, legs, material, new ResourceLocation(modID, "textures/model/armor/" + material.getName() + "_layer_1.png").toString(), new ResourceLocation(modID, "textures/model/armor/" + material.getName() + "_layer_2.png").toString());
//    }
//
//    public HumanoidModel<? extends LivingEntity> getModel(EquipmentSlot slot){
//        return slot == EquipmentSlot.LEGS ? legModel : armorModel;
//    }
}