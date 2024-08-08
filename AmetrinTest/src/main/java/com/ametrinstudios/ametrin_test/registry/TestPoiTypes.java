package com.ametrinstudios.ametrin_test.registry;

import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;

public final class TestPoiTypes {
    public static final DeferredRegister<PoiType> REGISTER = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, AmetrinTestMod.MOD_ID);

    public static final DeferredHolder<PoiType, PoiType> TEST_PORTAL = REGISTER.register("test_portal", ()-> new PoiType(getBlockStates(TestBlocks.TEST_PORTAL), 0, 1));

    private static Set<BlockState> getBlockStates(DeferredBlock<? extends Block> block) {
        return ImmutableSet.copyOf(block.get().getStateDefinition().getPossibleStates());
    }
}
