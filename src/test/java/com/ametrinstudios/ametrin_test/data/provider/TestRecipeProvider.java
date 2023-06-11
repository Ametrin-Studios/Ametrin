package com.ametrinstudios.ametrin_test.data.provider;

import com.ametrinstudios.ametrin.data.provider.ExtendedRecipeProvider;
import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.ItemTags;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class TestRecipeProvider extends ExtendedRecipeProvider {
    public TestRecipeProvider(PackOutput packOutput) {
        super(packOutput, AmetrinTestMod.MOD_ID);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        dying(consumer, ItemTags.BEDS, "minecraft:{color}_bed", "bed");
    }
}
