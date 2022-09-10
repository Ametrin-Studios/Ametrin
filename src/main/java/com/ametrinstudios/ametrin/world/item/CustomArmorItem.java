package com.ametrinstudios.ametrin.world.item;

import com.ametrinstudios.ametrin.util.Armor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CustomArmorItem extends ArmorItem {
    protected final Armor Armor;

    public CustomArmorItem(Armor armor, EquipmentSlot slot, Properties properties) {
        super(armor.getMaterial(), slot, properties);
        Armor = armor;
    }

    @Override @Nullable
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {return Armor.getTexture(slot);}
}