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
    private static final Map<String, BoatVariant<?>> VARIANTS = new HashMap<>();
    private static BoatVariant<?> get(String name) {return VARIANTS.get(name);}

    private final String Name;
    private final String TextureFolder;
    private final BiFunction<ModelPart, CustomBoatType, BoatModel> ModelFactory;
    private final BiFunction<Level, Vec3, B> EntityFactory;

    private BoatVariant(String name, String textureFolder, BiFunction<ModelPart, CustomBoatType, BoatModel> modelFactory, BiFunction<Level, Vec3, B> entityFactory){
        if(VARIANTS.containsKey(name)) throw new IllegalArgumentException("Duplicate Boat Variant!");

        Name = name;
        TextureFolder = textureFolder;
        ModelFactory = modelFactory;
        EntityFactory = entityFactory;
        VARIANTS.put(Name, this);
    }

    public String name() {return Name;}
    public String textureFolder() {return TextureFolder;}
    public BoatModel getModel(ModelPart modelPart, CustomBoatType type) {return ModelFactory.apply(modelPart, type);}
    public B getEntity(Level level, Vec3 pos) {return EntityFactory.apply(level, pos);}
    @Override public String toString() {return name();}

    public static class Builder <B extends Boat & ICustomBoat>{
        private final String Name;
        private final BiFunction<Level, Vec3, B> EntityFactory;
        private String TextureFolder;
        BiFunction<ModelPart, CustomBoatType, BoatModel> ModelFactory = (modelPart, type) -> new BoatModel(modelPart);


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
        public BoatVariant<B> register() {
            return new BoatVariant<>(Name, TextureFolder, ModelFactory, EntityFactory);
        }
    }
}
