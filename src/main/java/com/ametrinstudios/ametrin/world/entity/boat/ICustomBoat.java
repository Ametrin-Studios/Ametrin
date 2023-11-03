package com.ametrinstudios.ametrin.world.entity.boat;

import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public interface ICustomBoat {
    static <B extends Boat & ICustomBoat> B create(CustomBoatType type, BoatVariant<B> variant, Level level, Vec3 pos){
        var boat = variant.getEntity(level, pos);
        boat.setBoatType(type);
        return boat;
    }
    String TYPE_ID = "Type";
    String TYPE_DEFAULT = "null";

    CustomBoatType getBoatType();
    void setBoatType(@NotNull CustomBoatType type);
}
