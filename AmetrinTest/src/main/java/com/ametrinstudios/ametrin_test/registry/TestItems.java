package com.ametrinstudios.ametrin_test.registry;

import com.ametrinstudios.ametrin.world.item.CustomHeadBlockItem;
import com.ametrinstudios.ametrin.world.item.PortalCatalystItem;
import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class TestItems {
    public static final DeferredRegister.Items REGISTER = DeferredRegister.createItems(AmetrinTestMod.MOD_ID);

    public static final DeferredItem<PortalCatalystItem> TEST_CATALYST = REGISTER.registerItem("test_catalyst", properties-> new PortalCatalystItem(TestPortals.TEST_PORTAL, properties), new Item.Properties().durability(16).rarity(Rarity.RARE));
    public static final DeferredItem<BlockItem> TEST_BLOCK = REGISTER.registerSimpleBlockItem("test_block", TestBlocks.TEST_BLOCK, new Item.Properties().stacksTo(16).rarity(Rarity.EPIC));
    public static final DeferredItem<CustomHeadBlockItem> TEST_SKULL = REGISTER.registerItem("test_skull", properties-> new CustomHeadBlockItem(TestBlocks.TEST_SKULL, TestBlocks.TEST_SKULL_WALL, properties), new Item.Properties().stacksTo(16).rarity(Rarity.EPIC).useBlockDescriptionPrefix());

//    public static final DeferredItem<BoatItem> BEECH_BOAT = REGISTER.register("beech_boat", ()-> boat(TestBoatTypes.BEECH));
//    public static final DeferredItem<BoatItem> TROLL_BOAT = REGISTER.register("troll_raft", ()-> boat(TestBoatTypes.TROLL));
//    public static final DeferredItem<BoatItem> TROLL_CHEST_BOAT = REGISTER.register("troll_chest_raft", ()-> chestBoat(TestBoatTypes.TROLL));
//    public static final DeferredItem<BoatItem> BEECH_CHEST_BOAT = REGISTER.register("beech_chest_boat", ()-> chestBoat(TestBoatTypes.BEECH));
}
