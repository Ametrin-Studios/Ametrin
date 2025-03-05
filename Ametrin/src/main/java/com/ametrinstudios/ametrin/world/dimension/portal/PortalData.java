package com.ametrinstudios.ametrin.world.dimension.portal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Optional;
import java.util.function.Supplier;

public record PortalData(ResourceKey<Level> dimensionA, ResourceKey<Level> dimensionB, ResourceKey<PoiType> portalPoi,
                         Supplier<BlockState> portalBlock, Supplier<BlockState> frameBlock, TagKey<Block> validFrames,
                         Optional<SoundEvent> soundEvent, Portal.Transition transition,
                         Optional<ParticleOptions> particles) {
    public Optional<CustomPortalShape> findPortalShape(LevelAccessor level, BlockPos bottomLeft, Direction.Axis axis) {
        return CustomPortalShape.findEmptyPortalShape(this, level, bottomLeft, axis);
    }

    public boolean isValidDimension(Level level) {
        return isValidDimension(level.dimension());
    }

    public boolean isValidDimension(ResourceKey<Level> dimension) {
        return dimension == dimensionA() || dimension == dimensionB();
    }

    public PortalHelper createHelper() {
        return new PortalHelper(this);
    }

    public static Builder builder(ResourceKey<Level> dimensionA, ResourceKey<Level> dimensionB) {
        return new Builder(dimensionA, dimensionB);
    }

    public static class Builder {
        private final ResourceKey<Level> dimensionA;
        private final ResourceKey<Level> dimensionB;
        private ResourceKey<PoiType> portalPoi = null;
        private Supplier<BlockState> portalBlock = null;
        private Supplier<BlockState> frameBlock = null;
        private TagKey<Block> validFrames = null;
        private Optional<SoundEvent> soundEvent = Optional.empty();
        private Portal.Transition transition = Portal.Transition.NONE;
        private Optional<ParticleOptions> particles = Optional.empty();

        public Builder(ResourceKey<Level> dimensionA, ResourceKey<Level> dimensionB) {
            this.dimensionA = dimensionA;
            this.dimensionB = dimensionB;
        }

        public Builder poi(DeferredHolder<PoiType, PoiType> portalPoi) {
            return poi(portalPoi.getKey());
        }

        public Builder poi(ResourceKey<PoiType> portalPoi) {
            this.portalPoi = portalPoi;
            return this;
        }

        public Builder portal(Supplier<BlockState> portal) {
            this.portalBlock = portal;
            return this;
        }

        public Builder defaultFrame(DeferredBlock<? extends Block> frame) {
            return defaultFrame(() -> frame.get().defaultBlockState());
        }

        public Builder defaultFrame(Supplier<BlockState> frame) {
            this.frameBlock = frame;
            return this;
        }

        public Builder validFrames(TagKey<Block> validFrames) {
            this.validFrames = validFrames;
            return this;
        }

        public Builder sounds(SoundEvent soundEvent) {
            this.soundEvent = Optional.of(soundEvent);
            return this;
        }

        public Builder transition(Portal.Transition transition) {
            this.transition = transition;
            return this;
        }

        public Builder particles(ParticleOptions particles) {
            this.particles = Optional.of(particles);
            return this;
        }

        public PortalData build() {
            assertNotNull(portalPoi, "PortalData needs a Point of Interest");
            assertNotNull(portalBlock, "PortalData needs a portal block state");
            assertNotNull(frameBlock, "PortalData needs a default frame block state");
            assertNotNull(validFrames, "PortalData needs a block tag for valid frame blocks");

            return new PortalData(dimensionA, dimensionB, portalPoi, portalBlock, frameBlock, validFrames, soundEvent, transition, particles);
        }

        private void assertNotNull(Object value, String message) {
            if (value == null) {
                throw new NullPointerException(message);
            }
        }
    }
}
