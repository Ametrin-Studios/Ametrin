package com.ametrinstudios.ametrin.mixin;

import com.ametrinstudios.ametrin.util.IMixinBlockBehaviorProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Function;
import java.util.function.ToIntFunction;

@Mixin(BlockBehaviour.Properties.class)
public abstract class MixinBlockBehaviorProperties implements IMixinBlockBehaviorProperties {

    @Shadow Material material;
    @Shadow Function<BlockState, MaterialColor> materialColor;
    @Shadow boolean hasCollision;
    @Shadow SoundType soundType;
    @Shadow ToIntFunction<BlockState> lightEmission;
    @Shadow float explosionResistance;
    @Shadow float destroyTime;
    @Shadow boolean requiresCorrectToolForDrops;
    @Shadow boolean isRandomlyTicking;
    @Shadow float friction;
    @Shadow float speedFactor;
    @Shadow float jumpFactor;
    @Shadow ResourceLocation drops;
    @Shadow boolean canOcclude;
    @Shadow boolean isAir;
    @Shadow private java.util.function.Supplier<ResourceLocation> lootTableSupplier;
    @Shadow BlockBehaviour.StateArgumentPredicate<EntityType<?>> isValidSpawn;
    @Shadow BlockBehaviour.StatePredicate isRedstoneConductor;
    @Shadow BlockBehaviour.StatePredicate isSuffocating;
    @Shadow BlockBehaviour.StatePredicate isViewBlocking;
    @Shadow BlockBehaviour.StatePredicate hasPostProcess;
    @Shadow BlockBehaviour.StatePredicate emissiveRendering;
    @Shadow boolean dynamicShape;
    @Shadow Function<BlockState, BlockBehaviour.OffsetType> offsetType;

    public BlockBehaviour.Properties fullCopy(){
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of(material, materialColor)
                .strength(destroyTime, explosionResistance)
                .lightLevel(lightEmission)
                .sound(soundType)
                .friction(friction)
                .speedFactor(speedFactor)
                .jumpFactor(jumpFactor)
                .offsetType(offsetType);

        if(!hasCollision) {properties.noCollission();}
        if(isRandomlyTicking) {properties.randomTicks();}
        if(dynamicShape) {properties.dynamicShape();}
        if(!canOcclude) {properties.noOcclusion();}
        if(isAir) {properties.air();}
        if(requiresCorrectToolForDrops) {properties.requiresCorrectToolForDrops();}

        return properties;
    }

    /*public static BlockBehaviour.Properties copy(BlockBehaviour.Properties fromOriginal) {
        IBlockBehaviorPropertiesMixin from = (IBlockBehaviorPropertiesMixin) fromOriginal;

        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of(from.getMaterial(), from.getMaterialColor());
        properties.strength(from.getDestroyTime(), from.getExplosionResistance());
        if(!from.getHasCollision()) {properties.noCollission();}
        if(from.getIsRandomlyTicking()) {properties.randomTicks();}
        properties.lightLevel(from.getLightEmission());
        properties.sound(from.getSoundType());
        properties.friction(from.getFriction());
        properties.speedFactor(from.getSpeedFactor());
        properties.jumpFactor(from.getJumpFactor());
        if(from.getDrops() == BuiltInLootTables.EMPTY) {properties.noLootTable();}
        if(from.getDynamicShape()) {properties.dynamicShape();}
        if(!from.getCanOcclude()) {properties.noOcclusion();}
        if(from.getIsAir()) {properties.air();}
        if(from.getRequiresCorrectToolForDrops()) {properties.requiresCorrectToolForDrops();}
        properties.offsetType(from.getOffsetType());
        return properties;
    }*/
}