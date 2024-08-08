package com.ametrinstudios.ametrin_test.registry;

import com.ametrinstudios.ametrin.world.item.CustomHeadBlockItem;
import com.ametrinstudios.ametrin.world.item.PortalCatalystItem;
import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.ametrinstudios.ametrin.world.item.helper.ItemRegisterHelper.boat;
import static com.ametrinstudios.ametrin.world.item.helper.ItemRegisterHelper.chestBoat;

public final class TestItems {
    public static final DeferredRegister.Items REGISTER = DeferredRegister.createItems(AmetrinTestMod.MOD_ID);

    public static final DeferredItem<PortalCatalystItem> TEST_CATALYST = REGISTER.register("test_catalyst", ()-> new PortalCatalystItem(TestPortals.TEST_PORTAL, new Item.Properties().durability(16).rarity(Rarity.RARE)));
    public static final DeferredItem<BlockItem> TEST_BLOCK = REGISTER.register("test_block", ()-> new BlockItem(TestBlocks.TEST_BLOCK.get(), new Item.Properties().stacksTo(16).rarity(Rarity.EPIC)));
    public static final DeferredItem<CustomHeadBlockItem> TEST_SKULL = REGISTER.register("test_skull", ()-> new CustomHeadBlockItem(TestBlocks.TEST_SKULL, TestBlocks.TEST_SKULL_WALL, new Item.Properties().stacksTo(16).rarity(Rarity.EPIC)));

    public static final DeferredItem<BoatItem> BEECH_BOAT = REGISTER.register("beech_boat", ()-> boat(TestBoatTypes.BEECH));
    public static final DeferredItem<BoatItem> TROLL_BOAT = REGISTER.register("troll_raft", ()-> boat(TestBoatTypes.TROLL));
    public static final DeferredItem<BoatItem> TROLL_CHEST_BOAT = REGISTER.register("troll_chest_raft", ()-> chestBoat(TestBoatTypes.TROLL));
    public static final DeferredItem<BoatItem> BEECH_CHEST_BOAT = REGISTER.register("beech_chest_boat", ()-> chestBoat(TestBoatTypes.BEECH));

    static {
        var list = BuiltInRegistries.ITEM.stream()
                .filter(i -> i instanceof TieredItem)
                .map(t-> ((TieredItem) t).getTier())
                .distinct().toList();
    }
}
