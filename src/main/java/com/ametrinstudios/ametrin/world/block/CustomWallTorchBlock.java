package com.ametrinstudios.ametrin.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class CustomWallTorchBlock extends WallTorchBlock {
    protected final Supplier<SimpleParticleType> particle;
    public CustomWallTorchBlock(Properties properties, Supplier<SimpleParticleType> particle) {
        super(properties, ParticleTypes.FLAME);
        this.particle = particle;
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos pos, @NotNull RandomSource random) {
        double x = pos.getX() + 0.5D;
        double y = pos.getY() + 0.7D;
        double z = pos.getZ() + 0.5D;
        Direction direction = blockState.getValue(FACING).getOpposite();
        level.addParticle(ParticleTypes.SMOKE, x + 0.27D * (double)direction.getStepX(), y + 0.22D, z + 0.27D * (double)direction.getStepZ(), 0.0D, 0.0D, 0.0D);
        level.addParticle(particle.get(), x + 0.27D * (double)direction.getStepX(), y + 0.22D, z + 0.27D * (double)direction.getStepZ(), 0.0D, 0.0D, 0.0D);
    }
}