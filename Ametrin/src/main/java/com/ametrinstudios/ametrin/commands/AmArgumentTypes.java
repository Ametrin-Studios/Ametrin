package com.ametrinstudios.ametrin.commands;

import com.ametrinstudios.ametrin.Ametrin;
import com.ametrinstudios.ametrin.commands.arguments.TagKeyArgument;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class AmArgumentTypes {
    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> REGISTER = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, Ametrin.MOD_ID);

    public static final Supplier<ArgumentTypeInfo<?, ?>> TAG_KEY = REGISTER.register("tag_key", ()-> ArgumentTypeInfos.registerByClass(fixClassType(TagKeyArgument.class), new TagKeyArgument.Info<>()));


    private static <T extends ArgumentType<?>> Class<T> fixClassType(Class<? super T> type) {
        return (Class<T>)type;
    }

}
