package com.ametrinstudios.ametrin_test;

import com.ametrinstudios.ametrin_test.world.TestBlocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AmetrinTestMod.ModID)
public class AmetrinTestMod {
    public static final String ModID = "ametrin_test";

    public AmetrinTestMod(){
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        TestBlocks.Registry.register(modEventBus);
    }
}