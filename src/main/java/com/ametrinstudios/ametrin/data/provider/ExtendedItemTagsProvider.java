package com.ametrinstudios.ametrin.data.provider;

import com.ametrinstudios.ametrin.data.ItemTagProviderRule;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;

import static com.ametrinstudios.ametrin.data.DataProviderExtensions.getItemName;

public abstract class ExtendedItemTagsProvider extends ItemTagsProvider {
    protected ArrayList<Item> excludedItems = new ArrayList<>();
    protected ArrayList<ItemTagProviderRule> itemTagProviderRules = new ArrayList<>();

    public ExtendedItemTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTagProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, lookupProvider, blockTagProvider, modId, existingFileHelper);
    }
    public ExtendedItemTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Item>> itemTagProvider, CompletableFuture<TagLookup<Block>> blockTagProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, lookupProvider, itemTagProvider, blockTagProvider, modId, existingFileHelper);
    }

    {
        itemTagProviderRules.add((item, name)-> {
            if(item instanceof AxeItem){
                tag(ItemTags.AXES).add(item);
            }
            if(item instanceof PickaxeItem){
                tag(ItemTags.PICKAXES).add(item);
            }
            if(item instanceof SwordItem){
                tag(ItemTags.SWORDS).add(item);
            }
            if(item instanceof ShovelItem){
                tag(ItemTags.SHOVELS).add(item);
            }
            if(item instanceof HoeItem){
                tag(ItemTags.HOES).add(item);
            }
        });
    }

    @Override
    protected abstract void addTags(@Nullable HolderLookup.Provider provider);

    protected void runRules(DeferredRegister<Item> register){
        runRules(register.getEntries().stream().map(RegistryObject::get).iterator());
    }

    protected void runRules(Iterator<? extends Item> items){
        items.forEachRemaining(item ->{
            if(excludedItems.contains(item)) return;
            final var name = getItemName(item);

            for(var rule : itemTagProviderRules) {
                rule.run(item, name);
            }
        });
    }
}
