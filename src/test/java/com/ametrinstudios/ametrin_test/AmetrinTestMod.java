package com.ametrinstudios.ametrin_test;

import com.ametrinstudios.ametrin.data.provider.CustomLootTableProvider;
import com.ametrinstudios.ametrin_test.data.provider.TestBlockStateProvider;
import com.ametrinstudios.ametrin_test.data.provider.TestItemModelProvider;
import com.ametrinstudios.ametrin_test.data.provider.loot.TestBlockLootSubProvider;
import com.ametrinstudios.ametrin_test.data.provider.loot.TestLootTableSubProvider;
import com.ametrinstudios.ametrin_test.world.TestBlocks;
import com.ametrinstudios.ametrin_test.world.TestItems;
import com.google.common.reflect.Reflection;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(AmetrinTestMod.MOD_ID)
public class AmetrinTestMod {
    public static final String MOD_ID = "ametrin_test";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AmetrinTestMod(){
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        var forgeBus = MinecraftForge.EVENT_BUS;
        LOGGER.info("---------------------- TEST MOD LOADED ----------------------");

        Reflection.initialize(TestBlocks.class, TestItems.class);

        modBus.addListener(AmetrinTestMod::gatherData);
        TestBlocks.REGISTRY.register(modBus);
    }

    public static void gatherData(GatherDataEvent event){
        var generator = event.getGenerator();
        var output = generator.getPackOutput();
        var existingFileHelper = event.getExistingFileHelper();

        var runClient = event.includeClient();
        var runServer = event.includeServer();

        generator.addProvider(runServer, new TestBlockStateProvider(output, existingFileHelper));
        generator.addProvider(runServer, new TestItemModelProvider(output, existingFileHelper));

        var lootTableProvider = CustomLootTableProvider.Builder().AddBlockProvider(TestBlockLootSubProvider::new).AddChestProvider(TestLootTableSubProvider::new).Build(output);
        generator.addProvider(runServer, lootTableProvider);
    }

    public static ResourceLocation locate(String key){
        return new ResourceLocation(MOD_ID, key);
    }
}