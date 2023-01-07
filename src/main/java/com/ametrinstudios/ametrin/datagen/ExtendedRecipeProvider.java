package com.ametrinstudios.ametrin.datagen;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class ExtendedRecipeProvider extends RecipeProvider {
    protected static final Logger LOGGER = LogUtils.getLogger();
    protected static Set<ResourceLocation> Recipes = Sets.newHashSet();

    protected static String currentModID;
    protected String modID;

    public ExtendedRecipeProvider(PackOutput packOutput, String modID) {
        super(packOutput);
        this.modID = modID;
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput output){
        currentModID = modID;
        List<CompletableFuture<?>> list = new ArrayList<>();
        buildRecipes((recipe) -> {
            if (!Recipes.add(recipe.getId())) {
                throw new IllegalStateException("Duplicate recipe " + recipe.getId());
            } else {
                list.add(DataProvider.saveStable(output, recipe.serializeRecipe(), this.recipePathProvider.json(recipe.getId())));
                JsonObject serializedAdvancement = recipe.serializeAdvancement();
                if (serializedAdvancement != null) {
                    var saveAdvancementFuture = saveAdvancement(output, recipe, serializedAdvancement);
                    if (saveAdvancementFuture != null) {list.add(saveAdvancementFuture);}
                }
            }
        });
        currentModID = "";
        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
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
        if(hasStonecutting) {stonecutting(consumer, RecipeCategory.BUILDING_BLOCKS, stair, 1, material);}
    }
    protected static void stairs(Consumer<FinishedRecipe> consumer, ItemLike stair, ItemLike material, ItemLike... additionalStonecuttingMaterials){
        stairs(consumer, stair, material, true);
        for(ItemLike mat : additionalStonecuttingMaterials){
            stonecutting(consumer, RecipeCategory.BUILDING_BLOCKS, stair, 1, mat);
        }
    }

    protected static void slab(Consumer<FinishedRecipe> consumer, ItemLike slab, ItemLike material, boolean hasStonecutting){
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, slab, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(consumer, recipeID(slab, material));
        if(hasStonecutting) {stonecutting(consumer, RecipeCategory.BUILDING_BLOCKS, slab, 2, material);}
    }
    protected static void slab(Consumer<FinishedRecipe> consumer, ItemLike slab, ItemLike material, ItemLike... additionalStonecuttingMaterials){
        slab(consumer, slab, material, true);
        for(ItemLike mat : additionalStonecuttingMaterials){
            stonecutting(consumer, RecipeCategory.BUILDING_BLOCKS, slab, mat instanceof SlabBlock ? 1 : 2, mat);
        }
    }

    protected static void wall(Consumer<FinishedRecipe> consumer, ItemLike wall, ItemLike material, boolean hasStonecutting){
        wallBuilder(RecipeCategory.DECORATIONS, wall, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(consumer, recipeID(wall, material));
        if(hasStonecutting) {stonecutting(consumer, RecipeCategory.DECORATIONS, wall, 1, material);}
    }
    protected static void wall(Consumer<FinishedRecipe> consumer, ItemLike wall, ItemLike material, ItemLike... additionalStonecuttingMaterials){
        wall(consumer, wall, material, true);
        for(ItemLike mat : additionalStonecuttingMaterials){
            stonecutting(consumer, RecipeCategory.DECORATIONS, wall, 1, mat);
        }
    }

    protected static void button(Consumer<FinishedRecipe> consumer, ItemLike button, ItemLike material, boolean hasStonecutting){
        buttonBuilder(button, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(consumer, recipeID(button, material));
        if(hasStonecutting) {stonecutting(consumer, RecipeCategory.REDSTONE, button, 1, material);}
    }
    protected static void button(Consumer<FinishedRecipe> consumer, ItemLike button, ItemLike material, ItemLike... additionalStonecuttingMaterials) {
        button(consumer, button, material, true);
        for(ItemLike item : additionalStonecuttingMaterials) {stonecutting(consumer, RecipeCategory.REDSTONE, button, 1, item);}
    }

    protected static void chiseled(Consumer<FinishedRecipe> consumer, ItemLike chiseled, ItemLike material, boolean hasStonecutting){
        chiseledBuilder(RecipeCategory.BUILDING_BLOCKS, chiseled, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(consumer, recipeID(chiseled, material));
        if(hasStonecutting) {stonecutting(consumer, RecipeCategory.BUILDING_BLOCKS, chiseled, 1, material);}
    }
    protected static void fence(Consumer<FinishedRecipe> consumer, ItemLike fence, ItemLike material){
        fence(consumer, fence, 3, material, Items.STICK, false);
    }
    protected static void netherFence(Consumer<FinishedRecipe> consumer, ItemLike fence, ItemLike material){
        fence(consumer, fence, 6, material, Items.NETHER_BRICK, true);
    }
    protected static void fence(Consumer<FinishedRecipe> consumer, ItemLike fence, int count, ItemLike material, ItemLike stick, boolean hasStonecutting){
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, fence, count).define('W', material).define('#', stick).pattern("W#W").pattern("W#W").unlockedBy(getHasName(material), has(material)).save(consumer, recipeID(fence, material));
        if(hasStonecutting) {stonecutting(consumer, RecipeCategory.DECORATIONS, fence, 1, material);}
    }
    protected static void fenceGate(Consumer<FinishedRecipe> consumer, ItemLike fenceGate, ItemLike material){
        fenceGate(consumer, fenceGate, material, Items.STICK, false);
    }
    protected static void netherFenceGate(Consumer<FinishedRecipe> consumer, ItemLike fenceGate, ItemLike material){
        fenceGate(consumer, fenceGate, material, Items.NETHER_BRICK, true);
    }
    protected static void fenceGate(Consumer<FinishedRecipe> consumer, ItemLike fenceGate, ItemLike material, ItemLike stick, boolean hasStonecutting){
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, fenceGate).define('#', stick).define('W', material).pattern("#W#").pattern("#W#").unlockedBy(getHasName(material), has(material)).save(consumer, recipeID(fenceGate, material));
        if(hasStonecutting) {stonecutting(consumer, RecipeCategory.REDSTONE, fenceGate, 1, material);}
    }
    protected static void door(Consumer<FinishedRecipe> consumer, ItemLike door, ItemLike material){
        doorBuilder(door, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(consumer, recipeID(door, material));
    }
    protected static void trapdoor(Consumer<FinishedRecipe> consumer, ItemLike trapdoor, ItemLike material){
        trapdoorBuilder(trapdoor, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(consumer, recipeID(trapdoor, material));
    }

    protected static void torch(Consumer<FinishedRecipe> consumer, ItemLike torch, ItemLike flammable){
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, torch)
                .define('#', flammable)
                .define('|', Items.STICK)
                .pattern("#")
                .pattern("|")
                .unlockedBy(getHasName(flammable), has(flammable))
                .save(consumer, recipeID(torch, flammable));
    }
    protected static void torch(Consumer<FinishedRecipe> consumer, ItemLike torch, TagKey<Item> flammable){
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, torch)
                .define('#', flammable)
                .define('|', Items.STICK)
                .pattern("#")
                .pattern("|")
                .unlockedBy(getHasName(flammable), has(flammable))
                .save(consumer, recipeID(torch, flammable));
    }

    protected static void campfire(Consumer<FinishedRecipe> consumer, ItemLike campfire, ItemLike flammable){
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, campfire)
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
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, campfire)
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
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, lantern)
                .define('|', nugget)
                .define('#', torch)
                .pattern("|||")
                .pattern("|#|")
                .pattern("|||")
                .unlockedBy(getHasName(torch), has(torch))
                .save(consumer, recipeID(lantern, nugget));
    }
    protected static void lantern(Consumer<FinishedRecipe> consumer, ItemLike lantern, TagKey<Item> nugget, ItemLike torch){
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, lantern)
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
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, sword)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("#")
                .pattern("#")
                .pattern("|")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(sword, material));
    }
    protected static void sword(Consumer<FinishedRecipe> consumer, ItemLike sword, TagKey<Item> material){
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, sword)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("#")
                .pattern("#")
                .pattern("|")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(sword, material));
    }
    protected static void axe(Consumer<FinishedRecipe> consumer, ItemLike axe, ItemLike material){
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, axe)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("##")
                .pattern("#|")
                .pattern(" |")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(axe, material));
    }
    protected static void axe(Consumer<FinishedRecipe> consumer, ItemLike axe, TagKey<Item> material){
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, axe)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("##")
                .pattern("#|")
                .pattern(" |")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(axe, material));
    }
    protected static void pickaxe(Consumer<FinishedRecipe> consumer, ItemLike pickaxe, ItemLike material){
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, pickaxe)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("###")
                .pattern(" | ")
                .pattern(" | ")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(pickaxe, material));
    }
    protected static void pickaxe(Consumer<FinishedRecipe> consumer, ItemLike pickaxe, TagKey<Item> material){
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, pickaxe)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("###")
                .pattern(" | ")
                .pattern(" | ")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(pickaxe, material));
    }
    protected static void shovel(Consumer<FinishedRecipe> consumer, ItemLike shovel, ItemLike material){
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, shovel)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("#")
                .pattern("|")
                .pattern("|")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(shovel, material));
    }
    protected static void shovel(Consumer<FinishedRecipe> consumer, ItemLike shovel, TagKey<Item> material){
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, shovel)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("#")
                .pattern("|")
                .pattern("|")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(shovel, material));
    }
    protected static void hoe(Consumer<FinishedRecipe> consumer, ItemLike hoe, ItemLike material){
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, hoe)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("##")
                .pattern("| ")
                .pattern("| ")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(hoe, material));
    }
    protected static void hoe(Consumer<FinishedRecipe> consumer, ItemLike hoe, TagKey<Item> material){
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, hoe)
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
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, helmet).define('#', material)
                .pattern("###")
                .pattern("# #")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(helmet, material));
    }
    protected static void helmet(Consumer<FinishedRecipe> consumer, ItemLike helmet, TagKey<Item> material){
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, helmet).define('#', material)
                .pattern("###")
                .pattern("# #")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(helmet, material));
    }
    protected static void chestplate(Consumer<FinishedRecipe> consumer, ItemLike chestplate, ItemLike material){
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, chestplate).define('#', material)
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(chestplate, material));
    }
    protected static void chestplate(Consumer<FinishedRecipe> consumer, ItemLike chestplate, TagKey<Item> material){
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, chestplate).define('#', material)
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(chestplate, material));
    }
    protected static void leggings(Consumer<FinishedRecipe> consumer, ItemLike leggings, ItemLike material){
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, leggings).define('#', material)
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(leggings, material));
    }
    protected static void leggings(Consumer<FinishedRecipe> consumer, ItemLike leggings, TagKey<Item> material){
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, leggings).define('#', material)
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(leggings, material));
    }
    protected static void boots(Consumer<FinishedRecipe> consumer, ItemLike boots, ItemLike material){
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, boots).define('#', material)
                .pattern("# #")
                .pattern("# #")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(boots, material));
    }
    protected static void boots(Consumer<FinishedRecipe> consumer, ItemLike boots, TagKey<Item> material){
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, boots).define('#', material)
                .pattern("# #")
                .pattern("# #")
                .unlockedBy(getHasName(material), has(material))
                .save(consumer, recipeID(boots, material));
    }

    protected static void fourConversion(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike to, int count, ItemLike from){
        ShapedRecipeBuilder.shaped(category, to, count).define('#', from)
                .pattern("##")
                .pattern("##")
                .unlockedBy(getHasName(from), has(from))
                .save(consumer, recipeID(to, from));
    }

    protected static void nineBlockStorage(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike unpacked, ItemLike packed) {
        ShapelessRecipeBuilder.shapeless(category, unpacked, 9).requires(packed).unlockedBy(getHasName(packed), has(packed)).save(consumer, recipeID(unpacked, packed));
        ShapedRecipeBuilder.shaped(category, packed).define('#', unpacked).pattern("###").pattern("###").pattern("###").unlockedBy(getHasName(unpacked), has(unpacked)).save(consumer, recipeID(packed, unpacked));
    }

    protected static void combine(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike result, ItemLike block, ItemLike moss){
        ShapelessRecipeBuilder.shapeless(category, result).requires(block).requires(moss).unlockedBy(getHasName(block), has(block)).save(consumer, recipeID(result, block));
    }
    protected static void combine(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike result, TagKey<Item> block, TagKey<Item> moss){
        ShapelessRecipeBuilder.shapeless(category, result).requires(block).requires(moss).unlockedBy(getHasName(block), has(block)).save(consumer, recipeID(result, block));
    }

    protected static void oreSmelting(Consumer<FinishedRecipe> consumer, ItemLike ingot, ItemLike raw){
        smelting(consumer, RecipeCategory.MISC, ingot, raw, 0.7f, 200);
        blasting(consumer, RecipeCategory.MISC, ingot, raw, 0.7f, 100);
    }
    protected static void oreSmelting(Consumer<FinishedRecipe> consumer, ItemLike ingot, TagKey<Item> raw){
        smelting(consumer, RecipeCategory.MISC, ingot, raw, 0.7f, 200);
        blasting(consumer, RecipeCategory.MISC, ingot, raw, 0.7f, 100);
    }
    protected static void stoneSmelting(Consumer<FinishedRecipe> consumer, ItemLike ingot, TagKey<Item> raw){
        smelting(consumer, RecipeCategory.BUILDING_BLOCKS, ingot, raw, 0.1f, 200);
    }
    protected static void stoneSmelting(Consumer<FinishedRecipe> consumer, ItemLike ingot, ItemLike raw){
        smelting(consumer, RecipeCategory.BUILDING_BLOCKS, ingot, raw, 0.1f, 200);
    }

    protected static void shapeless(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike result, int countR, ItemLike material, int countM){
        ShapelessRecipeBuilder.shapeless(category, result, countR).requires(material, countM).unlockedBy(getHasName(material), has(material)).save(consumer, recipeID(result, material));
    }
    protected static void shapeless(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike result, int countR, TagKey<Item> material, int countM){
        ShapelessRecipeBuilder.shapeless(category, result, countR).requires(Ingredient.of(material), countM).unlockedBy(getHasName(material), has(material)).save(consumer, recipeID(result, material));
    }
    protected static void stonecutting(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike result, int count, ItemLike material){
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(material), category, result, count).unlockedBy(getHasName(material), has(material)).save(consumer, stonecuttingRecipeID(result, material));
    }
    protected static void smelting(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike result, ItemLike ingredient, float xp, int time) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ingredient), category, result, xp, time).unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer, smeltingRecipeID(result, ingredient));
    }
    protected static void smelting(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike result, TagKey<Item> ingredient, float xp, int time) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ingredient), category, result, xp, time).unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer, smeltingRecipeID(result, ingredient));
    }
    protected static void blasting(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike result, ItemLike ingredient, float xp, int time) {
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(ingredient), category, result, xp, time).unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer, blastingRecipeID(result, ingredient));
    }
    protected static void blasting(Consumer<FinishedRecipe> consumer, RecipeCategory category,  ItemLike result, TagKey<Item> ingredient, float xp, int time) {
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(ingredient), category, result, xp, time).unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer, blastingRecipeID(result, ingredient));
    }
    protected static void smoking(Consumer<FinishedRecipe> consumer, ItemLike result, ItemLike ingredient, float xp, int time) {
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(ingredient), RecipeCategory.FOOD, result, xp, time).unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer, smokingRecipeID(result, ingredient));
    }
    protected static void smoking(Consumer<FinishedRecipe> consumer, ItemLike result, TagKey<Item> ingredient, float xp, int time) {
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(ingredient), RecipeCategory.FOOD, result, xp, time).unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer, smokingRecipeID(result, ingredient));
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

    protected static ResourceLocation smokingRecipeID(String key) {
        return location("smoking/" + key);
    }
    protected static ResourceLocation smokingRecipeID(ItemLike result, ItemLike ingredient) {
        String itemID = itemID(result);
        ResourceLocation recipeID = smokingRecipeID(itemID);
        if(Recipes.contains(recipeID)){
            return location("smoking/" + getConversionRecipeName(itemID, ingredient));
        }
        return recipeID;
    }
    protected static ResourceLocation smokingRecipeID(ItemLike result, TagKey<Item> ingredient) {
        String itemID = itemID(result);
        ResourceLocation recipeID = smokingRecipeID(itemID);
        if(Recipes.contains(recipeID)){
            return location("smoking/" + getConversionRecipeName(itemID, ingredient));
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
}