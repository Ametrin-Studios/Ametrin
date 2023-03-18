package com.ametrinstudios.ametrin.mixin;

import com.ametrinstudios.ametrin.util.mixin.IMixinBlockBehaviorProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;
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
    @Shadow BlockBehaviour.StateArgumentPredicate<EntityType<?>> isValidSpawn;
    @Shadow BlockBehaviour.StatePredicate isRedstoneConductor;
    @Shadow BlockBehaviour.StatePredicate isSuffocating;
    @Shadow BlockBehaviour.StatePredicate isViewBlocking;
    @Shadow BlockBehaviour.StatePredicate hasPostProcess;
    @Shadow BlockBehaviour.StatePredicate emissiveRendering;
    @Shadow boolean dynamicShape;
    @Shadow FeatureFlagSet requiredFeatures;
    @Shadow Optional<BlockBehaviour.OffsetFunction> offsetFunction = Optional.empty();

    public BlockBehaviour.Properties copy(){
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of(material, materialColor)
                .strength(destroyTime, explosionResistance)
                .lightLevel(lightEmission)
                .sound(soundType)
                .friction(friction)
                .speedFactor(speedFactor)
                .jumpFactor(jumpFactor)
                .isValidSpawn(isValidSpawn)
                .isRedstoneConductor(isRedstoneConductor)
                .isSuffocating(isSuffocating)
                .hasPostProcess(hasPostProcess)
                .emissiveRendering(emissiveRendering);

        if(!hasCollision) {properties.noCollission();}
        if(isRandomlyTicking) {properties.randomTicks();}
        if(dynamicShape) {properties.dynamicShape();}
        if(!canOcclude) {properties.noOcclusion();}
        if(isAir) {properties.air();}
        if(requiresCorrectToolForDrops) {properties.requiresCorrectToolForDrops();}
        if(drops == BuiltInLootTables.EMPTY) {properties.noLootTable();}
        if(isViewBlocking != isSuffocating) {properties.isViewBlocking(isViewBlocking);}

        return properties;
    }
}