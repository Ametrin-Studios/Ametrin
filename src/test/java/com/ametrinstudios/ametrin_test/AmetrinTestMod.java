package com.ametrinstudios.ametrin_test;

import com.ametrinstudios.ametrin_test.data.provider.TestBlockStateProvider;
import com.ametrinstudios.ametrin_test.data.provider.TestItemModelProvider;
import com.ametrinstudios.ametrin_test.world.TestBlocks;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(AmetrinTestMod.MOD_ID)
public class AmetrinTestMod {
    public static final String MOD_ID = "ametrin_test";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AmetrinTestMod(){
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        LOGGER.info("---------------------- TEST MOD LOADED ----------------------");

        modEventBus.addListener(AmetrinTestMod::gatherData);
        TestBlocks.REGISTRY.register(modEventBus);
    }

    public static void gatherData(GatherDataEvent event){
        var generator = event.getGenerator();
        var output = generator.getPackOutput();
        var existingFileHelper = event.getExistingFileHelper();

        var runClient = event.includeClient();
        var runServer = event.includeServer();

        generator.addProvider(runServer, new TestBlockStateProvider(output, existingFileHelper));
        generator.addProvider(runServer, new TestItemModelProvider(output, existingFileHelper));
    }
}