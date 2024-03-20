package com.ametrinstudios.ametrin.data.provider;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredItem;

public abstract class ExtendedLanguageProvider extends LanguageProvider {
    public ExtendedLanguageProvider(PackOutput output, String mod_id, String locale) {
        super(output, mod_id, locale);
    }

    private void add(DeferredItem<Item> deferredItem, String name){
        add(deferredItem.asItem(), name);
    }
}
