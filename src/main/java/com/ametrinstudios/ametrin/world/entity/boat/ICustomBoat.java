package com.ametrinstudios.ametrin.world.entity.boat;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public interface ICustomBoat {
    static <B extends Boat & ICustomBoat> B create(CustomBoatType type, BoatVariant<B> variant, Level level, Vec3 pos){
        var boat = variant.getEntity(level, pos);
        boat.setBoatType(type);
        return boat;
    }
    EntityDataAccessor<String> DATA_ID_TYPE = SynchedEntityData.defineId(CustomBoat.class, EntityDataSerializers.STRING);
    String TYPE_ID = "Type";

    CustomBoatType getBoatType();
    void setBoatType(CustomBoatType type);
}
