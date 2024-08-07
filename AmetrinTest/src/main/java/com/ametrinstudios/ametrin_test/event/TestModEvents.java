package com.ametrinstudios.ametrin_test.event;

import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import com.ametrinstudios.ametrin_test.registry.TestBlocks;
import com.ametrinstudios.ametrin_test.registry.TestItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = AmetrinTestMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class TestModEvents {
    @SubscribeEvent
    public static void buildCreativeModeTabs(final BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(TestBlocks.TEST_BLOCK);
            event.accept(TestBlocks.TEST_LOG);
        }

        if(event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(TestItems.TROLL_CHEST_BOAT);
            event.accept(TestItems.TROLL_BOAT);
            event.accept(TestItems.BEECH_CHEST_BOAT);
            event.accept(TestItems.BEECH_BOAT);
        }

        if(event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            event.accept(TestItems.TROLL_CHEST_BOAT);
            event.accept(TestItems.BEECH_CHEST_BOAT);
        }
    }
}
