package com.ametrinstudios.ametrin_test.world;

import com.ametrinstudios.ametrin.world.entity.boat.CustomBoatType;
import com.ametrinstudios.ametrin_test.AmetrinTestMod;

public class TestBoatTypes {
    public static final CustomBoatType TROLL = CustomBoatType.builder(AmetrinTestMod.locate("troll")).boatItem(TestItems.TROLL_BOAT::get).chestBoatItem(TestItems.TROLL_CHEST_BOAT::get).register();
}
