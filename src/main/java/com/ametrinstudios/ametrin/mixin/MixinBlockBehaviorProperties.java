package com.ametrinstudios.ametrin.mixin;

import com.ametrinstudios.ametrin.util.mixin.IMixinBlockBehaviorProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

@Mixin(BlockBehaviour.Properties.class)
public abstract class MixinBlockBehaviorProperties implements IMixinBlockBehaviorProperties {
    @Shadow Function<BlockState, MapColor> mapColor;
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
    @Shadow boolean ignitedByLava;
    @Shadow boolean liquid;
    @Shadow boolean forceSolidOff;
    @Shadow boolean forceSolidOn;
    @Shadow PushReaction pushReaction;
    @Shadow boolean spawnParticlesOnBreak;
    @Shadow NoteBlockInstrument instrument;
    @Shadow boolean replaceable;
    @Shadow Supplier<ResourceLocation> lootTableSupplier;
    @Shadow BlockBehaviour.StateArgumentPredicate<EntityType<?>> isValidSpawn;
    @Shadow BlockBehaviour.StatePredicate isRedstoneConductor;
    @Shadow BlockBehaviour.StatePredicate isSuffocating;
    @Shadow BlockBehaviour.StatePredicate isViewBlocking;
    @Shadow BlockBehaviour.StatePredicate hasPostProcess;
    @Shadow BlockBehaviour.StatePredicate emissiveRendering;
    @Shadow boolean dynamicShape;
    @Shadow FeatureFlagSet requiredFeatures;
    @Shadow Optional<BlockBehaviour.OffsetFunction> offsetFunction;

    public BlockBehaviour.Properties copy(){
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of()
                .mapColor(mapColor)
                .sound(soundType)
                .lightLevel(lightEmission)
                .strength(destroyTime, explosionResistance)
                .friction(friction)
                .speedFactor(speedFactor)
                .jumpFactor(jumpFactor)
                .pushReaction(pushReaction)
                .instrument(instrument)
                .isValidSpawn(isValidSpawn)
                .isRedstoneConductor(isRedstoneConductor)
                .isSuffocating(isSuffocating)
                .hasPostProcess(hasPostProcess)
                .emissiveRendering(emissiveRendering);

        if(!hasCollision) {properties.noCollission();}
        if(requiresCorrectToolForDrops) {properties.requiresCorrectToolForDrops();}
        if(isRandomlyTicking) {properties.randomTicks();}
        if(!canOcclude) {properties.noOcclusion();}
        if(isAir) {properties.air();}
        if(ignitedByLava) {properties.ignitedByLava();}
        if(liquid) {properties.liquid();}
        if(forceSolidOn) {properties.forceSolidOn();}
        if(forceSolidOff) {properties.forceSolidOff();}
        if(!spawnParticlesOnBreak) {properties.noParticlesOnBreak();}
        if(replaceable) {properties.replaceable();}
        if(isViewBlocking != isSuffocating) {properties.isViewBlocking(isViewBlocking);}
        if(dynamicShape) {properties.dynamicShape();}

        var mixinProperties = ((IMixinBlockBehaviorProperties) properties);
        mixinProperties.SetFeatureFlagSet(requiredFeatures);
        offsetFunction.ifPresent(mixinProperties::SetOffsetFunction);

        if(lootTableSupplier != null){
            mixinProperties.SetLootTableSupplier(lootTableSupplier);
        }else if(drops == BuiltInLootTables.EMPTY){
            properties.noLootTable();
        }

        return properties;
    }

    @Override
    public void SetOffsetFunction(BlockBehaviour.OffsetFunction func) {
        offsetFunction = Optional.of(func);
    }

    @Override
    public void SetFeatureFlagSet(FeatureFlagSet featureFlags) {
        requiredFeatures = featureFlags;
    }

    @Override
    public void SetLootTableSupplier(Supplier<ResourceLocation> lootTable) {
        lootTableSupplier = lootTable;
    }
}