package com.ametrinstudios.ametrin.world.entity.boat;

import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CustomBoatType {
    private static final Map<ResourceLocation, CustomBoatType> TYPES = new HashMap<>();
    public static CustomBoatType get(ResourceLocation guid) {return TYPES.get(guid);}
    public static Collection<CustomBoatType> getAll() {return TYPES.values();}

    public static Builder builder(ResourceLocation guid) {return new Builder(guid);}

    private final ResourceLocation GUID;
    private final ImmutableMap<BoatVariant, Supplier<Item>> Items;
    private CustomBoatType(ResourceLocation guid, ImmutableMap<BoatVariant, Supplier<Item>> items) {
        GUID = guid;
        Items = items;
        if(TYPES.containsKey(this.GUID)) throw new IllegalArgumentException("Duplicate Boat Type");
        TYPES.put(this.GUID, this);
    }
    public ResourceLocation guid() {return GUID;}
    public String name() {return GUID.getPath();}
    public String modID() {return GUID.getNamespace();}
    public Item item(BoatVariant variant) {
        return Items.get(variant).get();
    }

    public String serialize() {return guid().toString();}

    public static class Builder{
        private final ResourceLocation GUID;
        private final Map<BoatVariant, Supplier<Item>> Items = new HashMap<>();

        public Builder(ResourceLocation guid){
            GUID = guid;
        }
        public Builder boatItem(Supplier<Item> item){
            return item(BoatVariants.DEFAULT, item);
        }
        public Builder chestBoatItem(Supplier<Item> item){
            return item(BoatVariants.CHEST, item);
        }
        public Builder item(BoatVariant variant, Supplier<Item> item){
            Items.put(variant, item);
            return this;
        }
        public CustomBoatType register(){
            return new CustomBoatType(GUID, ImmutableMap.copyOf(Items));
        }
    }
}
