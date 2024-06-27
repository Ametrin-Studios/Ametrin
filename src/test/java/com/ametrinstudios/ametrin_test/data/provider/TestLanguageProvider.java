package com.ametrinstudios.ametrin_test.data.provider;

import com.ametrinstudios.ametrin.data.provider.ExtendedLanguageProvider;
import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import net.minecraft.data.PackOutput;

public final class TestLanguageProvider extends ExtendedLanguageProvider {
    public TestLanguageProvider(PackOutput output) {
        super(output, AmetrinTestMod.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {

    }
}
