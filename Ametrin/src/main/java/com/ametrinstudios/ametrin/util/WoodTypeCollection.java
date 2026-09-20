package com.ametrinstudios.ametrin.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.apache.commons.lang3.function.TriConsumer;
import org.apache.commons.lang3.function.TriFunction;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public record WoodTypeCollection<T>(Map<WoodType, T> map) {
    public static final List<WoodType> VANILLA_TYPES = WoodType.values().toList();
    public static final List<WoodType> VANILLA_OVERWORLD_TYPES = List.of(WoodType.OAK, WoodType.SPRUCE, WoodType.BIRCH, WoodType.JUNGLE, WoodType.ACACIA, WoodType.DARK_OAK, WoodType.MANGROVE, WoodType.CHERRY, WoodType.PALE_OAK, WoodType.POPLAR);
    public static final List<WoodType> VANILLA_NETHER_TYPES = List.of(WoodType.CRIMSON, WoodType.WARPED);

    public static final WoodTypeCollection<Block> VANILLA_OVERWORLD_LOGS = new WoodTypeCollection<>(ImmutableMap.of(
            WoodType.OAK, Blocks.OAK_LOG,
            WoodType.SPRUCE, Blocks.SPRUCE_LOG,
            WoodType.BIRCH, Blocks.BIRCH_LOG,
            WoodType.JUNGLE, Blocks.JUNGLE_LOG,
            WoodType.ACACIA, Blocks.ACACIA_LOG,
            WoodType.DARK_OAK, Blocks.DARK_OAK_LOG,
            WoodType.MANGROVE, Blocks.MANGROVE_LOG,
            WoodType.CHERRY, Blocks.CHERRY_LOG,
            WoodType.PALE_OAK, Blocks.PALE_OAK_LOG,
            WoodType.POPLAR, Blocks.POPLAR_LOG
    ));

    public static final WoodTypeCollection<Block> VANILLA_NETHER_STEMS = new WoodTypeCollection<>(ImmutableMap.of(
            WoodType.CRIMSON, Blocks.CRIMSON_STEM,
            WoodType.WARPED, Blocks.WARPED_STEM
    ));

    public static final WoodTypeCollection<Block> VANILLA_LOGS = WoodTypeCollection.combine(VANILLA_OVERWORLD_LOGS, VANILLA_NETHER_STEMS);

    public static final WoodTypeCollection<Block> VANILLA_OVERWORLD_STRIPPED_LOGS = new WoodTypeCollection<>(ImmutableMap.of(
            WoodType.OAK, Blocks.STRIPPED_OAK_LOG,
            WoodType.SPRUCE, Blocks.STRIPPED_SPRUCE_LOG,
            WoodType.BIRCH, Blocks.STRIPPED_BIRCH_LOG,
            WoodType.JUNGLE, Blocks.STRIPPED_JUNGLE_LOG,
            WoodType.ACACIA, Blocks.STRIPPED_ACACIA_LOG,
            WoodType.DARK_OAK, Blocks.STRIPPED_DARK_OAK_LOG,
            WoodType.MANGROVE, Blocks.STRIPPED_MANGROVE_LOG,
            WoodType.CHERRY, Blocks.STRIPPED_CHERRY_LOG,
            WoodType.PALE_OAK, Blocks.STRIPPED_PALE_OAK_LOG,
            WoodType.POPLAR, Blocks.STRIPPED_POPLAR_LOG
    ));

    public static final WoodTypeCollection<Block> VANILLA_NETHER_STRIPPED_STEMS = new WoodTypeCollection<>(ImmutableMap.of(
            WoodType.CRIMSON, Blocks.STRIPPED_CRIMSON_STEM,
            WoodType.WARPED, Blocks.STRIPPED_WARPED_STEM
    ));
    public static final WoodTypeCollection<Block> VANILLA_STRIPPED_LOGS = WoodTypeCollection.combine(VANILLA_OVERWORLD_STRIPPED_LOGS, VANILLA_NETHER_STRIPPED_STEMS);

    public static final WoodTypeCollection<Block> VANILLA_OVERWORLD_WOODS = new WoodTypeCollection<>(ImmutableMap.of(
            WoodType.OAK, Blocks.OAK_WOOD,
            WoodType.SPRUCE, Blocks.SPRUCE_WOOD,
            WoodType.BIRCH, Blocks.BIRCH_WOOD,
            WoodType.JUNGLE, Blocks.JUNGLE_WOOD,
            WoodType.ACACIA, Blocks.ACACIA_WOOD,
            WoodType.DARK_OAK, Blocks.DARK_OAK_WOOD,
            WoodType.MANGROVE, Blocks.MANGROVE_WOOD,
            WoodType.CHERRY, Blocks.CHERRY_WOOD,
            WoodType.PALE_OAK, Blocks.PALE_OAK_WOOD,
            WoodType.POPLAR, Blocks.POPLAR_WOOD
    ));

    public static final WoodTypeCollection<Block> VANILLA_NETHER_HYPHAES = new WoodTypeCollection<>(ImmutableMap.of(
            WoodType.CRIMSON, Blocks.CRIMSON_HYPHAE,
            WoodType.WARPED, Blocks.WARPED_HYPHAE
    ));

    public static final WoodTypeCollection<Block> VANILLA_WOODS = WoodTypeCollection.combine(VANILLA_OVERWORLD_WOODS, VANILLA_NETHER_HYPHAES);

    public static final WoodTypeCollection<Block> VANILLA_OVERWORLD_STRIPPED_WOODS = new WoodTypeCollection<>(ImmutableMap.of(
            WoodType.OAK, Blocks.STRIPPED_OAK_WOOD,
            WoodType.SPRUCE, Blocks.STRIPPED_SPRUCE_WOOD,
            WoodType.BIRCH, Blocks.STRIPPED_BIRCH_WOOD,
            WoodType.JUNGLE, Blocks.STRIPPED_JUNGLE_WOOD,
            WoodType.ACACIA, Blocks.STRIPPED_ACACIA_WOOD,
            WoodType.DARK_OAK, Blocks.STRIPPED_DARK_OAK_WOOD,
            WoodType.MANGROVE, Blocks.STRIPPED_MANGROVE_WOOD,
            WoodType.CHERRY, Blocks.STRIPPED_CHERRY_WOOD,
            WoodType.PALE_OAK, Blocks.STRIPPED_PALE_OAK_WOOD,
            WoodType.POPLAR, Blocks.STRIPPED_POPLAR_WOOD
    ));

    public static final WoodTypeCollection<Block> VANILLA_NETHER_STRIPPED_HYPHAES = new WoodTypeCollection<>(ImmutableMap.of(
            WoodType.CRIMSON, Blocks.STRIPPED_CRIMSON_HYPHAE,
            WoodType.WARPED, Blocks.STRIPPED_WARPED_HYPHAE
    ));
    public static final WoodTypeCollection<Block> VANILLA_STRIPPED_WOODS = WoodTypeCollection.combine(VANILLA_OVERWORLD_STRIPPED_WOODS, VANILLA_NETHER_STRIPPED_HYPHAES);

    public void forEach(Consumer<T> consumer) {
        map.values().forEach(consumer);
    }

    public void forEach(BiConsumer<WoodType, T> consumer) {
        map.forEach(consumer);
    }

    public Optional<T> pick(WoodType woodType) {
        return Optional.of(map.get(woodType));
    }

    public <U> WoodTypeCollection<U> map(BiFunction<WoodType, T, U> mapper) {
        var builder = ImmutableMap.<WoodType, U>builder();
        for (var entry : map.entrySet()) {
            builder.put(entry.getKey(), mapper.apply(entry.getKey(), entry.getValue()));
        }
        return new WoodTypeCollection<>(builder.build());
    }

    public static <R> WoodTypeCollection<R> create(List<WoodType> keys, Function<WoodType, R> producer) {
        var builder = ImmutableMap.<WoodType, R>builder();
        for (var id : keys) {
            builder.put(id, producer.apply(id));
        }
        return new WoodTypeCollection<>(builder.build());
    }

    public static <T> WoodTypeCollection<T> combine(WoodTypeCollection<T> first, WoodTypeCollection<T> second) {
        return new WoodTypeCollection<>(ImmutableMap.<WoodType, T>builder().putAll(first.map).putAll(second.map).buildOrThrow());
    }

    public static <T, U> void zipCommonApply(WoodTypeCollection<T> first, WoodTypeCollection<U> second, TriConsumer<WoodType, T, U> consumer) {
        for (var key : Sets.intersection(first.map.keySet(), second.map.keySet())) {
            consumer.accept(key, first.map.get(key), second.map.get(key));
        }
    }

    public static <T, U, R> WoodTypeCollection<R> zipCommonMap(WoodTypeCollection<T> first, WoodTypeCollection<U> second, TriFunction<WoodType, T, U, R> operation) {
        var builder = ImmutableMap.<WoodType, R>builder();
        for (var key : Sets.intersection(first.map.keySet(), second.map.keySet())) {
            builder.put(key, operation.apply(key, first.map.get(key), second.map.get(key)));
        }
        return new WoodTypeCollection<>(builder.build());
    }
}
