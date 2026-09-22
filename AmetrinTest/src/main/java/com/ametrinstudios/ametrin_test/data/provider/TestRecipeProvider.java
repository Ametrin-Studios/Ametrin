package com.ametrinstudios.ametrin_test.data.provider;

import com.ametrinstudios.ametrin.data.provider.ExtendedRecipeProvider;
import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class TestRecipeProvider extends ExtendedRecipeProvider {
    public TestRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
        super(AmetrinTestMod.MOD_ID, provider, output, Map.of());
    }


    @Override
    protected void buildRecipes() {

    }

    public static class Runner extends ExtendedRecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> providerCompletableFuture) {
            super(output, providerCompletableFuture);
        }

        @Override
        protected ExtendedRecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput output, Set<Identifier> recipeSet) {
            return new TestRecipeProvider(provider, output);
        }

        @Override
        public String getName() {
            return "Ametrin Test Recipes";
        }
    }
}
