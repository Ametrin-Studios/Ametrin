package com.ametrinstudios.ametrin_test.event;

import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import com.ametrinstudios.ametrin_test.command.TestTerrainAnalyzerCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AmetrinTestMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TestForgeEvents {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event){
        new TestTerrainAnalyzerCommand(event.getDispatcher());

//        ConfigCommand.register(event.getDispatcher());
    }
}