package com.ametrinstudios.ametrin.world.entity.boat;

import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class BoatVariant<B extends Boat & ICustomBoat> {
    protected static final Map<String, BoatVariant<?>> VARIANTS = new HashMap<>();
    protected static BoatVariant<?> get(String name) {return VARIANTS.get(name);}

    protected final String Name;
    protected final String TextureFolder;
    protected final BiFunction<ModelPart, CustomBoatType, BoatModel> ModelFactory;
    protected final BiFunction<Level, Vec3, B> EntityFactory;

    protected BoatVariant(Builder<B> builder){
        if(VARIANTS.containsKey(builder.Name)) throw new IllegalArgumentException("Duplicate Boat Variant!");

        Name = builder.Name;
        TextureFolder = builder.TextureFolder;
        ModelFactory = builder.ModelFactory;
        EntityFactory = builder.EntityFactory;
        VARIANTS.put(Name, this);
    }

    public String name() {return Name;}
    public String textureFolder() {return TextureFolder;}
    public BoatModel getModel(ModelPart modelPart, CustomBoatType type) {return ModelFactory.apply(modelPart, type);}
    public B getEntity(Level level, Vec3 pos) {return EntityFactory.apply(level, pos);}
    @Override public String toString() {return name();}

    public static class Builder <B extends Boat & ICustomBoat>{
        protected final String Name;
        protected final BiFunction<Level, Vec3, B> EntityFactory;
        protected String TextureFolder;
        protected BiFunction<ModelPart, CustomBoatType, BoatModel> ModelFactory = (modelPart, type) -> new BoatModel(modelPart);


        public Builder(String name, BiFunction<Level, Vec3, B> entityFactory){
            Name = name;
            TextureFolder = name + "_boat/";
            EntityFactory = entityFactory;
        }
        public Builder<B> textureFolder(String folder){
            return textureFolderRaw(folder + "/");
        }
        public Builder<B> textureFolderRaw(String folder){
            TextureFolder = folder;
            return this;
        }
        public Builder<B> modelFactory(Function<ModelPart, BoatModel> factory){
            return modelFactory((modelPart, type) -> factory.apply(modelPart));
        }
        public Builder<B> modelFactory(BiFunction<ModelPart, CustomBoatType, BoatModel> factory){
            ModelFactory = factory;
            return this;
        }
        public BoatVariant<B> register() {return new BoatVariant<>(this);}
    }
}
