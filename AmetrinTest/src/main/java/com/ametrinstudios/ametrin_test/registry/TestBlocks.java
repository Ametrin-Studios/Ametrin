package com.ametrinstudios.ametrin_test.registry;

import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import com.ametrinstudios.ametrin_test.world.block.TestPortalBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.ametrinstudios.ametrin.world.block.helper.BlockBehaviourPropertiesHelper.copyProperties;

public final class TestBlocks {
    public static final DeferredRegister.Blocks REGISTER = DeferredRegister.createBlocks(AmetrinTestMod.MOD_ID);

    private static final BlockBehaviour.Properties DEFAULT_PROPERTIES = copyProperties(Blocks.STONE);

    public static final DeferredBlock<Block> TEST_BLOCK = REGISTER.register("test_block", ()-> new Block(copyProperties(DEFAULT_PROPERTIES).friction(0.1f)));
    public static final DeferredBlock<Block> TEST_LOG = registerWithItem("test_log", ()-> new RotatedPillarBlock(copyProperties(Blocks.OAK_LOG)));
    public static final DeferredBlock<Block> TEST_PORTAL = REGISTER.register("test_portal", TestPortalBlock::new);

    private static <T extends Block> DeferredBlock<T> registerWithItem(String name, Supplier<T> block) {
        return registerWithItem(name, block, new Item.Properties());
    }

    private static <T extends Block> DeferredBlock<T> registerWithItem(String name, Supplier<T> block, Item.Properties itemProperties) {
        var registryObject = REGISTER.register(name, block);
        TestItems.REGISTER.register(name, ()-> new BlockItem(registryObject.get(), itemProperties));
        return registryObject;
    }
}