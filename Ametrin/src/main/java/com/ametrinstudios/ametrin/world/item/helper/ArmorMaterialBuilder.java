package com.ametrinstudios.ametrin.world.item.helper;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.EnumMap;
import java.util.function.Consumer;

public final class ArmorMaterialBuilder {
    private int _durability = 1;
    private final EnumMap<ArmorType, Integer> _defence = new EnumMap<>(ArmorType.class);
    private int _enchantmentValue = 0;
    private Holder<SoundEvent> _equipSound = SoundEvents.ARMOR_EQUIP_IRON;
    private TagKey<Item> _repairIngredient;
    private float _toughness = 0;
    private float _knockbackResistance = 0;
    private final ResourceLocation _modelID;

    public ArmorMaterialBuilder(String modelId) {
        this(ResourceLocation.withDefaultNamespace(modelId));
    }
    public ArmorMaterialBuilder(ResourceLocation modelID) {
        _modelID = modelID;
    }

    public ArmorMaterialBuilder durability(int value){
        _durability = value;
        return this;
    }

    public ArmorMaterialBuilder defence(Consumer<EnumMap<ArmorType, Integer>> defence) {
        defence.accept(_defence);
        return this;
    }

    public ArmorMaterialBuilder enchantmentValue(int value) {
        _enchantmentValue = value;
        return this;
    }

    public ArmorMaterialBuilder equipSound(Holder<SoundEvent> equipSound) {
        _equipSound = equipSound;
        return this;
    }

    public ArmorMaterialBuilder repairIngredient(TagKey<Item> repairIngredient) {
        _repairIngredient = repairIngredient;
        return this;
    }

    public ArmorMaterialBuilder toughness(float value) {
        _toughness = value;
        return this;
    }

    public ArmorMaterialBuilder knockbackResistance(float value) {
        _knockbackResistance = value;
        return this;
    }

    public ArmorMaterial build() {
        return new ArmorMaterial(_durability, _defence, _enchantmentValue, _equipSound, _toughness, _knockbackResistance, _repairIngredient, _modelID);
    }
}