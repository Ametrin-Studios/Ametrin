package com.ametrinstudios.ametrin.util;

public class Armor{
//    protected final ArmorMaterial.Layer material;
//    protected final String textureLayer1, textureLayer2;
//
//
//    public Armor(ArmorMaterial.Layer material, String textureLayer1, String textureLayer2){
//        this.material = material;
//        this.textureLayer1 = textureLayer1;
//        this.textureLayer2 = textureLayer2;
//    }
//
//    public static Armor of(String modID, ArmorMaterial.Layer material, boolean singleTexture){
//        if(singleTexture){
//            String texture = new Identifier(modID, "textures/model/armor/" + material.getName() + ".png").toString();
//            return new Armor(material, texture, texture);
//        }
//
//        return new Armor(material, new Identifier(modID, "textures/model/armor/" + material.getName() + "_layer_1.png").toString(), new Identifier(modID, "textures/model/armor/" + material.getName() + "_layer_2.png").toString());
//    }
//
//    public ArmorMaterial.Layer getMaterial() {return material;}
//
//    public String getTexture(EquipmentSlot slot){
//        if(slot == EquipmentSlot.LEGS){
//            return textureLayer2;
//        }
//        return textureLayer1;
//    }
}