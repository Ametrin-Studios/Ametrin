package com.ametrinstudios.ametrin.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.apache.commons.lang3.function.TriFunction;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

// backported from 26.3
public record ColorCollection<T>(
        T white, T orange, T magenta, T lightBlue, T yellow, T lime, T pink, T gray, T lightGray, T cyan, T purple, T blue, T brown, T green, T red, T black
) {
    public static final ColorCollection<DyeColor> VALUES = new ColorCollection<>(
            DyeColor.WHITE,
            DyeColor.ORANGE,
            DyeColor.MAGENTA,
            DyeColor.LIGHT_BLUE,
            DyeColor.YELLOW,
            DyeColor.LIME,
            DyeColor.PINK,
            DyeColor.GRAY,
            DyeColor.LIGHT_GRAY,
            DyeColor.CYAN,
            DyeColor.PURPLE,
            DyeColor.BLUE,
            DyeColor.BROWN,
            DyeColor.GREEN,
            DyeColor.RED,
            DyeColor.BLACK
    );
    public static final ColorCollection<String> NAMES = VALUES.map(DyeColor::getName);

    public static final ColorCollection<Item> DYE = new ColorCollection<>(
            Items.WHITE_DYE,
            Items.ORANGE_DYE,
            Items.MAGENTA_DYE,
            Items.LIGHT_BLUE_DYE,
            Items.YELLOW_DYE,
            Items.LIME_DYE,
            Items.PINK_DYE,
            Items.GRAY_DYE,
            Items.LIGHT_GRAY_DYE,
            Items.CYAN_DYE,
            Items.PURPLE_DYE,
            Items.BLUE_DYE,
            Items.BROWN_DYE,
            Items.GREEN_DYE,
            Items.RED_DYE,
            Items.BLACK_DYE
    );

    public static final ColorCollection<Block> WOOL = new ColorCollection<>(
            Blocks.WHITE_WOOL,
            Blocks.ORANGE_WOOL,
            Blocks.MAGENTA_WOOL,
            Blocks.LIGHT_BLUE_WOOL,
            Blocks.YELLOW_WOOL,
            Blocks.LIME_WOOL,
            Blocks.PINK_WOOL,
            Blocks.GRAY_WOOL,
            Blocks.LIGHT_GRAY_WOOL,
            Blocks.CYAN_WOOL,
            Blocks.PURPLE_WOOL,
            Blocks.BLUE_WOOL,
            Blocks.BROWN_WOOL,
            Blocks.GREEN_WOOL,
            Blocks.RED_WOOL,
            Blocks.BLACK_WOOL
    );

    public static final ColorCollection<Block> CONCRETE = new ColorCollection<>(
            Blocks.WHITE_CONCRETE,
            Blocks.ORANGE_CONCRETE,
            Blocks.MAGENTA_CONCRETE,
            Blocks.LIGHT_BLUE_CONCRETE,
            Blocks.YELLOW_CONCRETE,
            Blocks.LIME_CONCRETE,
            Blocks.PINK_CONCRETE,
            Blocks.GRAY_CONCRETE,
            Blocks.LIGHT_GRAY_CONCRETE,
            Blocks.CYAN_CONCRETE,
            Blocks.PURPLE_CONCRETE,
            Blocks.BLUE_CONCRETE,
            Blocks.BROWN_CONCRETE,
            Blocks.GREEN_CONCRETE,
            Blocks.RED_CONCRETE,
            Blocks.BLACK_CONCRETE
    );

    public static final ColorCollection<Block> DYED_TERRACOTTA = new ColorCollection<>(
            Blocks.WHITE_TERRACOTTA,
            Blocks.ORANGE_TERRACOTTA,
            Blocks.MAGENTA_TERRACOTTA,
            Blocks.LIGHT_BLUE_TERRACOTTA,
            Blocks.YELLOW_TERRACOTTA,
            Blocks.LIME_TERRACOTTA,
            Blocks.PINK_TERRACOTTA,
            Blocks.GRAY_TERRACOTTA,
            Blocks.LIGHT_GRAY_TERRACOTTA,
            Blocks.CYAN_TERRACOTTA,
            Blocks.PURPLE_TERRACOTTA,
            Blocks.BLUE_TERRACOTTA,
            Blocks.BROWN_TERRACOTTA,
            Blocks.GREEN_TERRACOTTA,
            Blocks.RED_TERRACOTTA,
            Blocks.BLACK_TERRACOTTA
    );

    public static final ColorCollection<Block> GLAZED_TERRACOTTA = new ColorCollection<>(
            Blocks.WHITE_GLAZED_TERRACOTTA,
            Blocks.ORANGE_GLAZED_TERRACOTTA,
            Blocks.MAGENTA_GLAZED_TERRACOTTA,
            Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA,
            Blocks.YELLOW_GLAZED_TERRACOTTA,
            Blocks.LIME_GLAZED_TERRACOTTA,
            Blocks.PINK_GLAZED_TERRACOTTA,
            Blocks.GRAY_GLAZED_TERRACOTTA,
            Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA,
            Blocks.CYAN_GLAZED_TERRACOTTA,
            Blocks.PURPLE_GLAZED_TERRACOTTA,
            Blocks.BLUE_GLAZED_TERRACOTTA,
            Blocks.BROWN_GLAZED_TERRACOTTA,
            Blocks.GREEN_GLAZED_TERRACOTTA,
            Blocks.RED_GLAZED_TERRACOTTA,
            Blocks.BLACK_GLAZED_TERRACOTTA
    );

    public static <T> ColorCollection<T> create(T value) {
        return new ColorCollection<>(value, value, value, value, value, value, value, value, value, value, value, value, value, value, value, value);
    }

    public static <B extends Block, Id> ColorCollection<Block> registerBlocks(
            ColorCollection<Id> ids,
            TriFunction<Id, Function<BlockBehaviour.Properties, Block>, BlockBehaviour.Properties, Block> register,
            BiFunction<DyeColor, BlockBehaviour.Properties, B> colorBlockFactory,
            Function<DyeColor, BlockBehaviour.Properties> propertiesSupplier
    ) {
        return zipMap(VALUES, ids, (color, id) -> register.apply(id, p -> colorBlockFactory.apply(color, p), propertiesSupplier.apply(color)));
    }

    public static <Id> ColorCollection<Item> registerBlockItems(
            ColorCollection<Id> ids, ColorCollection<Block> blocks, TriFunction<Id, Block, DyeColor, Item> itemFactory
    ) {
        return zipMap(VALUES, ids, (color, id) -> itemFactory.apply(id, blocks.pick(color), color));
    }

    public static <Id> ColorCollection<Item> registerItems(ColorCollection<Id> ids, BiFunction<Id, DyeColor, Item> itemFactory) {
        return zipMap(VALUES, ids, (color, id) -> itemFactory.apply(id, color));
    }

    public static ColorCollection<String> prefixWithColor(ColorCollection<String> ids) {
        return zipMap(NAMES, ids, (color, id) -> color + "_" + id);
    }

    public List<T> asList() {
        Builder<T> builder = ImmutableList.builderWithExpectedSize(16);
        this.forEach(builder::add);
        return builder.build();
    }

    public void forEach(Consumer<T> consumer) {
        consumer.accept(this.white);
        consumer.accept(this.orange);
        consumer.accept(this.magenta);
        consumer.accept(this.lightBlue);
        consumer.accept(this.yellow);
        consumer.accept(this.lime);
        consumer.accept(this.pink);
        consumer.accept(this.gray);
        consumer.accept(this.lightGray);
        consumer.accept(this.cyan);
        consumer.accept(this.purple);
        consumer.accept(this.blue);
        consumer.accept(this.brown);
        consumer.accept(this.green);
        consumer.accept(this.red);
        consumer.accept(this.black);
    }

    public T pick(DyeColor dyeColor) {
        return (T)(switch (dyeColor) {
            case WHITE -> this.white;
            case ORANGE -> this.orange;
            case MAGENTA -> this.magenta;
            case LIGHT_BLUE -> this.lightBlue;
            case YELLOW -> this.yellow;
            case LIME -> this.lime;
            case PINK -> this.pink;
            case GRAY -> this.gray;
            case LIGHT_GRAY -> this.lightGray;
            case CYAN -> this.cyan;
            case PURPLE -> this.purple;
            case BLUE -> this.blue;
            case BROWN -> this.brown;
            case GREEN -> this.green;
            case RED -> this.red;
            case BLACK -> this.black;
        });
    }

    public <U> ColorCollection<U> map(Function<T, U> mapper) {
        return new ColorCollection<>(
                mapper.apply(this.white),
                mapper.apply(this.orange),
                mapper.apply(this.magenta),
                mapper.apply(this.lightBlue),
                mapper.apply(this.yellow),
                mapper.apply(this.lime),
                mapper.apply(this.pink),
                mapper.apply(this.gray),
                mapper.apply(this.lightGray),
                mapper.apply(this.cyan),
                mapper.apply(this.purple),
                mapper.apply(this.blue),
                mapper.apply(this.brown),
                mapper.apply(this.green),
                mapper.apply(this.red),
                mapper.apply(this.black)
        );
    }

    public static <T, U> void zipApply(ColorCollection<T> first, ColorCollection<U> second, BiConsumer<T, U> consumer) {
        consumer.accept(first.white(), second.white());
        consumer.accept(first.orange(), second.orange());
        consumer.accept(first.magenta(), second.magenta());
        consumer.accept(first.lightBlue(), second.lightBlue());
        consumer.accept(first.yellow(), second.yellow());
        consumer.accept(first.lime(), second.lime());
        consumer.accept(first.pink(), second.pink());
        consumer.accept(first.gray(), second.gray());
        consumer.accept(first.lightGray(), second.lightGray());
        consumer.accept(first.cyan(), second.cyan());
        consumer.accept(first.purple(), second.purple());
        consumer.accept(first.blue(), second.blue());
        consumer.accept(first.brown(), second.brown());
        consumer.accept(first.green(), second.green());
        consumer.accept(first.red(), second.red());
        consumer.accept(first.black(), second.black());
    }

    public static <T, U, R> ColorCollection<R> zipMap(ColorCollection<T> first, ColorCollection<U> second, BiFunction<T, U, R> operation) {
        return new ColorCollection<>(
                operation.apply(first.white(), second.white()),
                operation.apply(first.orange(), second.orange()),
                operation.apply(first.magenta(), second.magenta()),
                operation.apply(first.lightBlue(), second.lightBlue()),
                operation.apply(first.yellow(), second.yellow()),
                operation.apply(first.lime(), second.lime()),
                operation.apply(first.pink(), second.pink()),
                operation.apply(first.gray(), second.gray()),
                operation.apply(first.lightGray(), second.lightGray()),
                operation.apply(first.cyan(), second.cyan()),
                operation.apply(first.purple(), second.purple()),
                operation.apply(first.blue(), second.blue()),
                operation.apply(first.brown(), second.brown()),
                operation.apply(first.green(), second.green()),
                operation.apply(first.red(), second.red()),
                operation.apply(first.black(), second.black())
        );
    }
}

