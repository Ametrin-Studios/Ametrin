package com.ametrinstudios.ametrin;

import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.ApiStatus;

@Mod(Ametrin.MOD_ID)
public final class Ametrin {
    @ApiStatus.Internal
    public static final String MOD_ID = "ametrin";

    public Ametrin(IEventBus modBus) {

    }

    @ApiStatus.Internal
    public static Identifier locate(String key) {
        return Identifier.fromNamespaceAndPath(MOD_ID, key);
    }
}