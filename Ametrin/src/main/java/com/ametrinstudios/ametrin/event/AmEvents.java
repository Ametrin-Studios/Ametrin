package com.ametrinstudios.ametrin.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@SuppressWarnings("unused")
@EventBusSubscriber
public final class AmEvents {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
    }
}
