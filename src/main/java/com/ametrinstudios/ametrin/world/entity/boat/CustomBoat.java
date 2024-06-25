package com.ametrinstudios.ametrin.world.entity.boat;

import com.ametrinstudios.ametrin.world.AmetrinEntityTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class CustomBoat extends Boat implements ICustomBoat {
    private static final EntityDataAccessor<String> DATA_ID_TYPE = SynchedEntityData.defineId(CustomBoat.class, EntityDataSerializers.STRING);

    public CustomBoat(EntityType<CustomBoat> entityType, Level level) {
        super(entityType, level);
    }
    public CustomBoat(Level level, double x, double y, double z) {
        this(AmetrinEntityTypes.BOAT.get(), level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }
    public CustomBoat(Level level, Vec3 pos) {
        this(level, pos.x, pos.y, pos.z);
    }

    @Override
    public CustomBoatType getBoatType() {
        var typeString = entityData.get(DATA_ID_TYPE);
        if(typeString.equals(TYPE_DEFAULT)) throw new IllegalStateException("invalid boat type");
        return CustomBoatType.get(ResourceLocation.parse(typeString));
    }
    @Override
    public void setBoatType(@NotNull CustomBoatType type) {entityData.set(DATA_ID_TYPE, type.serialize());}

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_TYPE, TYPE_DEFAULT);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        if(!compoundTag.contains(TYPE_ID)) return;
        setBoatType(CustomBoatType.get(ResourceLocation.parse(compoundTag.getString(TYPE_ID))));
    }
    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        compoundTag.putString(TYPE_ID, entityData.get(DATA_ID_TYPE));
    }

    @Override
    public @NotNull Item getDropItem() {return getBoatType().item(BoatVariants.DEFAULT);}
    @Override
    protected @NotNull Component getTypeName() {return EntityType.BOAT.getDescription();}
}
