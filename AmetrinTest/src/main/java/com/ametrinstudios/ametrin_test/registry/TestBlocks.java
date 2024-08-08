package com.ametrinstudios.ametrin_test.registry;

import com.ametrinstudios.ametrin.world.block.CustomHeadBlock;
import com.ametrinstudios.ametrin.world.block.CustomWallHeadBlock;
import com.ametrinstudios.ametrin.world.block.PortalBlock;
import com.ametrinstudios.ametrin_test.AmetrinTestMod;
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

    public static final DeferredBlock<Block> TEST_BLOCK = REGISTER.register("test_block", ()-> new Block(copyProperties(DEFAULT_PROPERTIES).friction(0.9f)));
    public static final DeferredBlock<Block> TEST_LOG = registerWithItem("test_log", ()-> new RotatedPillarBlock(copyProperties(Blocks.OAK_LOG)));
    public static final DeferredBlock<CustomHeadBlock> TEST_SKULL = REGISTER.register("test_skull", ()-> new CustomHeadBlock(copyProperties(Blocks.SKELETON_SKULL)));
    public static final DeferredBlock<CustomWallHeadBlock> TEST_SKULL_WALL = REGISTER.register("test_skull_wall", ()-> new CustomWallHeadBlock(TestItems.TEST_SKULL, copyProperties(Blocks.SKELETON_WALL_SKULL)));

    public static final DeferredBlock<PortalBlock> TEST_PORTAL = REGISTER.register("test_portal", ()-> new PortalBlock(TestPortals.TEST_PORTAL, 11));


    private static <T extends Block> DeferredBlock<T> registerWithItem(String name, Supplier<T> block) {
        return registerWithItem(name, block, new Item.Properties());
    }

    private static <T extends Block> DeferredBlock<T> registerWithItem(String name, Supplier<T> block, Item.Properties itemProperties) {
        var registryObject = REGISTER.register(name, block);
        TestItems.REGISTER.register(name, ()-> new BlockItem(registryObject.get(), itemProperties));
        return registryObject;
    }
}