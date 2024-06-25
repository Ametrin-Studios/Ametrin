package com.ametrinstudios.ametrin_test;

import com.ametrinstudios.ametrin.data.DataProviderHelper;
import com.ametrinstudios.ametrin_test.data.provider.TestBlockStateProvider;
import com.ametrinstudios.ametrin_test.data.provider.TestItemModelProvider;
import com.ametrinstudios.ametrin_test.data.provider.TestLanguageProvider;
import com.ametrinstudios.ametrin_test.data.provider.TestRecipeProvider;
import com.ametrinstudios.ametrin_test.data.provider.loot.TestBlockLootSubProvider;
import com.ametrinstudios.ametrin_test.data.provider.loot.TestLootTableSubProvider;
import com.ametrinstudios.ametrin_test.world.TestBlocks;
import com.ametrinstudios.ametrin_test.world.TestItems;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.slf4j.Logger;

@Mod(AmetrinTestMod.MOD_ID)
public final class AmetrinTestMod {
    public static final String MOD_ID = "ametrin_test";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AmetrinTestMod(IEventBus modBus){
        LOGGER.info("---------------------- TEST MOD LOADED ----------------------");

        TestBlocks.REGISTER.register(modBus);
        TestItems.REGISTER.register(modBus);

        modBus.addListener(AmetrinTestMod::setup);
        modBus.addListener(AmetrinTestMod::gatherData);
    }

    private static void setup(final FMLCommonSetupEvent event){
//        VanillaCompat.addStrippable(TestBlocks.TEST_BLOCK.get(), Blocks.DIAMOND_BLOCK);
//        VanillaCompat.addFlattenable(TestBlocks.TEST_BLOCK.get(), Blocks.DIAMOND_BLOCK);
    }

    public static void gatherData(GatherDataEvent event){
        var helper = new DataProviderHelper(event);
        // somehow the providers don't get run

        helper.add(TestBlockStateProvider::new);
        helper.add(TestItemModelProvider::new);
        helper.add(TestRecipeProvider::new);
        helper.add(TestLanguageProvider::new);
        helper.addLootTables(builder -> builder
                .AddBlockProvider(TestBlockLootSubProvider::new)
                .AddChestProvider(TestLootTableSubProvider::new));
    }

    public static ResourceLocation locate(String key){
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, key);
    }
}