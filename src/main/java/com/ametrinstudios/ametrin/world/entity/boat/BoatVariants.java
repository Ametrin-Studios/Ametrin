package com.ametrinstudios.ametrin.world.entity.boat;

import net.minecraft.client.model.ChestBoatModel;

public class BoatVariants {
    public static BoatVariant<CustomBoat> DEFAULT = new BoatVariant.Builder<>("default", CustomBoat::new).textureFolder("boat").register();
    public static BoatVariant<CustomChestBoat> CHEST = new BoatVariant.Builder<>("chest", CustomChestBoat::new).modelFactory(ChestBoatModel::new).register();
}