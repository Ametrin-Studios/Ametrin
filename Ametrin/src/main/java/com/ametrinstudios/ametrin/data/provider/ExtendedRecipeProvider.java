package com.ametrinstudios.ametrin.data.provider;

import com.mojang.logging.LogUtils;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.conditions.ICondition;
import javax.annotation.Nullable;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public abstract class ExtendedRecipeProvider extends RecipeProvider {
    protected static final Logger LOGGER = LogUtils.getLogger();
    protected Set<ResourceLocation> knownRecipes = new HashSet<>();
    protected static final FeatureFlagSet DEFAULT_FEATURE_FLAG_SET = FeatureFlagSet.of(FeatureFlags.VANILLA);

    protected String modId;
    protected RecipeOutput output;
    protected HolderLookup.Provider registries;

    public ExtendedRecipeProvider(String modId, PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
        this.modId = modId;
    }

    @Override
    protected final void buildRecipes(RecipeOutput output, HolderLookup.Provider registries) {
        this.registries = registries;
        this.output = new RecipeOutput() {
            @Override
            public void accept(ResourceLocation id, Recipe<?> recipe, @Nullable AdvancementHolder advancement, ICondition... conditions) {
                knownRecipes.add(id);
                output.accept(id, recipe, advancement, conditions);
            }

            @Override
            public Advancement.Builder advancement() {
                return output.advancement();
            }
        };
        buildRecipes();
    }

    protected abstract void buildRecipes();

    protected void stairs(ItemLike stair, ItemLike material, boolean hasStonecutting) {
        stairBuilder(stair, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(output, recipeID(stair, material));
        if (hasStonecutting) {
            stonecutting(RecipeCategory.BUILDING_BLOCKS, stair, material, 1);
        }
    }

    protected void stairs(ItemLike stair, ItemLike material, ItemLike... additionalStonecuttingMaterials) {
        stairs(stair, material, true);
        for (var mat : additionalStonecuttingMaterials) {
            stonecutting(RecipeCategory.BUILDING_BLOCKS, stair, mat, 1);
        }
    }

    protected void slab(ItemLike slab, ItemLike material, boolean hasStonecutting) {
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, slab, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(output, recipeID(slab, material));
        if (hasStonecutting) {
            stonecutting(RecipeCategory.BUILDING_BLOCKS, slab, material, 2);
        }
    }

    protected void slab(ItemLike slab, ItemLike material, ItemLike... additionalStonecuttingMaterials) {
        slab(slab, material, true);
        for (ItemLike mat : additionalStonecuttingMaterials) {
            stonecutting(RecipeCategory.BUILDING_BLOCKS, slab, mat, getItemName(mat).contains("slab") ? 1 : 2);
        }
    }

    protected void wall(ItemLike wall, ItemLike material, boolean hasStonecutting) {
        wallBuilder(RecipeCategory.DECORATIONS, wall, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(output, recipeID(wall, material));
        if (hasStonecutting) {
            stonecutting(RecipeCategory.DECORATIONS, wall, material, 1);
        }
    }

    protected void wall(ItemLike wall, ItemLike material, ItemLike... additionalStonecuttingMaterials) {
        wall(wall, material, true);
        for (var mat : additionalStonecuttingMaterials) {
            stonecutting(RecipeCategory.DECORATIONS, wall, mat, 1);
        }
    }

    protected void button(ItemLike button, ItemLike material, boolean hasStonecutting) {
        buttonBuilder(button, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(output, recipeID(button, material));
        if (hasStonecutting) {
            stonecutting(RecipeCategory.REDSTONE, button, material, 1);
        }
    }

    protected void button(ItemLike button, ItemLike material, ItemLike... additionalStonecuttingMaterials) {
        button(button, material, true);
        for (var item : additionalStonecuttingMaterials) {
            stonecutting(RecipeCategory.REDSTONE, button, item, 1);
        }
    }

    protected void chiseled(ItemLike chiseled, ItemLike material, boolean hasStonecutting) {
        chiseledBuilder(RecipeCategory.BUILDING_BLOCKS, chiseled, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(output, recipeID(chiseled, material));
        if (hasStonecutting) {
            stonecutting(RecipeCategory.BUILDING_BLOCKS, chiseled, material, 1);
        }
    }

    protected void fence(ItemLike fence, ItemLike material) {
        fence(fence, 3, material, Items.STICK, false);
    }

    protected void netherFence(ItemLike fence, ItemLike material) {
        fence(fence, 6, material, Items.NETHER_BRICK, true);
    }

    protected void fence(ItemLike fence, int count, ItemLike material, ItemLike stick, boolean hasStonecutting) {
        shaped(RecipeCategory.DECORATIONS, fence, count).define('W', material).define('#', stick).pattern("W#W").pattern("W#W").unlockedBy(getHasName(material), has(material)).save(output, recipeID(fence, material));
        if (hasStonecutting) {
            stonecutting(RecipeCategory.DECORATIONS, fence, material, 1);
        }
    }

    protected void fenceGate(ItemLike fenceGate, ItemLike material) {
        fenceGate(fenceGate, material, Items.STICK, false);
    }

    protected void netherFenceGate(ItemLike fenceGate, ItemLike material) {
        fenceGate(fenceGate, material, Items.NETHER_BRICK, true);
    }

    protected void fenceGate(ItemLike fenceGate, ItemLike material, ItemLike stick, boolean hasStonecutting) {
        shaped(RecipeCategory.REDSTONE, fenceGate).define('#', stick).define('W', material).pattern("#W#").pattern("#W#").unlockedBy(getHasName(material), has(material)).save(output, recipeID(fenceGate, material));
        if (hasStonecutting) {
            stonecutting(RecipeCategory.REDSTONE, fenceGate, material, 1);
        }
    }

    protected void door(ItemLike door, ItemLike material) {
        doorBuilder(door, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(output, recipeID(door, material));
    }

    protected void trapdoor(ItemLike trapdoor, ItemLike material) {
        trapdoorBuilder(trapdoor, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(output, recipeID(trapdoor, material));
    }

    protected void torch(ItemLike torch, ItemLike flammable) {
        shaped(RecipeCategory.DECORATIONS, torch)
                .define('#', flammable)
                .define('|', Items.STICK)
                .pattern("#")
                .pattern("|")
                .unlockedBy(getHasName(flammable), has(flammable))
                .save(output, recipeID(torch, flammable));
    }

    protected void torch(ItemLike torch, TagKey<Item> flammable) {
        shaped(RecipeCategory.DECORATIONS, torch)
                .define('#', flammable)
                .define('|', Items.STICK)
                .pattern("#")
                .pattern("|")
                .unlockedBy(getHasName(flammable), has(flammable))
                .save(output, recipeID(torch, flammable));
    }

    protected void campfire(ItemLike campfire, ItemLike flammable) {
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

    protected void campfire(ItemLike campfire, TagKey<Item> flammable) {
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

    protected void lantern(ItemLike lantern, ItemLike nugget, ItemLike torch) {
        shaped(RecipeCategory.DECORATIONS, lantern)
                .define('|', nugget)
                .define('#', torch)
                .pattern("|||")
                .pattern("|#|")
                .pattern("|||")
                .unlockedBy(getHasName(torch), has(torch))
                .save(output, recipeID(lantern, nugget));
    }

    protected void lantern(ItemLike lantern, TagKey<Item> nugget, ItemLike torch) {
        shaped(RecipeCategory.DECORATIONS, lantern)
                .define('|', nugget)
                .define('#', torch)
                .pattern("|||")
                .pattern("|#|")
                .pattern("|||")
                .unlockedBy(getHasName(torch), has(torch))
                .save(output, recipeID(lantern, nugget));
    }

    protected void tools(ItemLike sword, ItemLike axe, ItemLike pickaxe, ItemLike shovel, ItemLike hoe, ItemLike material) {
        sword(sword, material);
        axe(axe, material);
        pickaxe(pickaxe, material);
        shovel(shovel, material);
        hoe(hoe, material);
    }

    protected void tools(ItemLike sword, ItemLike axe, ItemLike pickaxe, ItemLike shovel, ItemLike hoe, TagKey<Item> material) {
        sword(sword, material);
        axe(axe, material);
        pickaxe(pickaxe, material);
        shovel(shovel, material);
        hoe(hoe, material);
    }

    protected void sword(ItemLike sword, ItemLike material) {
        shaped(RecipeCategory.COMBAT, sword)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("#")
                .pattern("#")
                .pattern("|")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(sword, material));
    }

    protected void sword(ItemLike sword, TagKey<Item> material) {
        shaped(RecipeCategory.COMBAT, sword)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("#")
                .pattern("#")
                .pattern("|")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(sword, material));
    }

    protected void axe(ItemLike axe, ItemLike material) {
        shaped(RecipeCategory.TOOLS, axe)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("##")
                .pattern("#|")
                .pattern(" |")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(axe, material));
    }

    protected void axe(ItemLike axe, TagKey<Item> material) {
        shaped(RecipeCategory.TOOLS, axe)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("##")
                .pattern("#|")
                .pattern(" |")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(axe, material));
    }

    protected void pickaxe(ItemLike pickaxe, ItemLike material) {
        shaped(RecipeCategory.TOOLS, pickaxe)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("###")
                .pattern(" | ")
                .pattern(" | ")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(pickaxe, material));
    }

    protected void pickaxe(ItemLike pickaxe, TagKey<Item> material) {
        shaped(RecipeCategory.TOOLS, pickaxe)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("###")
                .pattern(" | ")
                .pattern(" | ")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(pickaxe, material));
    }

    protected void shovel(ItemLike shovel, ItemLike material) {
        shaped(RecipeCategory.TOOLS, shovel)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("#")
                .pattern("|")
                .pattern("|")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(shovel, material));
    }

    protected void shovel(ItemLike shovel, TagKey<Item> material) {
        shaped(RecipeCategory.TOOLS, shovel)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("#")
                .pattern("|")
                .pattern("|")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(shovel, material));
    }

    protected void hoe(ItemLike hoe, ItemLike material) {
        shaped(RecipeCategory.TOOLS, hoe)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("##")
                .pattern("| ")
                .pattern("| ")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(hoe, material));
    }

    protected void hoe(ItemLike hoe, TagKey<Item> material) {
        shaped(RecipeCategory.TOOLS, hoe)
                .define('#', material)
                .define('|', Items.STICK)
                .pattern("##")
                .pattern("| ")
                .pattern("| ")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(hoe, material));
    }

    protected void armor(ItemLike helmet, ItemLike chestplate, ItemLike leggings, ItemLike boots, ItemLike material) {
        helmet(helmet, material);
        chestplate(chestplate, material);
        leggings(leggings, material);
        boots(boots, material);
    }

    protected void armor(ItemLike helmet, ItemLike chestplate, ItemLike leggings, ItemLike boots, TagKey<Item> material) {
        helmet(helmet, material);
        chestplate(chestplate, material);
        leggings(leggings, material);
        boots(boots, material);
    }

    protected void helmet(ItemLike helmet, ItemLike material) {
        shaped(RecipeCategory.COMBAT, helmet).define('#', material)
                .pattern("###")
                .pattern("# #")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(helmet, material));
    }

    protected void helmet(ItemLike helmet, TagKey<Item> material) {
        shaped(RecipeCategory.COMBAT, helmet).define('#', material)
                .pattern("###")
                .pattern("# #")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(helmet, material));
    }

    protected void chestplate(ItemLike chestplate, ItemLike material) {
        shaped(RecipeCategory.COMBAT, chestplate).define('#', material)
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(chestplate, material));
    }

    protected void chestplate(ItemLike chestplate, TagKey<Item> material) {
        shaped(RecipeCategory.COMBAT, chestplate).define('#', material)
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(chestplate, material));
    }

    protected void leggings(ItemLike leggings, ItemLike material) {
        shaped(RecipeCategory.COMBAT, leggings).define('#', material)
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(leggings, material));
    }

    protected void leggings(ItemLike leggings, TagKey<Item> material) {
        shaped(RecipeCategory.COMBAT, leggings).define('#', material)
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(leggings, material));
    }

    protected void boots(ItemLike boots, ItemLike material) {
        shaped(RecipeCategory.COMBAT, boots).define('#', material)
                .pattern("# #")
                .pattern("# #")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(boots, material));
    }

    protected void boots(ItemLike boots, TagKey<Item> material) {
        shaped(RecipeCategory.COMBAT, boots).define('#', material)
                .pattern("# #")
                .pattern("# #")
                .unlockedBy(getHasName(material), has(material))
                .save(output, recipeID(boots, material));
    }

    protected void fourConversion(RecipeCategory category, ItemLike to, int count, ItemLike from) {
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

    protected void combine(RecipeCategory category, ItemLike result, ItemLike block, ItemLike moss) {
        shapeless(category, result).requires(block).requires(moss).unlockedBy(getHasName(block), has(block)).save(output, recipeID(result, block));
    }

    protected void combine(RecipeCategory category, ItemLike result, TagKey<Item> block, TagKey<Item> moss) {
        shapeless(category, result).requires(block).requires(moss).unlockedBy(getHasName(block), has(block)).save(output, recipeID(result, block));
    }

    @Deprecated
    protected void dying(TagKey<Item> dyedItems, String idPattern, String group) {
        for (var dye : DyeColor.values()) {
            var resultID = locate(idPattern.replace("{color}", dye.getName()));
            var dyeID = ResourceLocation.withDefaultNamespace(dye.getName() + "_dye");
            var result = BuiltInRegistries.ITEM.getOptional(resultID).orElseThrow();
            var dyeItem = BuiltInRegistries.ITEM.getOptional(dyeID).orElseThrow();
            shapeless(RecipeCategory.BUILDING_BLOCKS, result).requires(dyedItems)
                    .requires(dyeItem).group(group)
                    .unlockedBy("has_needed_dye", has(dyeItem))
                    .save(output, locate("dye_" + getItemName(result)));
        }
    }

    public void dyeing8(RecipeCategory category, ItemLike result, TagKey<Item> source, ItemLike dye, String group) {
        shaped(category, result, 8)
                .define('#', source)
                .define('X', dye)
                .pattern("###")
                .pattern("#X#")
                .pattern("###")
                .group(group)
                .unlockedBy(getHasName(source), has(source))
                .save(output, dyeingRecipeID(result, dye));
    }

    public void dyeing8(RecipeCategory category, ItemLike result, ItemLike source, ItemLike dye, String group) {
        shaped(category, result, 8)
                .define('#', source)
                .define('X', dye)
                .pattern("###")
                .pattern("#X#")
                .pattern("###")
                .group(group)
                .unlockedBy(getHasName(source), has(source))
                .save(output, dyeingRecipeID(result, dye));
    }

    protected void dyeing1(RecipeCategory category, ItemLike result, ItemLike source, ItemLike dye, String group) {
        shapeless(category, result)
                .requires(source).requires(dye).group(group)
                .unlockedBy(getHasName(source), has(source))
                .save(output, dyeingRecipeID(result, dye));

    }

    protected void dyeing1(RecipeCategory category, ItemLike result, TagKey<Item> source, ItemLike dye, String group) {
        shapeless(category, result)
                .requires(source).requires(dye).group(group)
                .unlockedBy(getHasName(source), has(source))
                .save(output, dyeingRecipeID(result, dye));

    }

    protected void generateRecipes(BlockFamily family, FeatureFlagSet flagSet) {
        generateRecipes(output, family, flagSet);
    }

    protected void generateSmeltingConversionRecipes(BlockFamily resultFamily, BlockFamily baseFamily, FeatureFlagSet flagSet) {
        resultFamily.getVariants().forEach((variant, result) -> {
            if (result.requiredFeatures().isSubsetOf(flagSet)) {
                var base = baseFamily.get(variant);
                if (base != null && base.requiredFeatures().isSubsetOf(flagSet)) {
                    smelting(variant == BlockFamily.Variant.WALL ? RecipeCategory.DECORATIONS : RecipeCategory.BUILDING_BLOCKS, result, base, 0.1F, 200);
                }
            }
        });
    }

    protected void generateStonecuttingConversionRecipes(BlockFamily resultFamily, BlockFamily baseFamily, FeatureFlagSet flagSet) {
        resultFamily.getVariants().forEach((variant, result) -> {
            if (result.requiredFeatures().isSubsetOf(flagSet)) {
                var base = baseFamily.get(variant);
                if (base != null && base.requiredFeatures().isSubsetOf(flagSet)) {
                    stonecutting(variant == BlockFamily.Variant.WALL ? RecipeCategory.DECORATIONS : RecipeCategory.BUILDING_BLOCKS, result, base, 1);
                }
            }
        });
    }

    protected void oreSmelting(ItemLike ingot, ItemLike raw) {
        smelting(RecipeCategory.MISC, ingot, raw, 0.7f, 200);
        blasting(RecipeCategory.MISC, ingot, raw, 0.7f, 100);
    }

    protected void oreSmelting(ItemLike ingot, TagKey<Item> raw) {
        smelting(RecipeCategory.MISC, ingot, raw, 0.7f, 200);
        blasting(RecipeCategory.MISC, ingot, raw, 0.7f, 100);
    }

    protected void stoneSmelting(ItemLike ingot, TagKey<Item> raw) {
        smelting(RecipeCategory.BUILDING_BLOCKS, ingot, raw, 0.1f, 200);
    }

    protected void stoneSmelting(ItemLike ingot, ItemLike raw) {
        smelting(RecipeCategory.BUILDING_BLOCKS, ingot, raw, 0.1f, 200);
    }

    protected ShapedRecipeBuilder shaped(RecipeCategory category, ItemLike result) {
        return ShapedRecipeBuilder.shaped(category, result);
    }

    protected ShapedRecipeBuilder shaped(RecipeCategory category, ItemLike result, int count) {
        return ShapedRecipeBuilder.shaped(category, result, count);
    }

    protected ShapelessRecipeBuilder shapeless(RecipeCategory category, ItemLike result) {
        return ShapelessRecipeBuilder.shapeless(category, result);
    }

    protected ShapelessRecipeBuilder shapeless(RecipeCategory category, ItemLike result, int count) {
        return ShapelessRecipeBuilder.shapeless(category, result, count);
    }

    protected void shapeless(RecipeCategory category, ItemLike result, int countR, ItemLike material, int countM) {
        shapeless(category, result, countR).requires(material, countM).unlockedBy(getHasName(material), has(material)).save(output, recipeID(result, material));
    }

    protected void shapeless(RecipeCategory category, ItemLike result, int countR, TagKey<Item> material, int countM) {
        shapeless(category, result, countR).requires(Ingredient.of(material), countM).unlockedBy(getHasName(material), has(material)).save(output, recipeID(result, material));
    }

    protected void stonecutting(RecipeCategory category, ItemLike result, ItemLike material, int count) {
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(material), category, result, count).unlockedBy(getHasName(material), has(material)).save(output, stonecuttingRecipeID(result, material));
    }

    protected void smelting(RecipeCategory category, ItemLike result, ItemLike ingredient, float xp, int time) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ingredient), category, result, xp, time).unlockedBy(getHasName(ingredient), has(ingredient)).save(output, smeltingRecipeID(result, ingredient));
    }

    protected void smelting(RecipeCategory category, ItemLike result, TagKey<Item> ingredient, float xp, int time) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ingredient), category, result, xp, time).unlockedBy(getHasName(ingredient), has(ingredient)).save(output, smeltingRecipeID(result, ingredient));
    }

    protected void blasting(RecipeCategory category, ItemLike result, ItemLike ingredient, float xp, int time) {
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(ingredient), category, result, xp, time).unlockedBy(getHasName(ingredient), has(ingredient)).save(output, blastingRecipeID(result, ingredient));
    }

    protected void blasting(RecipeCategory category, ItemLike result, TagKey<Item> ingredient, float xp, int time) {
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(ingredient), category, result, xp, time).unlockedBy(getHasName(ingredient), has(ingredient)).save(output, blastingRecipeID(result, ingredient));
    }

    protected void smoking(ItemLike result, ItemLike ingredient, float xp, int time) {
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(ingredient), RecipeCategory.FOOD, result, xp, time).unlockedBy(getHasName(ingredient), has(ingredient)).save(output, smokingRecipeID(result, ingredient));
    }

    protected void smoking(ItemLike result, TagKey<Item> ingredient, float xp, int time) {
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(ingredient), RecipeCategory.FOOD, result, xp, time).unlockedBy(getHasName(ingredient), has(ingredient)).save(output, smokingRecipeID(result, ingredient));
    }

    protected ResourceLocation recipeID(ItemLike result, ItemLike ingredient) {
        return prefixedRecipeID(result, ingredient, "");
    }

    protected ResourceLocation recipeID(ItemLike result, TagKey<Item> ingredient) {
        return prefixedRecipeID(result, ingredient, "");
    }

    protected ResourceLocation stonecuttingRecipeID(ItemLike result, ItemLike ingredient) {
        return prefixedRecipeID(result, ingredient, "stonecutting/");
    }

    protected ResourceLocation stonecuttingRecipeID(ItemLike result, TagKey<Item> ingredient) {
        return prefixedRecipeID(result, ingredient, "stonecutting/");
    }

    protected ResourceLocation smeltingRecipeID(ItemLike result, ItemLike ingredient) {
        return prefixedRecipeID(result, ingredient, "smelting/");
    }

    protected ResourceLocation smeltingRecipeID(ItemLike result, TagKey<Item> ingredient) {
        return prefixedRecipeID(result, ingredient, "smelting/");
    }

    protected ResourceLocation blastingRecipeID(ItemLike result, ItemLike ingredient) {
        return prefixedRecipeID(result, ingredient, "blasting/");
    }

    protected ResourceLocation blastingRecipeID(ItemLike result, TagKey<Item> ingredient) {
        return prefixedRecipeID(result, ingredient, "blasting/");
    }

    protected ResourceLocation smokingRecipeID(ItemLike result, ItemLike ingredient) {
        return prefixedRecipeID(result, ingredient, "smoking/");
    }

    protected ResourceLocation smokingRecipeID(ItemLike result, TagKey<Item> ingredient) {
        return prefixedRecipeID(result, ingredient, "smoking/");
    }

    protected ResourceLocation dyeingRecipeID(ItemLike result, ItemLike ingredient) {
        return prefixedRecipeID(result, ingredient, "dyeing/");
    }

    protected ResourceLocation dyeingRecipeID(ItemLike result, TagKey<Item> ingredient) {
        return prefixedRecipeID(result, ingredient, "dyeing/");
    }

    protected ResourceLocation prefixedRecipeID(ItemLike result, ItemLike ingredient, String prefix) {
        final var itemID = getItemName(result);
        var recipeID = locate(prefix + itemID);
        if (knownRecipes.contains(recipeID)) {
            recipeID = locate(prefix + getConversionRecipeName(itemID, ingredient));
            if (knownRecipes.contains(recipeID)) throw new IllegalStateException("duplicate recipe key:" + recipeID);
        }
        return recipeID;
    }

    protected ResourceLocation prefixedRecipeID(ItemLike result, TagKey<Item> ingredient, String prefix) {
        final var itemID = getItemName(result);
        var recipeID = locate(prefix + itemID);
        if (knownRecipes.contains(recipeID)) {
            recipeID = locate(prefix + getConversionRecipeName(itemID, ingredient));
            if (knownRecipes.contains(recipeID)) throw new IllegalStateException("duplicate recipe key:" + recipeID);
        }
        return recipeID;
    }

    protected String getConversionRecipeName(String result, ItemLike ingredient) {
        return result + "_from_" + getItemName(ingredient);
    }

    protected String getConversionRecipeName(String result, TagKey<Item> ingredient) {
        return result + "_from_" + getItemTagName(ingredient);
    }

    protected String getHasName(TagKey<Item> tag) {
        return "has_" + getItemTagName(tag);
    }

    protected String getItemTagName(TagKey<Item> tag) {
        return tag.location().getPath().replace('/', '_');
    }

    protected ResourceLocation locate(String key) {
        if (key.contains(":")) {
            return ResourceLocation.bySeparator(key, ':');
        }
        // explicitly create from consumer provided mod id
        return ResourceLocation.fromNamespaceAndPath(modId, key);
    }

    protected FamilyBuilder family(BlockFamily family) {
        return new FamilyBuilder(family, DEFAULT_FEATURE_FLAG_SET);
    }

    protected FamilyBuilder family(BlockFamily family, FeatureFlagSet featureFlags) {
        return new FamilyBuilder(family, featureFlags);
    }

    protected final class FamilyBuilder {
        private final BlockFamily family;
        private final FeatureFlagSet featureFlags;

        public FamilyBuilder(BlockFamily family, FeatureFlagSet featureFlags) {
            this.family = family;
            this.featureFlags = featureFlags;
        }

        public FamilyBuilder generate() {
            generateRecipes(family, featureFlags);
            return this;
        }

        public FamilyBuilder generateStonecuttingConversions(BlockFamily baseFamily) {
            generateStonecuttingConversionRecipes(family, baseFamily, featureFlags);
            return this;
        }

        public FamilyBuilder generateSmeltingConversions(BlockFamily baseFamily) {
            generateSmeltingConversionRecipes(family, baseFamily, featureFlags);
            return this;
        }
    }
}
