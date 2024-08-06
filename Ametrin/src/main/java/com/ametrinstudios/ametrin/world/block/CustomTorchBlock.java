package com.ametrinstudios.ametrin.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class CustomTorchBlock extends TorchBlock {
    protected final Supplier<SimpleParticleType> particle;

    public CustomTorchBlock(Properties properties, Supplier<SimpleParticleType> particle) {
        super(ParticleTypes.FLAME, properties);
        this.particle = particle;
    }

    @Override
    public void animateTick(@NotNull BlockState blockState, Level level, BlockPos pos, @NotNull RandomSource random) {
        double x = (double)pos.getX() + 0.5D;
        double y = (double)pos.getY() + 0.7D;
        double z = (double)pos.getZ() + 0.5D;
        level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
        level.addParticle(particle.get(), x, y, z, 0.0D, 0.0D, 0.0D);
    }
}