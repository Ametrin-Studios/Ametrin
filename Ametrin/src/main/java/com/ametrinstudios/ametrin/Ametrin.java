package com.ametrinstudios.ametrin;

import com.ametrinstudios.ametrin.util.VanillaCompat;
import net.minecraft.resources.ResourceLocation;
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
        event.enqueueWork(VanillaCompat::mergeRequests);
    }

    @ApiStatus.Internal
    public static ResourceLocation locate(String key) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, key);
    }
}