package com.ametrinstudios.ametrin_test.data.provider;

import com.ametrinstudios.ametrin.data.provider.ExtendedItemModelProvider;
import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import com.ametrinstudios.ametrin_test.world.TestItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class TestItemModelProvider extends ExtendedItemModelProvider {
    public TestItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, AmetrinTestMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        runProviderRules(TestItems.REGISTRY);
    }
}
