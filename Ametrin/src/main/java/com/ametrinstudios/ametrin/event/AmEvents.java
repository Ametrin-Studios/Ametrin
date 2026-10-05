package com.ametrinstudios.ametrin.event;

import com.ametrinstudios.ametrin.Ametrin;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = Ametrin.MOD_ID)
public final class AmEvents {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
//        AmListTagElementsCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onAboutToStart(ServerAboutToStartEvent event) {
        if (!FMLEnvironment.isProduction() && !event.getServer().isDedicatedServer()) {
            event.getServer().setUsesAuthentication(false);
        }
    }
}
