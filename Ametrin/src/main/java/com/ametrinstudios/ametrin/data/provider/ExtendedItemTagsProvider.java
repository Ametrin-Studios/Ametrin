package com.ametrinstudios.ametrin.data.provider;

import com.ametrinstudios.ametrin.data.ItemTagProviderRule;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public abstract class ExtendedItemTagsProvider extends ItemTagsProvider {
    private final List<Item> excludedItems = new ArrayList<>();
    private final List<ItemTagProviderRule> itemTagProviderRules = new ArrayList<>();

    public ExtendedItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId) {
        super(output, lookupProvider, modId);
    }

    {
        registerRule((item, name) -> {
            if (item.get() instanceof BoatItem boat) {
                if (name.contains("chest")) {
                    tag(ItemTags.CHEST_BOATS).add(boat);
                } else {
                    tag(ItemTags.BOATS).add(boat);
                }
            }
        });

        registerRule((item, name) -> {
            if (name.endsWith("_hanging_sign")) {
                tag(ItemTags.HANGING_SIGNS).add(item.get());
            } else if (name.endsWith("_sign")) {
                tag(ItemTags.SIGNS).add(item.get());
            }
        });
    }

    @Override
    protected abstract void addTags(HolderLookup.Provider provider);

    protected void runRules(DeferredRegister.Items register) {
        runRules(register.getEntries().stream());
    }

    protected void excludeItem(ItemLike item) {
        excludedItems.add(item.asItem());
    }

    protected void registerRule(ItemTagProviderRule rule) {
        itemTagProviderRules.add(rule);
    }

    protected void runRules(Stream<DeferredHolder<Item, ? extends Item>> items) {
        items.forEach(holder -> {
            if (excludedItems.contains(holder.get())) return;
            final var key = holder.getKey();
            final var name = key.identifier().getPath();

            for (var rule : itemTagProviderRules) {
                rule.run(holder, name);
            }
        });
    }
}
