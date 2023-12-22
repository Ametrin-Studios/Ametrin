package com.ametrinstudios.ametrin.world;

import com.ametrinstudios.ametrin.Ametrin;
import com.ametrinstudios.ametrin.util.AmUtil;
import com.ametrinstudios.ametrin.world.entity.boat.CustomBoat;
import com.ametrinstudios.ametrin.world.entity.boat.CustomChestBoat;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

@ApiStatus.Internal
public class AmetrinEntityTypes{
    @ApiStatus.Internal
    public static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(Registries.ENTITY_TYPE, Ametrin.MOD_ID);

    public static Supplier<EntityType<CustomBoat>> BOAT = register("boat", boat(CustomBoat::new));
    public static Supplier<EntityType<CustomChestBoat>> CHEST_BOAT = register("chest_boat", chestBoat(CustomChestBoat::new));

    private static <E extends Entity, EF extends EntityType.EntityFactory<E>> EntityType.Builder<E> boat(EF entity) {
        return entity(entity, MobCategory.MISC, 1.375f, 0.5625f).clientTrackingRange(10);
    }
    private static <E extends Entity, EF extends EntityType.EntityFactory<E>> EntityType.Builder<E> chestBoat(EF entity) {
        return entity(entity, MobCategory.MISC, 1.375f, 0.5625f).clientTrackingRange(10);
    }
    private static <E extends Entity, EF extends EntityType.EntityFactory<E>> EntityType.Builder<E> entity(EF entity, MobCategory category, float width, float height){
        return EntityType.Builder.of(entity, category).sized(width, height);
    }

    private static <E extends Entity> DeferredHolder<EntityType<?>, EntityType<E>> register(String id, EntityType.Builder<E> builder) {return REGISTER.register(id, ()-> builder.build(AmUtil.location(id).toString()));}
}
