package com.ametrinstudios.ametrin;

import com.ametrinstudios.ametrin.util.VanillaCompat;
import com.ametrinstudios.ametrin.world.AmetrinEntityTypes;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.ApiStatus;

@Mod(Ametrin.MOD_ID)
public class Ametrin{
    @ApiStatus.Internal
    public static final String MOD_ID = "ametrin";

    public Ametrin(){
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
//        var forgeEventBus = MinecraftForge.EVENT_BUS;

        AmetrinEntityTypes.REGISTER.register(modBus);
        modBus.addListener(Ametrin::setup);
    }

    private static void setup(final FMLCommonSetupEvent event){
        event.enqueueWork(VanillaCompat::PushRequests);
    }
}