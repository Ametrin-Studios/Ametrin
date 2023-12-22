package com.ametrinstudios.ametrin_test;

import com.ametrinstudios.ametrin.data.provider.CustomLootTableProvider;
import com.ametrinstudios.ametrin.util.VanillaCompat;
import com.ametrinstudios.ametrin_test.data.provider.TestBlockStateProvider;
import com.ametrinstudios.ametrin_test.data.provider.TestItemModelProvider;
import com.ametrinstudios.ametrin_test.data.provider.TestRecipeProvider;
import com.ametrinstudios.ametrin_test.data.provider.loot.TestBlockLootSubProvider;
import com.ametrinstudios.ametrin_test.data.provider.loot.TestLootTableSubProvider;
import com.ametrinstudios.ametrin_test.world.TestBlocks;
import com.ametrinstudios.ametrin_test.world.TestItems;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.slf4j.Logger;

@Mod(AmetrinTestMod.MOD_ID)
public class AmetrinTestMod {
    public static final String MOD_ID = "ametrin_test";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AmetrinTestMod(IEventBus modBus){
        LOGGER.info("---------------------- TEST MOD LOADED ----------------------");

        TestBlocks.REGISTRY.register(modBus);
        TestItems.REGISTRY.register(modBus);

        modBus.addListener(AmetrinTestMod::setup);
        modBus.addListener(AmetrinTestMod::gatherData);
    }

    private static void setup(final FMLCommonSetupEvent event){
//        VanillaCompat.addStrippable(TestBlocks.TEST_BLOCK.get(), Blocks.DIAMOND_BLOCK);
        VanillaCompat.addFlattenable(TestBlocks.TEST_BLOCK.get(), Blocks.DIAMOND_BLOCK);
    }

    public static void gatherData(GatherDataEvent event){
        // somehow the providers don't get run

        var generator = event.getGenerator();
        var output = generator.getPackOutput();
        var existingFileHelper = event.getExistingFileHelper();

        var runClient = event.includeClient();
        var runServer = event.includeServer();

        generator.addProvider(runServer, new TestBlockStateProvider(output, existingFileHelper));
        generator.addProvider(runServer, new TestItemModelProvider(output, existingFileHelper));
        generator.addProvider(runServer, new TestRecipeProvider(output, event.getLookupProvider()));

        var lootTableProvider = CustomLootTableProvider.Builder()
                .AddBlockProvider(TestBlockLootSubProvider::new)
                .AddChestProvider(TestLootTableSubProvider::new)
                .Build(output);
        generator.addProvider(runServer, lootTableProvider);
    }

    public static ResourceLocation locate(String key){
        return new ResourceLocation(MOD_ID, key);
    }
}