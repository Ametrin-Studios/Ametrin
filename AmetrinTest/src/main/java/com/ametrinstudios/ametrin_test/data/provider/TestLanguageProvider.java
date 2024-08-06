package com.ametrinstudios.ametrin_test.data.provider;

import com.ametrinstudios.ametrin.data.provider.ExtendedLanguageProvider;
import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import com.ametrinstudios.ametrin_test.world.TestItems;
import net.minecraft.data.PackOutput;

public final class TestLanguageProvider extends ExtendedLanguageProvider {
    public TestLanguageProvider(PackOutput output) {
        super(output, AmetrinTestMod.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(TestItems.TEST_BLOCK, "Test Block");

        add(TestItems.TROLL_BOAT, "Troll Raft");
        add(TestItems.TROLL_CHEST_BOAT, "Troll Chest Raft");
        add(TestItems.BEECH_BOAT, "Beech Boat");
        add(TestItems.BEECH_CHEST_BOAT, "Beech Chest Boat");
    }
}
