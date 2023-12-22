package com.ametrinstudios.ametrin_test.event;

import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import com.ametrinstudios.ametrin_test.command.TestTerrainAnalyzerCommand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@Mod.EventBusSubscriber(modid = AmetrinTestMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TestForgeEvents {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event){
        new TestTerrainAnalyzerCommand(event.getDispatcher());

//        ConfigCommand.register(event.getDispatcher());
    }
}