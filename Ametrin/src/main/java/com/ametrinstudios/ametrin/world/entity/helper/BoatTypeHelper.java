package com.ametrinstudios.ametrin.world.entity.helper;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

import java.util.function.Supplier;

public final class BoatTypeHelper {
    public static EnumProxy<Boat.Type> createProxy(ResourceLocation name, Supplier<Block> planks, Supplier<BoatItem> boatItem, Supplier<BoatItem> chestBoatItem) {
        return createProxy(name, planks, boatItem, chestBoatItem, ()-> Items.STICK, false);
    }

    public static EnumProxy<Boat.Type> createRaftProxy(ResourceLocation name, Supplier<Block> planks, Supplier<BoatItem> boatItem, Supplier<BoatItem> chestBoatItem) {
        return createProxy(name, planks, boatItem, chestBoatItem, ()-> Items.STICK, true);
    }

    public static EnumProxy<Boat.Type> createProxy(ResourceLocation name, Supplier<Block> planks, Supplier<BoatItem> boatItem, Supplier<BoatItem> chestBoatItem, boolean isRaft) {
        return createProxy(name, planks, boatItem, chestBoatItem, ()-> Items.STICK, isRaft);
    }

    public static EnumProxy<Boat.Type> createProxy(ResourceLocation name, Supplier<Block> planks, Supplier<BoatItem> boatItem, Supplier<BoatItem> chestBoatItem, Supplier<Item> stickItem, boolean isRaft) {
        return new EnumProxy<>(Boat.Type.class, planks, name.toString(), boatItem, chestBoatItem, stickItem, isRaft);
    }

    public static String getExtensionJson(Boat.Type boatType, Class<?> proxyClass) {
        return getExtensionJson(ResourceLocation.parse(boatType.name()), proxyClass);
    }
    public static String getExtensionJson(ResourceLocation id, Class<?> proxyClass) {
        final var modID = id.getNamespace().toUpperCase();
        final var boatTypeName = id.getPath().toUpperCase();
        final var typeDescriptor = proxyClass.getName().replace('.', '/');

        return String.format("""

                    {
                      "enum": "net/minecraft/world/entity/vehicle/Boat$Type",
                      "name": "%s_%s",
                      "constructor": "(Ljava/util/function/Supplier;Ljava/lang/String;Ljava/util/function/Supplier;Ljava/util/function/Supplier;Ljava/util/function/Supplier;Z)V",
                      "parameters": {
                        "class": "%s",
                        "field": "%s"
                      }
                    },
                    """, modID, boatTypeName, typeDescriptor, boatTypeName);
    }
}
