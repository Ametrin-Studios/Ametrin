package com.ametrinstudios.ametrin;

import com.ametrinstudios.ametrin.util.VanillaCompat;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jetbrains.annotations.ApiStatus;

@Mod(Ametrin.MOD_ID)
public final class Ametrin {
    @ApiStatus.Internal
    public static final String MOD_ID = "ametrin";

    public Ametrin(IEventBus modBus) {
        modBus.addListener(Ametrin::commonSetup);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(VanillaCompat::pushRequests);
    }
}