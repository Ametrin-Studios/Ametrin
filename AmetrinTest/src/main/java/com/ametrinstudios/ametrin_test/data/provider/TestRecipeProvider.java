package com.ametrinstudios.ametrin_test.data.provider;

import com.ametrinstudios.ametrin.data.provider.ExtendedRecipeProvider;
import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.MultiRegistryBootstrap;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Set;

public final class TestRecipeProvider extends ExtendedRecipeProvider {
    public TestRecipeProvider(BootstrapContext<Recipe<?>> provider, BootstrapContext<Advancement> output, Set<Identifier> recipeSet) {
        super(AmetrinTestMod.MOD_ID, provider, output, recipeSet);
    }


    @Override
    protected void buildRecipes() {

    }

    public static MultiRegistryBootstrap create() {
        return new MultiRegistryBootstrap() {
            @Override
            public Set<ResourceKey<? extends Registry<?>>> requestedRegistries() {
                return Set.of(Registries.RECIPE, Registries.ADVANCEMENT);
            }

            @Override
            public void run(MultiRegistryBootstrap.BootstrapGetter registries) {
                new TestRecipeProvider(registries.get(Registries.RECIPE), registries.get(Registries.ADVANCEMENT), Set.of()).buildRecipes();
            }
        };
    }
}
