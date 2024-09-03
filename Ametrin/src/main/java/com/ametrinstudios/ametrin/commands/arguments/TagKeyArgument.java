package com.ametrinstudios.ametrin.commands.arguments;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class TagKeyArgument<T> implements ArgumentType<TagKey<T>> {
    final ResourceKey<? extends Registry<T>> registryKey;

    public TagKeyArgument(ResourceKey<? extends Registry<T>> registryKey) {
        this.registryKey = registryKey;
    }

    @Override
    public TagKey<T> parse(StringReader reader) throws CommandSyntaxException {
        int i = reader.getCursor();

        try {
            reader.skip();
            var resourceLocation = ResourceLocation.read(reader);
            return TagKey.create(registryKey, resourceLocation);
        } catch (CommandSyntaxException commandsyntaxexception) {
            reader.setCursor(i);
            throw commandsyntaxexception;
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return context.getSource() instanceof SharedSuggestionProvider sharedsuggestionprovider
                ? sharedsuggestionprovider.suggestRegistryElements(this.registryKey, SharedSuggestionProvider.ElementSuggestionType.TAGS, builder, context)
                : builder.buildFuture();
    }

    public static class Info<T> implements ArgumentTypeInfo<TagKeyArgument<T>, TagKeyArgument.Info<T>.Template> {
        public void serializeToNetwork(Template template, FriendlyByteBuf buffer) {
            buffer.writeResourceKey(template.registryKey);
        }

        public @NotNull Template deserializeFromNetwork(FriendlyByteBuf buffer) {
            return new Template(buffer.readRegistryKey());
        }

        public void serializeToJson(Template template, JsonObject json) {
            json.addProperty("registry", template.registryKey.location().toString());
        }

        public @NotNull Template unpack(TagKeyArgument<T> argument) {
            return new Template(argument.registryKey);
        }

        public final class Template implements ArgumentTypeInfo.Template<TagKeyArgument<T>> {
            final ResourceKey<? extends Registry<T>> registryKey;

            Template(ResourceKey<? extends Registry<T>> registryKey) {
                this.registryKey = registryKey;
            }

            public @NotNull TagKeyArgument<T> instantiate(@NotNull CommandBuildContext context) {
                return new TagKeyArgument<>(this.registryKey);
            }

            @Override
            public @NotNull ArgumentTypeInfo<TagKeyArgument<T>, ?> type() {
                return TagKeyArgument.Info.this;
            }
        }
    }
}
