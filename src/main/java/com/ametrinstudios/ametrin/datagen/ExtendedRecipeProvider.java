package com.ametrinstudios.ametrin.datagen;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public abstract class ExtendedRecipeProvider extends RecipeProvider {
    protected static final Logger LOGGER = LogUtils.getLogger();
    protected static Set<ResourceLocation> Recipes = Sets.newHashSet();

    protected static String currentModID;
    protected String modID;

    public ExtendedRecipeProvider(DataGenerator generator, String modID) {
        super(generator);
        this.modID = modID;
    }

    @Override
    public void run(@NotNull CachedOutput output){
        currentModID = modID;
        buildCraftingRecipes((recipe) -> {
            if (!Recipes.add(recipe.getId())) {
                throw new IllegalStateException("Duplicate recipe " + recipe.getId());
            } else {
                saveRecipe(output, recipe.serializeRecipe(), recipePathProvider.json(recipe.getId()));
                JsonObject unlockAdvancement = recipe.serializeAdvancement();
                if (unlockAdvancement != null) {
                    saveAdvancement(output, unlockAdvancement, advancementPathProvider.json(Objects.requireNonNull(recipe.getAdvancementId())));
                }

            }
        });
        currentModID = "";
    }

    protected static void stairSlabWallButton(Consumer<FinishedRecipe> consumer, @Nullable ItemLike stair, @Nullable ItemLike slab, @Nullable ItemLike wall, @Nullable ItemLike button, ItemLike material, boolean hasStonecutting){
        if(stair != null) {stairs(consumer, stair, material, hasStonecutting);}
        if(slab != null) {slab(consumer, slab, material, hasStonecutting);}
        if(wall != null) {wall(consumer, wall, material, hasStonecutting);}
        if(button != null) {button(consumer, button, material, hasStonecutting);}
    }
    protected static void stairSlabWallButton(Consumer<FinishedRecipe> consumer, @Nullable ItemLike stair, @Nullable ItemLike slab, @Nullable ItemLike wall, @Nullable ItemLike button, ItemLike material, ItemLike... additionalStonecuttingMaterials){
        if(stair != null) {stairs(consumer, stair, material, additionalStonecuttingMaterials);}
        if(slab != null) {slab(consumer, slab, material, additionalStonecuttingMaterials);}
        if(wall != null) {wall(consumer, wall, material, additionalStonecuttingMaterials);}
        if(button != null) {button(consumer, button, material, additionalStonecuttingMaterials);}
    }

    protected static void stairs(Consumer<FinishedRecipe> consumer, ItemLike stair, ItemLike material, boolean hasStonecutting){
        stairBuilder(stair, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(consumer, recipeID(stair, material));
        if(hasStonecutting) {stonecutting(consumer, stair, 1, material);}
    }
    protected static void stairs(Consumer<FinishedRecipe> consumer, ItemLike stair, ItemLike material, ItemLike... additionalStonecuttingMaterials){
        stairs(consumer, stair, material, true);
        for(ItemLike mat : additionalStonecuttingMaterials){
            stonecutting(consumer, stair, 1, mat);
        }
    }

    protected static void slab(Consumer<FinishedRecipe> consumer, ItemLike slab, ItemLike material, boolean hasStonecutting){
        slabBuilder(slab, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(consumer, recipeID(slab, material));
        if(hasStonecutting) {stonecutting(consumer, slab, 2, material);}
    }
    protected static void slab(Consumer<FinishedRecipe> consumer, ItemLike slab, ItemLike material, ItemLike... additionalStonecuttingMaterials){
        slab(consumer, slab, material, true);
        for(ItemLike mat : additionalStonecuttingMaterials){
            stonecutting(consumer, slab, mat instanceof SlabBlock ? 1 : 2, mat);
        }
    }

    protected static void wall(Consumer<FinishedRecipe> consumer, ItemLike wall, ItemLike material, boolean hasStonecutting){
        wallBuilder(wall, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(consumer, recipeID(wall, material));
        if(hasStonecutting) {stonecutting(consumer, wall, 1, material);}
    }
    protected static void wall(Consumer<FinishedRecipe> consumer, ItemLike wall, ItemLike material, ItemLike... additionalStonecuttingMaterials){
        wall(consumer, wall, material, true);
        for(ItemLike mat : additionalStonecuttingMaterials){
            stonecutting(consumer, wall, 1, mat);
        }
    }

    protected static void button(Consumer<FinishedRecipe> consumer, ItemLike button, ItemLike material, boolean hasStonecutting){
        buttonBuilder(button, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(consumer, recipeID(button, material));
        if(hasStonecutting) {stonecutting(consumer, button, 1, material);}
    }
    protected static void button(Consumer<FinishedRecipe> consumer, ItemLike button, ItemLike material, ItemLike... additionalStonecuttingMaterials) {
        button(consumer, button, material, true);
        for(ItemLike item : additionalStonecuttingMaterials) {stonecutting(consumer, button, 1, item);}
    }

    protected static void chiseled(Consumer<FinishedRecipe> consumer, ItemLike chiseled, ItemLike material, boolean hasStonecutting){
        chiseledBuilder(chiseled, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(consumer, recipeID(chiseled, material));
        if(hasStonecutting) {stonecutting(consumer, chiseled, 1, material);}
    }
    protected static void fence(Consumer<FinishedRecipe> consumer, ItemLike fence, ItemLike material){
        fence(consumer, fence, 3, material, Items.STICK, false);
    }
    protected static void netherFence(Consumer<FinishedRecipe> consumer, ItemLike fence, ItemLike material){
        fence(consumer, fence, 6, material, Items.NETHER_BRICK, true);
    }
    protected static void fence(Consumer<FinishedRecipe> consumer, ItemLike fence, int count, ItemLike material, ItemLike stick, boolean hasStonecutting){
        ShapedRecipeBuilder.shaped(fence, count).define('W', material).define('#', stick).pattern("W#W").pattern("W#W").unlockedBy(getHasName(material), has(material)).save(consumer, recipeID(fence, material));
        if(hasStonecutting) {stonecutting(consumer, fence, 1, material);}
    }
    protected static void fenceGate(Consumer<FinishedRecipe> consumer, ItemLike fenceGate, ItemLike material){
        fenceGate(consumer, fenceGate, material, Items.STICK, false);
    }
    protected static void netherFenceGate(Consumer<FinishedRecipe> consumer, ItemLike fenceGate, ItemLike material){
        fenceGate(consumer, fenceGate, material, Items.NETHER_BRICK, true);
    }
    protected static void fenceGate(Consumer<FinishedRecipe> consumer, ItemLike fenceGate, ItemLike material, ItemLike stick, boolean hasStonecutting){
        ShapedRecipeBuilder.shaped(fenceGate).define('#', stick).define('W', material).pattern("#W#").pattern("#W#").unlockedBy(getHasName(material), has(material)).save(consumer, recipeID(fenceGate, material));
        if(hasStonecutting) {stonecutting(consumer, fenceGate, 1, material);}
    }
    protected static void door(Consumer<FinishedRecipe> consumer, ItemLike door, ItemLike material){
        doorBuilder(door, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(consumer, recipeID(door, material));
    }
    protected static void trapdoor(Consumer<FinishedRecipe> consumer, ItemLike trapdoor, ItemLike material){
        trapdoorBuilder(trapdoor, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(consumer, recipeID(trapdoor, material));
    }

    protected static void torch(Consumer<FinishedRecipe> consumer, ItemLike torch, ItemLike flammable){
        ShapedRecipeBuilder.shaped(torch)
                .define('#', flammable)
                .define('|', Items.STICK)
                .pattern("#")
                .pattern("|")
                .unlockedBy(getHasName(flammable), has(flammable))
                .save(consumer, recipeID(torch, flammable));
    }
    protected static void torch(Consumer<FinishedRecipe> consumer, ItemLike torch, TagKey<Item> flammable){
        ShapedRecipeBuilder.shaped(torch)
                .define('#', flammable)
                .define('|', Items.STICK)
                .pattern("#")
                .pattern("|")
                .unlockedBy(getHasName(flammable), has(flammable))
                .save(consumer, recipeID(torch, flammable));
    }

    protected static void campfire(Consumer<FinishedRecipe> consumer, ItemLike campfire, ItemLike flammable){
        ShapedRecipeBuilder.shaped(campfire)
                .define('|', Items.STICK)
                .define('#', flammable)
                .define('O', ItemTags.LOGS_THAT_BURN)
                .pattern(" | ")
                .pattern("|#|")
                .pattern("OOO")
                .unlockedBy(getHasName(flammable), has(flammable))
                .save(consumer, recipeID(campfire, flammable));
    }
    protected static void campfire(Consumer<FinishedRecipe> consumer, ItemLike campfire, TagKey<Item> flammable){
        ShapedRecipeBuilder.shaped(campfire)
                .define('|', Items.STICK)
                .define('#', flammable)
                .define('O', ItemTags.LOGS_THAT_BURN)
                .pattern(" | ")
                .pattern("|#|")
                .pattern("OOO")
                .unlockedBy(getHasName(flammable), has(flammable))
                .save(consumer, recipeID(campfire, flammable));
    }

    protected static void lantern(Consumer<FinishedRecipe> consumer, ItemLike lantern, ItemLike nugget, ItemLike torch){
        ShapedRecipeBuilder.shaped(lantern)
                .define('|', nugget)
                .define('#', torch)
                .pattern("|||")
                .pattern("|#|")
                .pattern("|||")
                .unlockedBy(getHasName(torch), has(torch))
                .save(consumer, recipeID(lantern, nugget));
    }
    protected static void lantern(Consumer<FinishedRecipe> consumer, ItemLike lantern, TagKey<Item> nugget, ItemLike torch){
        ShapedRecipeBuilder.shaped(lantern)
                .define('|', nugget)
                .define('#', torch)
                .pattern("|||")
                .pattern("|#|")
                .pattern("|||")
                .unlockedBy(getHasName(torch), has(torch))
                .save(consumer, recipeID(lantern, nugget));
    }

    protected static void tools(Consumer<FinishedRecipe> consumer, ItemLike sword, ItemLike axe, ItemLike pickaxe, ItemLike shovel, ItemLike hoe, ItemLike material){
        sword(consumer, sword, material);
        axe(consumer, axe, material);
        pickaxe(consumer, pickaxe, material);
        shovel(consumer, shovel, material);
        hoe(consumer, hoe, material);
    }
    protected static void tools(Consumer<FinishedRecipe> consumer, ItemLike sword, ItemLike axe, ItemLike pickaxe, ItemLike shovel, ItemLike hoe, TagKey<Item> material){
        sword(consumer, sword, material);
        axe(consumer, axe, material);
        pickaxe(consumer, pickaxe, material);
        shovel(consumer, shovel, material);
        hoe(consumer, hoe, material);
    }
    protected static void sword(Consumer<FinishedRecipe> consumer, ItemLike sword, ItemLike material){
        ShapedRecipeBuilder.shaped(sword)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("#")
                .pattern("#")
                .pattern("|")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(sword, material));
    }
    protected static void sword(Consumer<FinishedRecipe> consumer, ItemLike sword, TagKey<Item> material){
        ShapedRecipeBuilder.shaped(sword)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("#")
                .pattern("#")
                .pattern("|")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(sword, material));
    }
    protected static void axe(Consumer<FinishedRecipe> consumer, ItemLike axe, ItemLike material){
        ShapedRecipeBuilder.shaped(axe)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("##")
                .pattern("#|")
                .pattern(" |")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(axe, material));
    }
    protected static void axe(Consumer<FinishedRecipe> consumer, ItemLike axe, TagKey<Item> material){
        ShapedRecipeBuilder.shaped(axe)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("##")
                .pattern("#|")
                .pattern(" |")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(axe, material));
    }
    protected static void pickaxe(Consumer<FinishedRecipe> consumer, ItemLike pickaxe, ItemLike material){
        ShapedRecipeBuilder.shaped(pickaxe)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("###")
                .pattern(" | ")
                .pattern(" | ")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(pickaxe, material));
    }
    protected static void pickaxe(Consumer<FinishedRecipe> consumer, ItemLike pickaxe, TagKey<Item> material){
        ShapedRecipeBuilder.shaped(pickaxe)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("###")
                .pattern(" | ")
                .pattern(" | ")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(pickaxe, material));
    }
    protected static void shovel(Consumer<FinishedRecipe> consumer, ItemLike shovel, ItemLike material){
        ShapedRecipeBuilder.shaped(shovel)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("#")
                .pattern("|")
                .pattern("|")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(shovel, material));
    }
    protected static void shovel(Consumer<FinishedRecipe> consumer, ItemLike shovel, TagKey<Item> material){
        ShapedRecipeBuilder.shaped(shovel)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("#")
                .pattern("|")
                .pattern("|")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(shovel, material));
    }
    protected static void hoe(Consumer<FinishedRecipe> consumer, ItemLike hoe, ItemLike material){
        ShapedRecipeBuilder.shaped(hoe)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("##")
                .pattern("| ")
                .pattern("| ")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(hoe, material));
    }
    protected static void hoe(Consumer<FinishedRecipe> consumer, ItemLike hoe, TagKey<Item> material){
        ShapedRecipeBuilder.shaped(hoe)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("##")
                .pattern("| ")
                .pattern("| ")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(hoe, material));
    }

    protected static void armor(Consumer<FinishedRecipe> consumer, ItemLike helmet, ItemLike chestplate, ItemLike leggings, ItemLike boots, ItemLike material){
        helmet(consumer, helmet, material);
        chestplate(consumer, chestplate, material);
        leggings(consumer, leggings, material);
        boots(consumer, boots, material);
    }
    protected static void armor(Consumer<FinishedRecipe> consumer, ItemLike helmet, ItemLike chestplate, ItemLike leggings, ItemLike boots, TagKey<Item> material){
        helmet(consumer, helmet, material);
        chestplate(consumer, chestplate, material);
        leggings(consumer, leggings, material);
        boots(consumer, boots, material);
    }
    protected static void helmet(Consumer<FinishedRecipe> consumer, ItemLike helmet, ItemLike material){
        ShapedRecipeBuilder.shaped(helmet).define('#', material)
                .pattern("###")
                .pattern("# #")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(helmet, material));
    }
    protected static void helmet(Consumer<FinishedRecipe> consumer, ItemLike helmet, TagKey<Item> material){
        ShapedRecipeBuilder.shaped(helmet).define('#', material)
                .pattern("###")
                .pattern("# #")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(helmet, material));
    }
    protected static void chestplate(Consumer<FinishedRecipe> consumer, ItemLike chestplate, ItemLike material){
        ShapedRecipeBuilder.shaped(chestplate).define('#', material)
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(chestplate, material));
    }
    protected static void chestplate(Consumer<FinishedRecipe> consumer, ItemLike chestplate, TagKey<Item> material){
        ShapedRecipeBuilder.shaped(chestplate).define('#', material)
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(chestplate, material));
    }
    protected static void leggings(Consumer<FinishedRecipe> consumer, ItemLike leggings, ItemLike material){
        ShapedRecipeBuilder.shaped(leggings).define('#', material)
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(leggings, material));
    }
    protected static void leggings(Consumer<FinishedRecipe> consumer, ItemLike leggings, TagKey<Item> material){
        ShapedRecipeBuilder.shaped(leggings).define('#', material)
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(leggings, material));
    }
    protected static void boots(Consumer<FinishedRecipe> consumer, ItemLike boots, ItemLike material){
        ShapedRecipeBuilder.shaped(boots).define('#', material)
                .pattern("# #")
                .pattern("# #")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(boots, material));
    }
    protected static void boots(Consumer<FinishedRecipe> consumer, ItemLike boots, TagKey<Item> material){
        ShapedRecipeBuilder.shaped(boots).define('#', material)
                .pattern("# #")
                .pattern("# #")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(boots, material));
    }

    protected static void fourConversion(Consumer<FinishedRecipe> consumer, ItemLike to, int count, ItemLike from){
        ShapedRecipeBuilder.shaped(to, count).define('#', from)
                .pattern("##")
                .pattern("##")
                .unlockedBy(getHasName(from), has(from))
                .save(consumer, recipeID(to, from));
    }

    protected static void nineBlockStorage(Consumer<FinishedRecipe> consumer, ItemLike unpacked, ItemLike packed) {
        ShapelessRecipeBuilder.shapeless(unpacked, 9).requires(packed).unlockedBy(getHasName(packed), has(packed)).save(consumer, recipeID(unpacked, packed));
        ShapedRecipeBuilder.shaped(packed).define('#', unpacked).pattern("###").pattern("###").pattern("###").unlockedBy(getHasName(unpacked), has(unpacked)).save(consumer, recipeID(packed, unpacked));
    }

    protected static void combine(Consumer<FinishedRecipe> consumer, ItemLike result, ItemLike block, ItemLike moss){
        ShapelessRecipeBuilder.shapeless(result).requires(block).requires(moss).unlockedBy(getHasName(block), has(block)).save(consumer, recipeID(result, block));
    }
    protected static void combine(Consumer<FinishedRecipe> consumer, ItemLike result, TagKey<Item> block, TagKey<Item> moss){
        ShapelessRecipeBuilder.shapeless(result).requires(block).requires(moss).unlockedBy(getHasName(block), has(block)).save(consumer, recipeID(result, block));
    }

    protected static void oreSmelting(Consumer<FinishedRecipe> consumer, ItemLike ingot, ItemLike raw){
        furnance(consumer, ingot, raw, 0.7f, 200);
        blasting(consumer, ingot, raw, 0.7f, 100);
    }
    protected static void oreSmelting(Consumer<FinishedRecipe> consumer, ItemLike ingot, TagKey<Item> raw){
        furnance(consumer, ingot, raw, 0.7f, 200);
        blasting(consumer, ingot, raw, 0.7f, 100);
    }
    protected static void stoneSmelting(Consumer<FinishedRecipe> consumer, ItemLike ingot, TagKey<Item> raw){
        furnance(consumer, ingot, raw, 0.1f, 200);
    }
    protected static void stoneSmelting(Consumer<FinishedRecipe> consumer, ItemLike ingot, ItemLike raw){
        furnance(consumer, ingot, raw, 0.1f, 200);
    }

    protected static void shapeless(Consumer<FinishedRecipe> consumer, ItemLike result, int countR, ItemLike material, int countM){
        ShapelessRecipeBuilder.shapeless(result, countR).requires(material, countM).unlockedBy(getHasName(material), has(material)).save(consumer, recipeID(result, material));
    }
    protected static void shapeless(Consumer<FinishedRecipe> consumer, ItemLike result, int countR, TagKey<Item> material, int countM){
        ShapelessRecipeBuilder.shapeless(result, countR).requires(Ingredient.of(material), countM).unlockedBy(getHasName(material), has(material)).save(consumer, recipeID(result, material));
    }
    protected static void stonecutting(Consumer<FinishedRecipe> consumer, ItemLike result, int count, ItemLike material){
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(material), result, count).unlockedBy(getHasName(material), has(material)).save(consumer, stonecuttingRecipeID(result, material));
    }
    protected static void furnance(Consumer<FinishedRecipe> consumer, ItemLike result, ItemLike ingredient, float xp, int time) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ingredient), result, xp, time).unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer, smeltingRecipeID(result, ingredient));
    }
    protected static void furnance(Consumer<FinishedRecipe> consumer, ItemLike result, TagKey<Item> ingredient, float xp, int time) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ingredient), result, xp, time).unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer, smeltingRecipeID(result, ingredient));
    }
    protected static void blasting(Consumer<FinishedRecipe> consumer, ItemLike result, ItemLike ingredient, float xp, int time) {
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(ingredient), result, xp, time).unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer, blastingRecipeID(result, ingredient));
    }
    protected static void blasting(Consumer<FinishedRecipe> consumer, ItemLike result, TagKey<Item> ingredient, float xp, int time) {
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(ingredient), result, xp, time).unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer, blastingRecipeID(result, ingredient));
    }

    protected static ResourceLocation recipeID(ItemLike result, ItemLike material) {
        String itemID = itemID(result);
        ResourceLocation recipeID = location(itemID);
        if(Recipes.contains(recipeID)){
            return location(getConversionRecipeName(itemID, material));
        }
        return recipeID;
    }
    protected static ResourceLocation recipeID(ItemLike result, TagKey<Item> material) {
        String itemID = itemID(result);
        ResourceLocation recipeID = location(itemID);
        if(Recipes.contains(recipeID)){
            return location(getConversionRecipeName(itemID, material));
        }
        return recipeID;
    }

    protected static ResourceLocation stonecuttingRecipeID(String key) {
        return location("stonecutting/" + key);
    }
    protected static ResourceLocation stonecuttingRecipeID(ItemLike result, ItemLike ingredient) {
        String itemID = itemID(result);
        ResourceLocation recipeID = stonecuttingRecipeID(itemID);
        if(Recipes.contains(recipeID)){
            return location("stonecutting/" + getConversionRecipeName(itemID, ingredient));
        }
        return recipeID;
    }
    protected static ResourceLocation smeltingRecipeID(String key) {
        return location("smelting/" + key);
    }
    protected static ResourceLocation smeltingRecipeID(ItemLike result, ItemLike ingredient) {
        String itemID = itemID(result);
        ResourceLocation recipeID = smeltingRecipeID(itemID);
        if(Recipes.contains(recipeID)){
            return location("smelting/" + getConversionRecipeName(itemID, ingredient));
        }
        return recipeID;
    }
    protected static ResourceLocation smeltingRecipeID(ItemLike result, TagKey<Item> ingredient) {
        String itemID = itemID(result);
        ResourceLocation recipeID = smeltingRecipeID(itemID);
        if(Recipes.contains(recipeID)){
            return location("smelting/" + getConversionRecipeName(itemID, ingredient));
        }
        return recipeID;
    }
    protected static ResourceLocation blastingRecipeID(String key) {
        return location("blasting/" + key);
    }
    protected static ResourceLocation blastingRecipeID(ItemLike result, ItemLike ingredient) {
        String itemID = itemID(result);
        ResourceLocation recipeID = blastingRecipeID(itemID);
        if(Recipes.contains(recipeID)){
            return location("blasting/" + getConversionRecipeName(itemID, ingredient));
        }
        return recipeID;
    }
    protected static ResourceLocation blastingRecipeID(ItemLike result, TagKey<Item> ingredient) {
        String itemID = itemID(result);
        ResourceLocation recipeID = blastingRecipeID(itemID);
        if(Recipes.contains(recipeID)){
            return location("blasting/" + getConversionRecipeName(itemID, ingredient));
        }
        return recipeID;
    }

    protected static String getConversionRecipeName(String result, ItemLike ingredient){
        return result + "_from_" + getItemName(ingredient);
    }

    protected static String getConversionRecipeName(String result, TagKey<Item> ingredient){
        return result + "_from_" + getItemTagName(ingredient);
    }

    protected static String getHasName(TagKey<Item> tag) {return "has_" + tag.location().getPath().replace('/', '_');}
    protected static String itemID(ItemLike item) {return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item.asItem())).getPath();}
    protected static String getItemTagName(TagKey<Item> tag) {return tag.location().getPath().replace('/', '_');}
    protected static ResourceLocation location(String key) {return new ResourceLocation(currentModID, key);}

    private static void saveRecipe(CachedOutput pOutput, JsonObject pRecipeJson, Path pPath) {
        try {
            DataProvider.saveStable(pOutput, pRecipeJson, pPath);
        } catch (IOException ioexception) {
            LOGGER.error("Couldn't save recipe {}", pPath, ioexception);
        }
    }
}