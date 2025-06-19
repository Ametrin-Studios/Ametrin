package com.ametrinstudios.ametrin.data.provider;

import com.ametrinstudios.ametrin.data.ItemTagProviderRule;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.ametrinstudios.ametrin.data.DataProviderExtensions.getItemName;

public abstract class ExtendedItemTagsProvider extends ItemTagsProvider {
    private final List<Item> excludedItems = new ArrayList<>();
    private final List<ItemTagProviderRule> itemTagProviderRules = new ArrayList<>();

    public ExtendedItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId) {
        super(output, lookupProvider, modId);
    }

    {

        registerRule((item, name) -> {
            if (item instanceof BoatItem boat) {
                if (boat.getDescriptionId().contains("chest")) {
                    tag(ItemTags.CHEST_BOATS).add(item);
                } else {
                    tag(ItemTags.BOATS).add(item);
                }
            }
        });

        registerRule((item, name) -> {
            if (item instanceof SignItem) {
                tag(ItemTags.SIGNS).add(item);
            }
        });
    }

    @Override
    protected abstract void addTags(@NotNull HolderLookup.Provider provider);

    protected void runRules(DeferredRegister.Items register) {
        runRules(register.getEntries().stream().map(Supplier::get));
    }

    protected void excludeItem(ItemLike item) {
        excludedItems.add(item.asItem());
    }

    protected void registerRule(ItemTagProviderRule rule) {
        itemTagProviderRules.add(rule);
    }

    protected void runRules(Stream<? extends Item> items) {
        items.forEach(item -> {
            if (excludedItems.contains(item)) return;
            final var name = getItemName(item);
            var stack = item.getDefaultInstance();

            for (var rule : itemTagProviderRules) {
                rule.run(item, name);
            }



            if(stack.has(DataComponents.TOOL)){
                var tool = stack.get(DataComponents.TOOL);
            }
        });
    }
}
