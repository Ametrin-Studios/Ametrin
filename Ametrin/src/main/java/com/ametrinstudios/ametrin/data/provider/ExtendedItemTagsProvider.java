package com.ametrinstudios.ametrin.data.provider;

import com.ametrinstudios.ametrin.data.ItemTagProviderRule;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static com.ametrinstudios.ametrin.data.DataProviderExtensions.getItemName;

public abstract class ExtendedItemTagsProvider extends ItemTagsProvider {
    private final List<Item> excludedItems = new ArrayList<>();
    private final List<ItemTagProviderRule> itemTagProviderRules = new ArrayList<>();

    public ExtendedItemTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTagProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, lookupProvider, blockTagProvider, modId, existingFileHelper);
    }
    public ExtendedItemTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Item>> itemTagProvider, CompletableFuture<TagLookup<Block>> blockTagProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, lookupProvider, itemTagProvider, blockTagProvider, modId, existingFileHelper);
    }

    {
        registerRule((item, name)-> {
            if(item instanceof AxeItem){
                tag(ItemTags.AXES).add(item);
            }else if(item instanceof PickaxeItem){
                tag(ItemTags.PICKAXES).add(item);
            } else if(item instanceof SwordItem){
                tag(ItemTags.SWORDS).add(item);
            } else if(item instanceof ShovelItem){
                tag(ItemTags.SHOVELS).add(item);
            } else if(item instanceof HoeItem){
                tag(ItemTags.HOES).add(item);
            }
        });

        registerRule((item, name)-> {
            if(item instanceof BoatItem boat){
                if(boat.getDescriptionId().contains("chest")) {
                    tag(ItemTags.CHEST_BOATS).add(item);
                } else {
                    tag(ItemTags.BOATS).add(item);
                }
            }
        });

        registerRule((item, name)-> {
            if(item instanceof SignItem){
                tag(ItemTags.SIGNS).add(item);
            }
        });

        registerRule((item, name) -> {
            if(item instanceof ArmorItem armorItem) {
                switch (armorItem.getEquipmentSlot(armorItem.getDefaultInstance())) {
                    case HEAD -> tag(ItemTags.HEAD_ARMOR).add(armorItem);
                    case CHEST -> tag(ItemTags.CHEST_ARMOR).add(armorItem);
                    case LEGS -> tag(ItemTags.LEG_ARMOR).add(armorItem);
                    case FEET -> tag(ItemTags.FOOT_ARMOR).add(armorItem);
                    case null, default -> { }
                }
            }
        });
    }

    @Override
    protected abstract void addTags(@NotNull HolderLookup.Provider provider);

    protected void runRules(DeferredRegister.Items register){
        runRules(register.getEntries().stream().map(Supplier::get).iterator());
    }

    protected void excludeItem(ItemLike item) {
        excludedItems.add(item.asItem());
    }

    protected void registerRule(ItemTagProviderRule rule) {
        itemTagProviderRules.add(rule);
    }

    protected void runRules(Iterator<? extends Item> items){
        items.forEachRemaining(item -> {
            if(excludedItems.contains(item)) return;
            final var name = getItemName(item);

            for(var rule : itemTagProviderRules) {
                rule.run(item, name);
            }
        });
    }
}
