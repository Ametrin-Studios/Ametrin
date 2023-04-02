package com.ametrinstudios.ametrin.world.entity.boat;

import com.ametrinstudios.ametrin.world.AmetrinEntityTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class CustomBoat extends Boat implements ICustomBoat {
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
    public CustomBoatType getBoatType(){
        return CustomBoatType.get(new ResourceLocation(entityData.get(DATA_ID_TYPE)));
    }
    @Override
    public void setBoatType(CustomBoatType type){
        entityData.set(DATA_ID_TYPE, type.serialize());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_ID_TYPE, "null");
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
    public @NotNull Item getDropItem() {
        return getBoatType().item(BoatVariants.DEFAULT);
    }
}
