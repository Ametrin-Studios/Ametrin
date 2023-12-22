package com.ametrinstudios.ametrin_test.world;

import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.ametrinstudios.ametrin.world.block.helper.BlockBehaviourPropertiesHelper.CopyProperties;

public class TestBlocks {
    public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(AmetrinTestMod.MOD_ID);

    private static final BlockBehaviour.Properties DEFAULT_PROPERTIES = CopyProperties(Blocks.STONE);
    public static final Supplier<Block> TEST_BLOCK = REGISTRY.register("test_block", ()-> new Block(CopyProperties(DEFAULT_PROPERTIES).friction(0.1f)));
}