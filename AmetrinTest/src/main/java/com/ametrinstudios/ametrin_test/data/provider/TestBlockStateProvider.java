package com.ametrinstudios.ametrin_test.data.provider;

import com.ametrinstudios.ametrin.data.provider.ExtendedBlockStateProvider;
import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import com.ametrinstudios.ametrin_test.registry.TestBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public final class TestBlockStateProvider extends ExtendedBlockStateProvider {
    public TestBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, AmetrinTestMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        runProviderRules(TestBlocks.REGISTER);
    }
}
