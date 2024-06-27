package com.ametrinstudios.ametrin_test.world;

import com.ametrinstudios.ametrin.world.entity.boat.CustomBoatType;
import com.ametrinstudios.ametrin_test.AmetrinTestMod;

public final class TestBoatTypes {
    public static final CustomBoatType TROLL = CustomBoatType.builder(AmetrinTestMod.locate("troll")).boatItem(TestItems.TROLL_BOAT::get).chestBoatItem(TestItems.TROLL_CHEST_BOAT::get).register();
    public static final CustomBoatType BEECH = CustomBoatType.builder(AmetrinTestMod.locate("beech")).boatItem(TestItems.BEECH_BOAT::get).chestBoatItem(TestItems.BEECH_CHEST_BOAT::get).register();
}
