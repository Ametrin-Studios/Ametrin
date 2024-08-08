package com.ametrinstudios.ametrin_test;

import com.ametrinstudios.ametrin.data.DataProviderHelper;
import com.ametrinstudios.ametrin.world.entity.helper.BoatTypeHelper;
import com.ametrinstudios.ametrin_test.data.provider.*;
import com.ametrinstudios.ametrin_test.data.provider.loot.TestBlockLootProvider;
import com.ametrinstudios.ametrin_test.data.provider.loot.TestLootTableProvider;
import com.ametrinstudios.ametrin_test.registry.TestBlocks;
import com.ametrinstudios.ametrin_test.registry.TestBoatTypes;
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

    public static void gatherData(GatherDataEvent event){
        var helper = new DataProviderHelper(event);

        helper.add(TestBlockStateProvider::new);
        helper.add(TestItemModelProvider::new);
        helper.add(TestRecipeProvider::new);
        helper.add(TestLanguageProvider::new);
        helper.addLootTables(builder -> builder
                .addBlockProvider(TestBlockLootProvider::new)
                .addChestProvider(TestLootTableProvider::new));

        helper.addBlockAndItemTags(TestBlockTagsProvider::new, TestItemTagsProvider::new);

        LOGGER.info("Dumping generated enum extensions:");
        LOGGER.info(BoatTypeHelper.getExtensionJson(locate("troll"), TestBoatTypes.class));
        LOGGER.info(BoatTypeHelper.getExtensionJson(locate("beech"), TestBoatTypes.class));
    }

    public static ResourceLocation locate(String key){
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, key);
    }
}