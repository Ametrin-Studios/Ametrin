package com.ametrinstudios.ametrin.util;

import com.ametrinstudios.ametrin.Ametrin;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class AmUtil {
    public static ResourceLocation location(String key) { return ResourceLocation.fromNamespaceAndPath(Ametrin.MOD_ID, key); }
}