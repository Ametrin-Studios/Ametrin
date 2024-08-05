package com.ametrinstudios.ametrin.world.item.helper;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ArmorMaterialBuilder {
    private final EnumMap<ArmorItem.Type, Integer> _defence = new EnumMap<>(ArmorItem.Type.class);
    private int _enchantmentValue = 0;
    private Holder<SoundEvent> _equipSound = SoundEvents.ARMOR_EQUIP_IRON;
    private Supplier<Ingredient> _repairIngredient = ()-> Ingredient.EMPTY;
    private final List<ArmorMaterial.Layer> _layers;
    private float _toughness = 0;
    private float _knockbackResistance = 0;

    public ArmorMaterialBuilder(String name) {
        _layers = List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace(name)));
    }

    public ArmorMaterialBuilder defence(Consumer<EnumMap<ArmorItem.Type, Integer>> defence) {
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

    public ArmorMaterialBuilder repairIngredient(Supplier<Ingredient> repairIngredient) {
        _repairIngredient = repairIngredient;
        return this;
    }

    public ArmorMaterialBuilder repairItem(Supplier<ItemLike> repairIngredient) {
        return repairIngredient(() -> Ingredient.of(repairIngredient.get()));
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
        return new ArmorMaterial(_defence, _enchantmentValue, _equipSound, _repairIngredient, _layers, _toughness, _knockbackResistance);
    }
}