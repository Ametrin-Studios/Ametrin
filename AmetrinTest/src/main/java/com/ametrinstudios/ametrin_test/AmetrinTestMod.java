package com.ametrinstudios.ametrin_test;

import com.ametrinstudios.ametrin_test.data.provider.*;
import com.ametrinstudios.ametrin_test.registry.TestBlocks;
import com.ametrinstudios.ametrin_test.registry.TestItems;
import com.ametrinstudios.ametrin_test.registry.TestPoiTypes;
import com.mojang.logging.LogUtils;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
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

        modBus.addListener(AmetrinTestMod::gatherServerData);
        modBus.addListener(AmetrinTestMod::gatherClientData);
    }

    // Server data uses Client event because I don't need to run them separately
    public static void gatherServerData(GatherDataEvent.Client event) {
        event.createReloadableRegistryObjects(new RegistrySetBuilder()
                .add(Registries.LOOT_TABLE, TestLootTableProvider.create())
                .add(TestRecipeProvider.create())
        );

        event.createProvider(TestBiomeTagsProvider::new);
        event.createBlockAndItemTags(TestBlockTagsProvider::new, TestItemTagsProvider::new);
    }

    public static void gatherClientData(GatherDataEvent.Client event) {
        event.createProvider(TestModelProvider::new);
        event.createProvider(TestLanguageProvider::new);
    }

    public static Identifier locate(String key) {
        return Identifier.fromNamespaceAndPath(MOD_ID, key);
    }
}