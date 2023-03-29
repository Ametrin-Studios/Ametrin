package com.ametrinstudios.ametrin.data.provider.loot_table;

import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public abstract class ExtendedEntityLootSubProvider extends EntityLootSubProvider {

    protected ExtendedEntityLootSubProvider() {
        super(FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected abstract @NotNull Stream<EntityType<?>> getKnownEntityTypes();
}
