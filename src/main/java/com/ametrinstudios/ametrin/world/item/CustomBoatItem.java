package com.ametrinstudios.ametrin.world.item;

import com.ametrinstudios.ametrin.world.entity.boat.BoatVariant;
import com.ametrinstudios.ametrin.world.entity.boat.BoatVariants;
import com.ametrinstudios.ametrin.world.entity.boat.CustomBoatType;
import com.ametrinstudios.ametrin.world.entity.boat.ICustomBoat;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class CustomBoatItem extends Item {
    protected static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);

    public final CustomBoatType Type;
    public final BoatVariant<?> Variant;
    public CustomBoatItem(CustomBoatType type, BoatVariant<?> variant, Properties properties) {
        super(properties);
        Type = type;
        Variant = variant;
    }
    public CustomBoatItem(CustomBoatType type, BoatVariant<?> variant){
        this(type, variant, new Properties().stacksTo(1));
    }

    public static CustomBoatItem chest(CustomBoatType type){
        return new CustomBoatItem(type, BoatVariants.CHEST);
    }
    public static CustomBoatItem boat(CustomBoatType type){
        return new CustomBoatItem(type, BoatVariants.DEFAULT);
    }

    @Override @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        var stack = player.getItemInHand(hand);
        var hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);

        if(hitResult.getType() == HitResult.Type.MISS) return InteractionResultHolder.pass(stack);

        var viewVector = player.getViewVector(1);
        var entities = level.getEntities(player, player.getBoundingBox().expandTowards(viewVector.scale(5)).inflate(1), ENTITY_PREDICATE);

        if (!entities.isEmpty()) {
            var eyePosition = player.getEyePosition();

            for(Entity entity : entities) {
                var inflatedBoundingBox = entity.getBoundingBox().inflate(entity.getPickRadius());
                if (inflatedBoundingBox.contains(eyePosition)) {
                    return InteractionResultHolder.pass(stack);
                }
            }
        }

        if(hitResult.getType() != HitResult.Type.BLOCK) return InteractionResultHolder.pass(stack);

        var boat = getBoat(level, hitResult);
        boat.setYRot(player.getYRot());

        if(!level.noCollision(boat, boat.getBoundingBox())) return InteractionResultHolder.fail(stack);

        if (!level.isClientSide) {
            level.addFreshEntity(boat);
            level.gameEvent(player, GameEvent.ENTITY_PLACE, hitResult.getLocation());
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    protected <B extends Boat & ICustomBoat> B getBoat(Level level, @NotNull HitResult hitResult) {
        return (B) ICustomBoat.create(Type, Variant, level, hitResult.getLocation());
    }
}
