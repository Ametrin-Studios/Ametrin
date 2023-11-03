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
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class CustomChestBoat extends ChestBoat implements ICustomBoat {
    private static final  EntityDataAccessor<String> DATA_ID_TYPE = SynchedEntityData.defineId(CustomChestBoat.class, EntityDataSerializers.STRING);

    public CustomChestBoat(EntityType<? extends Boat> entityType, Level level) {
        super(entityType, level);
    }
    public CustomChestBoat(Level level, double x, double y, double z) {
        this(AmetrinEntityTypes.CHEST_BOAT.get(), level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }
    public CustomChestBoat(Level level, Vec3 pos){
        this(level, pos.x, pos.y, pos.z);
    }

    @Override
    public CustomBoatType getBoatType() {
        var typeString = entityData.get(DATA_ID_TYPE);
        if(typeString.equals(TYPE_DEFAULT)) throw new IllegalStateException("invalid boat type");
        return CustomBoatType.get(new ResourceLocation(typeString));
    }
    @Override
    public void setBoatType(@NotNull CustomBoatType type) {entityData.set(DATA_ID_TYPE, type.serialize());}

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_ID_TYPE, TYPE_DEFAULT);
    }
    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        if(!compoundTag.contains(TYPE_ID)) return;
        setBoatType(CustomBoatType.get(new ResourceLocation(compoundTag.getString(TYPE_ID))));
    }
    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        compoundTag.putString(TYPE_ID, entityData.get(DATA_ID_TYPE));
    }

    @Override
    public @NotNull Item getDropItem() {return getBoatType().item(BoatVariants.CHEST);}
    @Override
    protected @NotNull Component getTypeName() {return EntityType.CHEST_BOAT.getDescription();}
}
