package com.ametrinstudios.ametrin_test;

import com.ametrinstudios.ametrin.data.provider.CustomLootTableProvider;
import com.ametrinstudios.ametrin_test.data.provider.TestBlockTagsProvider;
import com.ametrinstudios.ametrin_test.data.provider.TestItemTagsProvider;
import com.ametrinstudios.ametrin_test.data.provider.TestLanguageProvider;
import com.ametrinstudios.ametrin_test.data.provider.TestRecipeProvider;
import com.ametrinstudios.ametrin_test.data.provider.loot.TestBlockLootProvider;
import com.ametrinstudios.ametrin_test.data.provider.loot.TestLootTableProvider;
import com.ametrinstudios.ametrin_test.registry.TestBlocks;
import com.ametrinstudios.ametrin_test.registry.TestItems;
import com.ametrinstudios.ametrin_test.registry.TestPoiTypes;
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
        TestPoiTypes.REGISTER.register(modBus);

        modBus.addListener(AmetrinTestMod::setup);
        modBus.addListener(AmetrinTestMod::gatherData);

    }

    private static void setup(final FMLCommonSetupEvent event){
//        VanillaCompat.addStrippable(TestBlocks.TEST_BLOCK.get(), Blocks.DIAMOND_BLOCK);
//        VanillaCompat.addFlattenable(TestBlocks.TEST_BLOCK.get(), Blocks.DIAMOND_BLOCK);
    }

    public static void gatherData(GatherDataEvent.Server event){

//        event.createProvider(TestBlockStateProvider::new);
//        event.createProvider(TestItemModelProvider::new);
        event.createProvider(TestRecipeProvider.Runner::new);
        event.createProvider(TestLanguageProvider::new);
        event.createProvider(CustomLootTableProvider.builder()
                .addBlockProvider(TestBlockLootProvider::new)
                .addChestProvider(TestLootTableProvider::new)::build);

        event.createBlockAndItemTags(TestBlockTagsProvider::new, TestItemTagsProvider::new);
    }

    public static ResourceLocation locate(String key){
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, key);
    }
}