package com.ametrinstudios.ametrin_test.registry;

import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.ametrinstudios.ametrin.world.item.helper.ItemRegisterHelper.boat;
import static com.ametrinstudios.ametrin.world.item.helper.ItemRegisterHelper.chestBoat;

public final class TestItems {
    public static final DeferredRegister.Items REGISTER = DeferredRegister.createItems(AmetrinTestMod.MOD_ID);

    public static final DeferredItem<Item> TEST_BLOCK = REGISTER.register("test_block", ()-> new BlockItem(TestBlocks.TEST_BLOCK.get(), new Item.Properties().setNoRepair().stacksTo(16).rarity(Rarity.EPIC)));
    public static final DeferredItem<BoatItem> BEECH_BOAT = REGISTER.register("beech_boat", ()-> boat(TestBoatTypes.BEECH));
    public static final DeferredItem<BoatItem> TROLL_BOAT = REGISTER.register("troll_raft", ()-> boat(TestBoatTypes.TROLL));
    public static final DeferredItem<BoatItem> TROLL_CHEST_BOAT = REGISTER.register("troll_chest_raft", ()-> chestBoat(TestBoatTypes.TROLL));
    public static final DeferredItem<BoatItem> BEECH_CHEST_BOAT = REGISTER.register("beech_chest_boat", ()-> chestBoat(TestBoatTypes.BEECH));
}
