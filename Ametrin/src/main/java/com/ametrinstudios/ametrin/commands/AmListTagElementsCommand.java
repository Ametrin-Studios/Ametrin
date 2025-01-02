package com.ametrinstudios.ametrin.commands;

import com.ametrinstudios.ametrin.commands.arguments.TagKeyArgument;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public final class AmListTagElementsCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("ametrin")
                .then(Commands.literal("list")
                        .then(Commands.literal("biomes")
                                .then(Commands.argument("tag", new TagKeyArgument<>(Registries.BIOME))
                                        .executes(command -> lol(command.getSource(), (TagKey<Biome>) command.getArgument("tag", TagKey.class), Registries.BIOME))
                                )
                        )
                )
        );
    }

    private static <T> int lol(CommandSourceStack context, TagKey<T> tag, ResourceKey<Registry<T>> registryKey) {
        var output = new StringBuilder();
        output.append(tag.location());
        output.append(" contains:");
        output.append('\n');


        for (var holder : context.getLevel().registryAccess().lookup(registryKey).orElseThrow().getTagOrEmpty(tag)) {
            output.append(holder.getRegisteredName());
            output.append('\n');
        }

        output.deleteCharAt(output.length() - 1);

        context.sendSuccess(() -> Component.literal(output.toString()), false);
        return 1;
    }
}
