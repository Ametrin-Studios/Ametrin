package com.ametrinstudios.ametrin.world.item;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;

public class CustomArmorItem extends ArmorItem {
    public CustomArmorItem(Holder<ArmorMaterial> p_323783_, Type p_266831_, Properties p_40388_) {
        super(p_323783_, p_266831_, p_40388_);
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