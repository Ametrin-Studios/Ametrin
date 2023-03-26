package com.ametrinstudios.ametrin_test.world;

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
}
