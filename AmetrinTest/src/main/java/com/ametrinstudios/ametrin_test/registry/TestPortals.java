package com.ametrinstudios.ametrin_test.registry;

import com.ametrinstudios.ametrin.world.dimension.portal.PortalData;
import net.minecraft.world.level.Level;

public final class TestPortals {
    public static final PortalData TEST_PORTAL =
            PortalData.builder(Level.NETHER, Level.END)
            .poi(TestPoiTypes.TEST_PORTAL)
            .portal(TestBlocks.TEST_PORTAL)
            .defaultFrame(TestBlocks.TEST_BLOCK)
            .validFrames(TestTags.Blocks.TEST_PORTAL_FRAMES)
            .build();
}
