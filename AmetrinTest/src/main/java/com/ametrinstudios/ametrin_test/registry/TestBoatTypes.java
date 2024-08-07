package com.ametrinstudios.ametrin_test.registry;

import com.ametrinstudios.ametrin.world.entity.helper.BoatTypeHelper;
import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

public final class TestBoatTypes {
    public static final EnumProxy<Boat.Type> TROLL = BoatTypeHelper.createRaftProxy(AmetrinTestMod.locate("troll"), TestBlocks.TEST_BLOCK, TestItems.TROLL_BOAT, TestItems.TROLL_CHEST_BOAT);
    public static final EnumProxy<Boat.Type> BEECH = BoatTypeHelper.createProxy(AmetrinTestMod.locate("beech"), ()-> Blocks.OAK_PLANKS, TestItems.BEECH_BOAT, TestItems.BEECH_CHEST_BOAT);
}
