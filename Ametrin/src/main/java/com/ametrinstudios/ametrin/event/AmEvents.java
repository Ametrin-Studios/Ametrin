package com.ametrinstudios.ametrin.event;

import com.ametrinstudios.ametrin.Ametrin;
import com.ametrinstudios.ametrin.commands.AmListTagElementsCommand;
import com.ametrinstudios.ametrin.util.VanillaCompat;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.server.command.ConfigCommand;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = Ametrin.MOD_ID)
public final class AmEvents {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        AmListTagElementsCommand.register(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(VanillaCompat::mergeRequests);
    }
}
