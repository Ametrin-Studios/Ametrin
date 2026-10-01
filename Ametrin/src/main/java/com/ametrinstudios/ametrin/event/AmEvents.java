package com.ametrinstudios.ametrin.event;

import com.ametrinstudios.ametrin.Ametrin;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = Ametrin.MOD_ID)
public final class AmEvents {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
//        ConfigCommand.register(event.getDispatcher());
    }
}
