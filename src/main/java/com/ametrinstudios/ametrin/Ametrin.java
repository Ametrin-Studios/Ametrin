package com.ametrinstudios.ametrin;

import com.ametrinstudios.ametrin.world.AmetrinEntityTypes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.ApiStatus;

@Mod(Ametrin.MOD_ID)
public class Ametrin{
    @ApiStatus.Internal
    public static final String MOD_ID = "ametrin";

    public Ametrin(IEventBus modEventBus){
        AmetrinEntityTypes.REGISTER.register(modEventBus);
    }
}