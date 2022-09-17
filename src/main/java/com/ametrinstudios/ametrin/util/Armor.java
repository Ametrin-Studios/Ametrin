package com.ametrinstudios.ametrin.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;

public class Armor{
    protected final ArmorMaterial material;
    protected final String textureLayer1, textureLayer2;


    public Armor(ArmorMaterial material, String textureLayer1, String textureLayer2){
        this.material = material;
        this.textureLayer1 = textureLayer1;
        this.textureLayer2 = textureLayer2;
    }

    public static Armor of(String modID, ArmorMaterial material, boolean singleTexture){
        if(singleTexture){
            String texture = new ResourceLocation(modID, "textures/model/armor/" + material.getName() + ".png").toString();
            return new Armor(material, texture, texture);
        }

        return new Armor(material, new ResourceLocation(modID, "textures/model/armor/" + material.getName() + "_layer_1.png").toString(), new ResourceLocation(modID, "textures/model/armor/" + material.getName() + "_layer_2.png").toString());
    }

    public ArmorMaterial getMaterial() {return material;}

    public String getTexture(EquipmentSlot slot){
        if(slot == EquipmentSlot.LEGS){
            return textureLayer2;
        }
        return textureLayer1;
    }
}