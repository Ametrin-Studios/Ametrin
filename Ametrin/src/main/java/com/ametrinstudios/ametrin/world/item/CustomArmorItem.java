package com.ametrinstudios.ametrin.world.item;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;

//just here for back-compat, might be reused when armor system gets comeback
public class CustomArmorItem extends ArmorItem {
    public CustomArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }
//    protected final Armor Armor;

//    public CustomArmorItem(Armor armor, ArmorItem.Type type, Properties properties) {
//        super(armor.getMaterial(), type, properties);
//        Armor = armor;
//    }
//
//    @Override @Nullable
//    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {return Armor.getTexture(slot);}
}