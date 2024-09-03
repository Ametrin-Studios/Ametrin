package com.ametrinstudios.ametrin.event;

import com.ametrinstudios.ametrin.Ametrin;
import com.ametrinstudios.ametrin.util.VanillaCompat;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = Ametrin.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class AmModEvents {

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(VanillaCompat::mergeRequests);
    }
}
