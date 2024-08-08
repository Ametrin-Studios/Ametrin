package com.ametrinstudios.ametrin_test.data.provider;

import com.ametrinstudios.ametrin.data.provider.ExtendedLanguageProvider;
import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import com.ametrinstudios.ametrin_test.registry.TestBlocks;
import com.ametrinstudios.ametrin_test.registry.TestItems;
import net.minecraft.data.PackOutput;

public final class TestLanguageProvider extends ExtendedLanguageProvider {
    public TestLanguageProvider(PackOutput output) {
        super(output, AmetrinTestMod.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(TestItems.TEST_BLOCK, "Test Portal Frame");
        add(TestBlocks.TEST_LOG, "Test Log");
        add(TestItems.TEST_CATALYST, "Test Catalyst");
        add(TestItems.TEST_SKULL, "Test Skull");

        add(TestItems.TROLL_BOAT, "Troll Raft");
        add(TestItems.TROLL_CHEST_BOAT, "Troll Raft with Chest");
        add(TestItems.BEECH_BOAT, "Beech Boat");
        add(TestItems.BEECH_CHEST_BOAT, "Beech Boat with Chest");
    }
}
