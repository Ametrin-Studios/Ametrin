package com.ametrinstudios.ametrin.data.provider;

import com.ametrinstudios.ametrin.data.ItemTagProviderRule;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public abstract class ExtendedItemTagsProvider extends ItemTagsProvider {
    private final List<ResourceKey<Item>> excludedItems = new ArrayList<>();
    private final List<ItemTagProviderRule> itemTagProviderRules = new ArrayList<>();

    public ExtendedItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId) {
        super(output, lookupProvider, modId);
    }

    {

        registerRule((item, name) -> {
            if (item.get() instanceof BoatItem boat) {
                if (boat.getDescriptionId().contains("chest")) {
                    tag(ItemTags.CHEST_BOATS).add(item.getKey());
                } else {
                    tag(ItemTags.BOATS).add(item.getKey());
                }
            }
        });

        registerRule((item, name) -> {
            if (item.get() instanceof SignItem) {
                tag(ItemTags.SIGNS).add(item.getKey());
            }
        });
    }

    @Override
    protected abstract void addTags(@NotNull HolderLookup.Provider provider);

    protected void runRules(DeferredRegister.Items register) {
        runRules(register.getEntries().stream());
    }

    protected void excludeItem(ResourceKey<Item> item) {
        excludedItems.add(item);
    }

    protected void registerRule(ItemTagProviderRule rule) {
        itemTagProviderRules.add(rule);
    }

    protected void runRules(Stream<DeferredHolder<Item, ? extends Item>> items) {
        items.forEach(holder -> {
            final var key = holder.getKey();
            if (excludedItems.contains(key)) return;
            final var name = key.identifier().getPath();

            for (var rule : itemTagProviderRules) {
                rule.run(holder, name);
            }
        });
    }
}
