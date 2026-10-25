package com.ametrinstudios.ametrin_test.data.provider;

import com.ametrinstudios.ametrin.data.provider.ExtendedRecipeProvider;
import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import com.ametrinstudios.ametrin_test.registry.TestItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;

import java.util.concurrent.CompletableFuture;

public final class TestRecipeProvider extends ExtendedRecipeProvider {
    public TestRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(AmetrinTestMod.MOD_ID, output, registries);
    }


    @Override
    protected void buildRecipes() {
        shapeless(RecipeCategory.DECORATIONS, TestItems.TEST_BLOCK, 1, TestItems.TEST_SKULL, 1);
        shapeless(RecipeCategory.DECORATIONS, TestItems.TEST_BLOCK, 1, TestItems.TEST_CATALYST, 1);
    }
}
