package com.ametrinstudios.ametrin_test.world;

import com.ametrinstudios.ametrin.world.entity.boat.BoatVariants;
import com.ametrinstudios.ametrin.world.item.CustomBoatItem;
import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TestItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, AmetrinTestMod.MOD_ID);

    public static final RegistryObject<Item> TEST_ITEM = REGISTRY.register("test_item", ()-> new BlockItem(TestBlocks.TEST_BLOCK.get(), new Item.Properties().setNoRepair().stacksTo(16).rarity(Rarity.EPIC)));
    public static final RegistryObject<CustomBoatItem> TROLL_BOAT = REGISTRY.register("troll_boat", ()-> new CustomBoatItem(TestBoatTypes.TROLL, BoatVariants.DEFAULT));
    public static final RegistryObject<CustomBoatItem> TROLL_CHEST_BOAT = REGISTRY.register("troll_chest_boat", ()-> new CustomBoatItem(TestBoatTypes.TROLL, BoatVariants.CHEST));
}
