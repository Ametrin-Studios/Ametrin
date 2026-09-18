package com.ametrinstudios.ametrin.mixin;

import com.ametrinstudios.ametrin.util.mixin.IMixinBlockBehaviorProperties;
import net.minecraft.resources.DependantName;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
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
    @Shadow float bounceRestitution;
    @Shadow DependantName<Block, Optional<ResourceKey<LootTable>>> drops;
    @Shadow DependantName<Block, String> descriptionId;
    @Shadow boolean canOcclude;
    @Shadow boolean isAir;
    @Shadow boolean ignitedByLava;
    @Shadow boolean liquid;
    @Shadow boolean forceSolidOff;
    @Shadow boolean forceSolidOn;
    @Shadow PushReaction pushReaction;
    @Shadow boolean spawnTerrainParticles;
    @Shadow NoteBlockInstrument instrument;
    @Shadow boolean replaceable;
    @Shadow BlockBehaviour.StateArgumentPredicate<EntityType<?>> isValidSpawn;
    @Shadow BlockBehaviour.StatePredicate isRedstoneConductor;
    @Shadow BlockBehaviour.StatePredicate isSuffocating;
    @Shadow BlockBehaviour.StateArgumentPredicate<AABB> isViewBlocking;
    @Shadow BlockBehaviour.PostProcess postProcess;
    @Shadow Predicate<BlockState> emissiveRendering;
    @Shadow boolean dynamicShape;
    @Shadow FeatureFlagSet requiredFeatures;
    @Shadow BlockBehaviour.OffsetFunction offsetFunction;
    @Shadow float fallDistanceReduction;

    @SuppressWarnings("deprecation")
    public BlockBehaviour.Properties copy() {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of()
                .mapColor(mapColor)
                .sound(soundType)
                .lightLevel(lightEmission)
                .strength(destroyTime, explosionResistance)
                .friction(friction)
                .speedFactor(speedFactor)
                .jumpFactor(jumpFactor)
                .bounceRestitution(bounceRestitution)
                .fallDistanceReduction(fallDistanceReduction)
                .pushReaction(pushReaction)
                .instrument(instrument)
                .isValidSpawn(isValidSpawn)
                .isRedstoneConductor(isRedstoneConductor)
                .isSuffocating(isSuffocating)
                .isViewBlocking(isViewBlocking)
                .postProcess(postProcess)
                .emissiveRendering(emissiveRendering);

        if(!hasCollision) { properties.noCollision(); }
        if(requiresCorrectToolForDrops) { properties.requiresCorrectToolForDrops(); }
        if(isRandomlyTicking) { properties.randomTicks(); }
        if(!canOcclude) { properties.noOcclusion(); }
        if(isAir) { properties.air(); }
        if(ignitedByLava) { properties.ignitedByLava(); }
        if(liquid) { properties.liquid(); }
        if(forceSolidOn) { properties.forceSolidOn(); }
        if(forceSolidOff) { properties.forceSolidOff(); }
        if(!spawnTerrainParticles) { properties.noTerrainParticles(); }
        if(replaceable) { properties.replaceable(); }
        if(dynamicShape) { properties.dynamicShape(); }

        var mixinProperties = ((IMixinBlockBehaviorProperties) properties);
        mixinProperties.setFeatureFlagSet(requiredFeatures);
        mixinProperties.setOffsetFunction(offsetFunction);
        mixinProperties.overrideDrops(drops);
        mixinProperties.overrideDescriptionId(descriptionId);

        return properties;
    }

    @Override
    public void setOffsetFunction(BlockBehaviour.OffsetFunction func) {
        offsetFunction = func;
    }

    @Override
    public void setFeatureFlagSet(FeatureFlagSet featureFlags) {
        requiredFeatures = featureFlags;
    }

    @Override
    public void overrideDrops(DependantName<Block, Optional<ResourceKey<LootTable>>> drops) {
        this.drops = drops;
    }

    @Override
    public void overrideDescriptionId(DependantName<Block, String> descriptionId) {
        this.descriptionId = descriptionId;
    }
}