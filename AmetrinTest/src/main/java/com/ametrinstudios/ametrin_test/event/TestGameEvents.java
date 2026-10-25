package com.ametrinstudios.ametrin_test.event;

import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import com.ametrinstudios.ametrin_test.command.TestTerrainAnalyzerCommand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = AmetrinTestMod.MOD_ID)
public final class TestGameEvents {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        new TestTerrainAnalyzerCommand(event.getDispatcher());
    }
}