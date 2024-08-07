package com.ametrinstudios.ametrin_test.world.block;

import com.ametrinstudios.ametrin.world.block.PortalBlock;
import com.ametrinstudios.ametrin.world.dimension.portal.PortalHelper;
import com.ametrinstudios.ametrin_test.registry.TestBlocks;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public final class TestPortalBlock extends PortalBlock {

    public TestPortalBlock() {
        super(11, new PortalHelper(()-> TestBlocks.TEST_PORTAL.get().defaultBlockState(), ()-> TestBlocks.TEST_BLOCK.get().defaultBlockState()));
    }

    @Override
    protected ParticleOptions particle() {
        return ParticleTypes.PORTAL;
    }

    @Override
    protected ResourceKey<Level> targetLevel() {
        return Level.NETHER;
    }
}
