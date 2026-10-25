package com.ametrinstudios.ametrin_test;

import com.ametrinstudios.ametrin.data.provider.CustomLootTableProvider;
import com.ametrinstudios.ametrin_test.data.provider.*;
import com.ametrinstudios.ametrin_test.data.provider.loot.TestBlockLootProvider;
import com.ametrinstudios.ametrin_test.data.provider.loot.TestChestLootProvider;
import com.ametrinstudios.ametrin_test.registry.TestBlocks;
import com.ametrinstudios.ametrin_test.registry.TestItems;
import com.ametrinstudios.ametrin_test.registry.TestPoiTypes;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.slf4j.Logger;

@Mod(AmetrinTestMod.MOD_ID)
public final class AmetrinTestMod {
    public static final String MOD_ID = "ametrin_test";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AmetrinTestMod(IEventBus modBus) {
        LOGGER.info("---------------------- TEST MOD LOADED ----------------------");

        TestBlocks.REGISTER.register(modBus);
        TestItems.REGISTER.register(modBus);
        TestPoiTypes.REGISTER.register(modBus);

        modBus.addListener(AmetrinTestMod::gatherData);
    }

    public static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var output = generator.getPackOutput();
        var registries = event.getLookupProvider();
        var existingFileHelper = event.getExistingFileHelper();
        var server = event.includeServer();

        generator.addProvider(server, CustomLootTableProvider.builder()
                .addBlockProvider(TestBlockLootProvider::new)
                .addChestProvider(TestChestLootProvider::new)
                .build(output, registries));

        generator.addProvider(server, new TestRecipeProvider(output, registries));
        generator.addProvider(server, new TestBiomeTagsProvider(output, registries, existingFileHelper));
        var blockTags = generator.addProvider(server, new TestBlockTagsProvider(output, registries, existingFileHelper));
        generator.addProvider(server, new TestItemTagsProvider(output, registries, blockTags.contentsGetter(), existingFileHelper));

        var client = event.includeClient();
        generator.addProvider(client, new TestBlockStateProvider(output, existingFileHelper));
        generator.addProvider(client, new TestItemModelProvider(output, existingFileHelper));
        generator.addProvider(client, new TestLanguageProvider(output));
    }

    public static ResourceLocation locate(String key) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, key);
    }
}