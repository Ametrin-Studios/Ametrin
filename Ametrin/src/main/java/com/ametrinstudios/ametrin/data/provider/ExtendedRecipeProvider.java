package com.ametrinstudios.ametrin.data.provider;

import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public abstract class ExtendedRecipeProvider extends RecipeProvider {
    protected static final Logger LOGGER = LogUtils.getLogger();
    protected Set<ResourceLocation> recipes;

    protected String modID;

    public ExtendedRecipeProvider(String modID, HolderLookup.Provider registries, RecipeOutput output, Set<ResourceLocation> recipeSet) {
        super(registries, output);
        this.modID = modID;
        this.recipes = recipeSet;
    }

    @Override
    protected abstract void buildRecipes();

    protected void stairSlabWallButton(@Nullable ItemLike stair, @Nullable ItemLike slab, @Nullable ItemLike wall, @Nullable ItemLike button, ItemLike material, boolean hasStonecutting){
        if(stair != null) { stairs(stair, material, hasStonecutting); }
        if(slab != null) { slab(slab, material, hasStonecutting); }
        if(wall != null) { wall(wall, material, hasStonecutting); }
        if(button != null) { button(button, material, hasStonecutting); }
    }
    protected void stairSlabWallButton(@Nullable ItemLike stair, @Nullable ItemLike slab, @Nullable ItemLike wall, @Nullable ItemLike button, ItemLike material, ItemLike... additionalStonecuttingMaterials){
        if(stair != null) { stairs(stair, material, additionalStonecuttingMaterials); }
        if(slab != null) { slab(slab, material, additionalStonecuttingMaterials); }
        if(wall != null) { wall(wall, material, additionalStonecuttingMaterials); }
        if(button != null) { button(button, material, additionalStonecuttingMaterials); }
    }

    protected void stairs(ItemLike stair, ItemLike material, boolean hasStonecutting){
        stairBuilder(stair, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(output, recipeID(stair, material));
        if(hasStonecutting) { stonecutting(RecipeCategory.BUILDING_BLOCKS, stair, 1, material); }
    }
    protected void stairs(ItemLike stair, ItemLike material, ItemLike... additionalStonecuttingMaterials){
        stairs(stair, material, true);
        for(ItemLike mat : additionalStonecuttingMaterials) {
            stonecutting(RecipeCategory.BUILDING_BLOCKS, stair, 1, mat);
        }
    }

    protected void slab(ItemLike slab, ItemLike material, boolean hasStonecutting){
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, slab, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(output, recipeID(slab, material));
        if(hasStonecutting) { stonecutting(RecipeCategory.BUILDING_BLOCKS, slab, 2, material); }
    }
    protected void slab(ItemLike slab, ItemLike material, ItemLike... additionalStonecuttingMaterials){
        slab(slab, material, true);
        for(ItemLike mat : additionalStonecuttingMaterials){
            stonecutting(RecipeCategory.BUILDING_BLOCKS, slab, getItemName(mat).contains("slab") ? 1 : 2, mat);
        }
    }

    protected void wall(ItemLike wall, ItemLike material, boolean hasStonecutting){
        wallBuilder(RecipeCategory.DECORATIONS, wall, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(output, recipeID(wall, material));
        if(hasStonecutting) { stonecutting(RecipeCategory.DECORATIONS, wall, 1, material); }
    }
    protected void wall(ItemLike wall, ItemLike material, ItemLike... additionalStonecuttingMaterials){
        wall(wall, material, true);
        for(ItemLike mat : additionalStonecuttingMaterials){
            stonecutting(RecipeCategory.DECORATIONS, wall, 1, mat);
        }
    }

    protected void button(ItemLike button, ItemLike material, boolean hasStonecutting){
        buttonBuilder(button, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(output, recipeID(button, material));
        if(hasStonecutting) {stonecutting(RecipeCategory.REDSTONE, button, 1, material); }
    }
    protected void button(ItemLike button, ItemLike material, ItemLike... additionalStonecuttingMaterials) {
        button(button, material, true);
        for(ItemLike item : additionalStonecuttingMaterials) { stonecutting(RecipeCategory.REDSTONE, button, 1, item); }
    }

    protected void chiseled(ItemLike chiseled, ItemLike material, boolean hasStonecutting){
        chiseledBuilder(RecipeCategory.BUILDING_BLOCKS, chiseled, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(output, recipeID(chiseled, material));
        if(hasStonecutting) { stonecutting(RecipeCategory.BUILDING_BLOCKS, chiseled, 1, material); }
    }
    protected void fence(ItemLike fence, ItemLike material){
        fence(fence, 3, material, Items.STICK, false);
    }
    protected void netherFence(ItemLike fence, ItemLike material){
        fence(fence, 6, material, Items.NETHER_BRICK, true);
    }
    protected void fence(ItemLike fence, int count, ItemLike material, ItemLike stick, boolean hasStonecutting){
        shaped(RecipeCategory.DECORATIONS, fence, count).define('W', material).define('#', stick).pattern("W#W").pattern("W#W").unlockedBy(getHasName(material), has(material)).save(output, recipeID(fence, material));
        if(hasStonecutting) {stonecutting(RecipeCategory.DECORATIONS, fence, 1, material); }
    }
    protected void fenceGate(ItemLike fenceGate, ItemLike material){
        fenceGate(fenceGate, material, Items.STICK, false);
    }
    protected void netherFenceGate(ItemLike fenceGate, ItemLike material){
        fenceGate(fenceGate, material, Items.NETHER_BRICK, true);
    }
    protected void fenceGate(ItemLike fenceGate, ItemLike material, ItemLike stick, boolean hasStonecutting){
        shaped(RecipeCategory.REDSTONE, fenceGate).define('#', stick).define('W', material).pattern("#W#").pattern("#W#").unlockedBy(getHasName(material), has(material)).save(output, recipeID(fenceGate, material));
        if(hasStonecutting) {stonecutting(RecipeCategory.REDSTONE, fenceGate, 1, material); }
    }
    protected void door(ItemLike door, ItemLike material){
        doorBuilder(door, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(output, recipeID(door, material));
    }
    protected void trapdoor(ItemLike trapdoor, ItemLike material){
        trapdoorBuilder(trapdoor, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(output, recipeID(trapdoor, material));
    }

    protected void torch(ItemLike torch, ItemLike flammable){
        shaped(RecipeCategory.DECORATIONS, torch)
                .define('#', flammable)
                .define('|', Items.STICK)
                .pattern("#")
                .pattern("|")
                .unlockedBy(getHasName(flammable), has(flammable))
                .save(output, recipeID(torch, flammable));
    }
    protected void torch(ItemLike torch, TagKey<Item> flammable){
        shaped(RecipeCategory.DECORATIONS, torch)
                .define('#', flammable)
                .define('|', Items.STICK)
                .pattern("#")
                .pattern("|")
                .unlockedBy(getHasName(flammable), has(flammable))
                .save(output, recipeID(torch, flammable));
    }

    protected void campfire(ItemLike campfire, ItemLike flammable){
        shaped(RecipeCategory.DECORATIONS, campfire)
                .define('|', Items.STICK)
                .define('#', flammable)
                .define('O', ItemTags.LOGS_THAT_BURN)
                .pattern(" | ")
                .pattern("|#|")
                .pattern("OOO")
                .unlockedBy(getHasName(flammable), has(flammable))
                .save(output, recipeID(campfire, flammable));
    }
    protected void campfire(ItemLike campfire, TagKey<Item> flammable){
        shaped(RecipeCategory.DECORATIONS, campfire)
                .define('|', Items.STICK)
                .define('#', flammable)
                .define('O', ItemTags.LOGS_THAT_BURN)
                .pattern(" | ")
                .pattern("|#|")
                .pattern("OOO")
                .unlockedBy(getHasName(flammable), has(flammable))
                .save(output, recipeID(campfire, flammable));
    }

    protected void lantern(ItemLike lantern, ItemLike nugget, ItemLike torch){
        shaped(RecipeCategory.DECORATIONS, lantern)
                .define('|', nugget)
                .define('#', torch)
                .pattern("|||")
                .pattern("|#|")
                .pattern("|||")
                .unlockedBy(getHasName(torch), has(torch))
                .save(output, recipeID(lantern, nugget));
    }
    protected void lantern(ItemLike lantern, TagKey<Item> nugget, ItemLike torch){
        shaped(RecipeCategory.DECORATIONS, lantern)
                .define('|', nugget)
                .define('#', torch)
                .pattern("|||")
                .pattern("|#|")
                .pattern("|||")
                .unlockedBy(getHasName(torch), has(torch))
                .save(output, recipeID(lantern, nugget));
    }

    protected void tools(ItemLike sword, ItemLike axe, ItemLike pickaxe, ItemLike shovel, ItemLike hoe, ItemLike material){
        sword(sword, material);
        axe(axe, material);
        pickaxe(pickaxe, material);
        shovel(shovel, material);
        hoe(hoe, material);
    }
    protected void tools(ItemLike sword, ItemLike axe, ItemLike pickaxe, ItemLike shovel, ItemLike hoe, TagKey<Item> material){
        sword(sword, material);
        axe(axe, material);
        pickaxe(pickaxe, material);
        shovel(shovel, material);
        hoe(hoe, material);
    }
    protected void sword(ItemLike sword, ItemLike material){
        shaped(RecipeCategory.COMBAT, sword)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("#")
                .pattern("#")
                .pattern("|")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(sword, material));
    }
    protected void sword(ItemLike sword, TagKey<Item> material){
        shaped(RecipeCategory.COMBAT, sword)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("#")
                .pattern("#")
                .pattern("|")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(sword, material));
    }
    protected void axe(ItemLike axe, ItemLike material){
        shaped(RecipeCategory.TOOLS, axe)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("##")
                .pattern("#|")
                .pattern(" |")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(axe, material));
    }
    protected void axe(ItemLike axe, TagKey<Item> material){
        shaped(RecipeCategory.TOOLS, axe)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("##")
                .pattern("#|")
                .pattern(" |")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(axe, material));
    }
    protected void pickaxe(ItemLike pickaxe, ItemLike material){
        shaped(RecipeCategory.TOOLS, pickaxe)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("###")
                .pattern(" | ")
                .pattern(" | ")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(pickaxe, material));
    }
    protected void pickaxe(ItemLike pickaxe, TagKey<Item> material){
        shaped(RecipeCategory.TOOLS, pickaxe)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("###")
                .pattern(" | ")
                .pattern(" | ")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(pickaxe, material));
    }
    protected void shovel(ItemLike shovel, ItemLike material){
        shaped(RecipeCategory.TOOLS, shovel)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("#")
                .pattern("|")
                .pattern("|")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(shovel, material));
    }
    protected void shovel(ItemLike shovel, TagKey<Item> material){
        shaped(RecipeCategory.TOOLS, shovel)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("#")
                .pattern("|")
                .pattern("|")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(shovel, material));
    }
    protected void hoe(ItemLike hoe, ItemLike material){
        shaped(RecipeCategory.TOOLS, hoe)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("##")
                .pattern("| ")
                .pattern("| ")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(hoe, material));
    }
    protected void hoe(ItemLike hoe, TagKey<Item> material){
        shaped(RecipeCategory.TOOLS, hoe)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("##")
                .pattern("| ")
                .pattern("| ")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(hoe, material));
    }

    protected void armor(ItemLike helmet, ItemLike chestplate, ItemLike leggings, ItemLike boots, ItemLike material){
        helmet(helmet, material);
        chestplate(chestplate, material);
        leggings(leggings, material);
        boots(boots, material);
    }
    protected void armor(ItemLike helmet, ItemLike chestplate, ItemLike leggings, ItemLike boots, TagKey<Item> material){
        helmet(helmet, material);
        chestplate(chestplate, material);
        leggings(leggings, material);
        boots(boots, material);
    }
    protected void helmet(ItemLike helmet, ItemLike material){
        shaped(RecipeCategory.COMBAT, helmet).define('#', material)
                .pattern("###")
                .pattern("# #")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(helmet, material));
    }
    protected void helmet(ItemLike helmet, TagKey<Item> material){
        shaped(RecipeCategory.COMBAT, helmet).define('#', material)
                .pattern("###")
                .pattern("# #")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(helmet, material));
    }
    protected void chestplate(ItemLike chestplate, ItemLike material){
        shaped(RecipeCategory.COMBAT, chestplate).define('#', material)
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(chestplate, material));
    }
    protected void chestplate(ItemLike chestplate, TagKey<Item> material){
        shaped(RecipeCategory.COMBAT, chestplate).define('#', material)
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(chestplate, material));
    }
    protected void leggings(ItemLike leggings, ItemLike material){
        shaped(RecipeCategory.COMBAT, leggings).define('#', material)
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(leggings, material));
    }
    protected void leggings(ItemLike leggings, TagKey<Item> material){
        shaped(RecipeCategory.COMBAT, leggings).define('#', material)
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(leggings, material));
    }
    protected void boots(ItemLike boots, ItemLike material){
        shaped(RecipeCategory.COMBAT, boots).define('#', material)
                .pattern("# #")
                .pattern("# #")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(boots, material));
    }
    protected void boots(ItemLike boots, TagKey<Item> material){
        shaped(RecipeCategory.COMBAT, boots).define('#', material)
                .pattern("# #")
                .pattern("# #")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(boots, material));
    }

    protected void fourConversion(RecipeCategory category, ItemLike to, int count, ItemLike from){
        shaped(category, to, count).define('#', from)
                .pattern("##")
                .pattern("##")
                .unlockedBy(getHasName(from), has(from))
                .save(output, recipeID(to, from));
    }

    protected void nineBlockStorage(RecipeCategory category, ItemLike unpacked, ItemLike packed) {
        shapeless(category, unpacked, 9).requires(packed).unlockedBy(getHasName(packed), has(packed)).save(output, recipeID(unpacked, packed));
        shaped(category, packed).define('#', unpacked).pattern("###").pattern("###").pattern("###").unlockedBy(getHasName(unpacked), has(unpacked)).save(output, recipeID(packed, unpacked));
    }

    protected void combine(RecipeCategory category, ItemLike result, ItemLike block, ItemLike moss){
        shapeless(category, result).requires(block).requires(moss).unlockedBy(getHasName(block), has(block)).save(output, recipeID(result, block));
    }
    protected void combine(RecipeCategory category, ItemLike result, TagKey<Item> block, TagKey<Item> moss){
        shapeless(category, result).requires(block).requires(moss).unlockedBy(getHasName(block), has(block)).save(output, recipeID(result, block));
    }

    protected void dying(TagKey<Item> dyedItems, String idPattern, String group){
        for(var dye : DyeColor.values()){
            var resultID = locate(idPattern.replace("{color}", dye.getName()));
            var dyeID = ResourceLocation.withDefaultNamespace(dye.getName() + "_dye");
            var result = BuiltInRegistries.ITEM.get(resultID).orElseThrow().value();
            var dyeItem = BuiltInRegistries.ITEM.get(dyeID).orElseThrow().value();
            shapeless(RecipeCategory.BUILDING_BLOCKS, result).requires(dyedItems)
                    .requires(dyeItem).group(group)
                    .unlockedBy("has_needed_dye", has(dyeItem))
                    .save(output, ResourceKey.create(Registries.RECIPE, locate("dye_" + getItemName(result))));
        }
    }

    protected void oreSmelting(ItemLike ingot, ItemLike raw){
        smelting(RecipeCategory.MISC, ingot, raw, 0.7f, 200);
        blasting(RecipeCategory.MISC, ingot, raw, 0.7f, 100);
    }
    protected void oreSmelting(ItemLike ingot, TagKey<Item> raw){
        smelting(RecipeCategory.MISC, ingot, raw, 0.7f, 200);
        blasting(RecipeCategory.MISC, ingot, raw, 0.7f, 100);
    }
    protected void stoneSmelting(ItemLike ingot, TagKey<Item> raw){
        smelting(RecipeCategory.BUILDING_BLOCKS, ingot, raw, 0.1f, 200);
    }
    protected void stoneSmelting(ItemLike ingot, ItemLike raw){
        smelting(RecipeCategory.BUILDING_BLOCKS, ingot, raw, 0.1f, 200);
    }

    protected void shapeless(RecipeCategory category, ItemLike result, int countR, ItemLike material, int countM){
        shapeless(category, result, countR).requires(material, countM).unlockedBy(getHasName(material), has(material)).save(output, recipeID(result, material));
    }
    protected void shapeless(RecipeCategory category, ItemLike result, int countR, TagKey<Item> material, int countM){
        shapeless(category, result, countR).requires(tag(material), countM).unlockedBy(getHasName(material), has(material)).save(output, recipeID(result, material));
    }
    protected void stonecutting(RecipeCategory category, ItemLike result, int count, ItemLike material){
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(material), category, result, count).unlockedBy(getHasName(material), has(material)).save(output, stonecuttingRecipeID(result, material));
    }
    protected void smelting(RecipeCategory category, ItemLike result, ItemLike ingredient, float xp, int time) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ingredient), category, result, xp, time).unlockedBy(getHasName(ingredient), has(ingredient)).save(output, smeltingRecipeID(result, ingredient));
    }
    protected void smelting(RecipeCategory category, ItemLike result, TagKey<Item> ingredient, float xp, int time) {
        SimpleCookingRecipeBuilder.smelting(tag(ingredient), category, result, xp, time).unlockedBy(getHasName(ingredient), has(ingredient)).save(output, smeltingRecipeID(result, ingredient));
    }
    protected void blasting(RecipeCategory category, ItemLike result, ItemLike ingredient, float xp, int time) {
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(ingredient), category, result, xp, time).unlockedBy(getHasName(ingredient), has(ingredient)).save(output, blastingRecipeID(result, ingredient));
    }
    protected void blasting(RecipeCategory category, ItemLike result, TagKey<Item> ingredient, float xp, int time) {
        SimpleCookingRecipeBuilder.blasting(tag(ingredient), category, result, xp, time).unlockedBy(getHasName(ingredient), has(ingredient)).save(output, blastingRecipeID(result, ingredient));
    }
    protected void smoking(ItemLike result, ItemLike ingredient, float xp, int time) {
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(ingredient), RecipeCategory.FOOD, result, xp, time).unlockedBy(getHasName(ingredient), has(ingredient)).save(output, smokingRecipeID(result, ingredient));
    }
    protected void smoking(ItemLike result, TagKey<Item> ingredient, float xp, int time) {
        SimpleCookingRecipeBuilder.blasting(tag(ingredient), RecipeCategory.FOOD, result, xp, time).unlockedBy(getHasName(ingredient), has(ingredient)).save(output, smokingRecipeID(result, ingredient));
    }

    protected ResourceKey<Recipe<?>> recipeID(ItemLike result, ItemLike material) {
        String itemID = getItemName(result);
        ResourceLocation recipeID = locate(itemID);
        if(recipes.contains(recipeID)){
            return ResourceKey.create(Registries.RECIPE, locate(getConversionRecipeName(itemID, material)));
        }
        return ResourceKey.create(Registries.RECIPE, recipeID);
    }
    protected ResourceKey<Recipe<?>> recipeID(ItemLike result, TagKey<Item> material) {
        String itemID = getItemName(result);
        ResourceLocation recipeID = locate(itemID);
        if(recipes.contains(recipeID)){
            return ResourceKey.create(Registries.RECIPE, locate(getConversionRecipeName(itemID, material)));
        }
        return ResourceKey.create(Registries.RECIPE, recipeID);
    }

    protected ResourceLocation stonecuttingRecipeID(String key) {
        return locate("stonecutting/" + key);
    }
    protected ResourceKey<Recipe<?>> stonecuttingRecipeID(ItemLike result, ItemLike ingredient) {
        String itemID = getItemName(result);
        ResourceLocation recipeID = stonecuttingRecipeID(itemID);
        if(recipes.contains(recipeID)) {
            return ResourceKey.create(Registries.RECIPE, locate("stonecutting/" + getConversionRecipeName(itemID, ingredient)));
        }
        return ResourceKey.create(Registries.RECIPE, recipeID);
    }
    protected ResourceLocation smeltingRecipeID(String key) {
        return locate("smelting/" + key);
    }
    protected ResourceKey<Recipe<?>> smeltingRecipeID(ItemLike result, ItemLike ingredient) {
        String itemID = getItemName(result);
        ResourceLocation recipeID = smeltingRecipeID(itemID);
        if(recipes.contains(recipeID)) {
            return ResourceKey.create(Registries.RECIPE, locate("smelting/" + getConversionRecipeName(itemID, ingredient)));
        }
        return ResourceKey.create(Registries.RECIPE, recipeID);
    }
    protected ResourceKey<Recipe<?>> smeltingRecipeID(ItemLike result, TagKey<Item> ingredient) {
        String itemID = getItemName(result);
        ResourceLocation recipeID = smeltingRecipeID(itemID);
        if(recipes.contains(recipeID)){
            return ResourceKey.create(Registries.RECIPE, locate("smelting/" + getConversionRecipeName(itemID, ingredient)));
        }
        return ResourceKey.create(Registries.RECIPE, recipeID);
    }
    protected ResourceLocation blastingRecipeID(String key) {
        return locate("blasting/" + key);
    }
    protected ResourceKey<Recipe<?>> blastingRecipeID(ItemLike result, ItemLike ingredient) {
        String itemID = getItemName(result);
        ResourceLocation recipeID = blastingRecipeID(itemID);
        if(recipes.contains(recipeID)){
            return ResourceKey.create(Registries.RECIPE, locate("blasting/" + getConversionRecipeName(itemID, ingredient)));
        }
        return ResourceKey.create(Registries.RECIPE, recipeID);
    }
    protected ResourceKey<Recipe<?>> blastingRecipeID(ItemLike result, TagKey<Item> ingredient) {
        String itemID = getItemName(result);
        ResourceLocation recipeID = blastingRecipeID(itemID);
        if(recipes.contains(recipeID)){
            return ResourceKey.create(Registries.RECIPE, locate("blasting/" + getConversionRecipeName(itemID, ingredient)));
        }
        return ResourceKey.create(Registries.RECIPE, recipeID);
    }

    protected ResourceLocation smokingRecipeID(String key) {
        return locate("smoking/" + key);
    }
    protected ResourceKey<Recipe<?>> smokingRecipeID(ItemLike result, ItemLike ingredient) {
        String itemID = getItemName(result);
        ResourceLocation recipeID = smokingRecipeID(itemID);
        if(recipes.contains(recipeID)){
            return ResourceKey.create(Registries.RECIPE, locate("smoking/" + getConversionRecipeName(itemID, ingredient)));
        }
        return ResourceKey.create(Registries.RECIPE, recipeID);
    }
    protected ResourceKey<Recipe<?>> smokingRecipeID(ItemLike result, TagKey<Item> ingredient) {
        String itemID = getItemName(result);
        ResourceLocation recipeID = smokingRecipeID(itemID);
        if(recipes.contains(recipeID)){
            return ResourceKey.create(Registries.RECIPE, locate("smoking/" + getConversionRecipeName(itemID, ingredient)));
        }
        return ResourceKey.create(Registries.RECIPE, recipeID);
    }

    protected String getConversionRecipeName(String result, ItemLike ingredient){
        return result + "_from_" + getItemName(ingredient);
    }

    protected String getConversionRecipeName(String result, TagKey<Item> ingredient){
        return result + "_from_" + getItemTagName(ingredient);
    }

    protected String getHasName(TagKey<Item> tag) { return "has_" + getItemTagName(tag); }
    protected String getItemTagName(TagKey<Item> tag) { return tag.location().getPath().replace('/', '_'); }
    protected ResourceLocation locate(String key) {
        if(key.contains(":")){
            return ResourceLocation.bySeparator(key, ':');
        }
        return ResourceLocation.fromNamespaceAndPath(modID, key);
    }

    public static abstract class Runner implements DataProvider {
        private final PackOutput packOutput;
        private final CompletableFuture<HolderLookup.Provider> registries;
        private final static Set<ResourceLocation> recipes = Sets.newHashSet();

        protected Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            this.packOutput = packOutput;
            this.registries = registries;
        }

        @Override
        public final @NotNull CompletableFuture<?> run(@NotNull CachedOutput output) {
            return this.registries
                    .thenCompose(
                            provider -> {
                                final var recipeProvider = this.packOutput.createRegistryElementsPathProvider(Registries.RECIPE);
                                final var advancementsProvider = this.packOutput.createRegistryElementsPathProvider(Registries.ADVANCEMENT);
                                final List<CompletableFuture<?>> list = new ArrayList<>();
                                RecipeOutput recipeoutput = new RecipeOutput() {
                                    @Override @ParametersAreNonnullByDefault
                                    public void accept(ResourceKey<Recipe<?>> id, Recipe<?> recipe, @Nullable AdvancementHolder advancementHolder, net.neoforged.neoforge.common.conditions.ICondition... conditions) {
                                        if (!recipes.add(id.location())) {
                                            throw new IllegalStateException("Duplicate recipe " + id.location());
                                        } else {
                                            this.saveRecipe(id, recipe, conditions);
                                            if (advancementHolder != null) {
                                                this.saveAdvancement(advancementHolder, conditions);
                                            }
                                        }
                                    }

                                    @Override
                                    public @NotNull Advancement.Builder advancement() {
                                        return Advancement.Builder.recipeAdvancement().parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT);
                                    }

                                    @Override
                                    public void includeRootAdvancement() {
                                        AdvancementHolder advancementholder = Advancement.Builder.recipeAdvancement()
                                                .addCriterion("impossible", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
                                                .build(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT);
                                        this.saveAdvancement(advancementholder);
                                    }

                                    private void saveRecipe(ResourceKey<Recipe<?>> key, Recipe<?> recipe) {
                                        saveRecipe(key, recipe, new net.neoforged.neoforge.common.conditions.ICondition[0]);
                                    }
                                    private void saveRecipe(ResourceKey<Recipe<?>> key, Recipe<?> recipe, net.neoforged.neoforge.common.conditions.ICondition... conditions) {
                                        list.add(
                                                DataProvider.saveStable(output, provider, Recipe.CONDITIONAL_CODEC, Optional.of(new net.neoforged.neoforge.common.conditions.WithConditions<>(recipe, conditions)), recipeProvider.json(key.location()))
                                        );
                                    }

                                    private void saveAdvancement(AdvancementHolder advancementHolder) {
                                        saveAdvancement(advancementHolder, new net.neoforged.neoforge.common.conditions.ICondition[0]);
                                    }
                                    private void saveAdvancement(AdvancementHolder p_363148_, net.neoforged.neoforge.common.conditions.ICondition... conditions) {
                                        list.add(
                                                DataProvider.saveStable(
                                                        output, provider, Advancement.CONDITIONAL_CODEC, Optional.of(new net.neoforged.neoforge.common.conditions.WithConditions<>(p_363148_.value(), conditions)), advancementsProvider.json(p_363148_.id())
                                                )
                                        );
                                    }
                                };
                                this.createRecipeProvider(provider, recipeoutput, recipes).buildRecipes();
                                return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
                            }
                    );
        }


        protected abstract ExtendedRecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput output, Set<ResourceLocation> recipeSet);
    }
}