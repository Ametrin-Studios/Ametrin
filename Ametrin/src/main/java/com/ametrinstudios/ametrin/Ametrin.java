package com.ametrinstudios.ametrin;

import com.ametrinstudios.ametrin.commands.AmArgumentTypes;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.ApiStatus;

@Mod(Ametrin.MOD_ID)
public final class Ametrin {
    @ApiStatus.Internal
    public static final String MOD_ID = "ametrin";

    public Ametrin(IEventBus modBus) {
        AmArgumentTypes.REGISTER.register(modBus);
    }

    @ApiStatus.Internal
    public static ResourceLocation locate(String key) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, key);
    }
}