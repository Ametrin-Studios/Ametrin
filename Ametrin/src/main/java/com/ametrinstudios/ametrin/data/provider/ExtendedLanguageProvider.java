package com.ametrinstudios.ametrin.data.provider;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.LanguageProvider;

public abstract class ExtendedLanguageProvider extends LanguageProvider {
    public ExtendedLanguageProvider(PackOutput output, String mod_id, String locale) {
        super(output, mod_id, locale);
    }

    protected void add(ItemLike itemLike, String name) {
        add(itemLike.asItem(), name);
    }
}
