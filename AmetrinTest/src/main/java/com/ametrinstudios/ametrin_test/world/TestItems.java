package com.ametrinstudios.ametrin_test.world;

import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class TestItems {
    public static final DeferredRegister.Items REGISTER = DeferredRegister.createItems(AmetrinTestMod.MOD_ID);

    public static final Supplier<Item> TEST_ITEM = REGISTER.register("test_item", ()-> new BlockItem(TestBlocks.TEST_BLOCK.get(), new Item.Properties().setNoRepair().stacksTo(16).rarity(Rarity.EPIC)));
//    public static final Supplier<CustomBoatItem> TROLL_BOAT = REGISTER.register("troll_boat", ()-> CustomBoatItem.boat(TestBoatTypes.TROLL));
//    public static final Supplier<CustomBoatItem> TROLL_CHEST_BOAT = REGISTER.register("troll_chest_boat", ()-> CustomBoatItem.chest(TestBoatTypes.TROLL));
//    public static final Supplier<CustomBoatItem> BEECH_BOAT = REGISTER.register("beech_boat", ()-> CustomBoatItem.boat(TestBoatTypes.BEECH));
//    public static final Supplier<CustomBoatItem> BEECH_CHEST_BOAT = REGISTER.register("beech_chest_boat", ()-> CustomBoatItem.chest(TestBoatTypes.BEECH));
}
