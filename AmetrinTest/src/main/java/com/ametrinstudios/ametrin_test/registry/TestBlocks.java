package com.ametrinstudios.ametrin_test.registry;

import com.ametrinstudios.ametrin.world.block.CustomHeadBlock;
import com.ametrinstudios.ametrin.world.block.CustomWallHeadBlock;
import com.ametrinstudios.ametrin.world.block.PortalBlock;
import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

import static com.ametrinstudios.ametrin.world.block.helper.BlockBehaviourPropertiesHelper.copyProperties;
import static com.ametrinstudios.ametrin.world.block.helper.BlockRegisterHelper.portalBlock;

public final class TestBlocks {
    public static final DeferredRegister.Blocks REGISTER = DeferredRegister.createBlocks(AmetrinTestMod.MOD_ID);

    private static final BlockBehaviour.Properties DEFAULT_PROPERTIES = copyProperties(Blocks.STONE);

    public static final DeferredBlock<Block> TEST_BLOCK = REGISTER.registerSimpleBlock("test_block", copyProperties(DEFAULT_PROPERTIES).friction(0.9f));
    public static final DeferredBlock<Block> TEST_LOG = registerWithItem("test_log", RotatedPillarBlock::new, copyProperties(Blocks.OAK_LOG));
    public static final DeferredBlock<CustomHeadBlock> TEST_SKULL = REGISTER.registerBlock("test_skull", CustomHeadBlock::new, copyProperties(Blocks.SKELETON_SKULL));
    public static final DeferredBlock<CustomWallHeadBlock> TEST_SKULL_WALL = REGISTER.registerBlock("test_skull_wall", properties-> new CustomWallHeadBlock(TestItems.TEST_SKULL, properties), copyProperties(Blocks.SKELETON_WALL_SKULL));

    public static final DeferredBlock<PortalBlock> TEST_PORTAL = REGISTER.registerBlock("test_portal", portalBlock(TestPortals.TEST_PORTAL, 11), BlockBehaviour.Properties.of());


    private static <T extends Block> DeferredBlock<T> registerWithItem(String name, Function<BlockBehaviour.Properties, T> block, BlockBehaviour.Properties properties) {
        var registryObject = REGISTER.registerBlock(name, block, properties);
        TestItems.REGISTER.registerSimpleBlockItem(registryObject);
        return registryObject;    }

    private static <T extends Block> DeferredBlock<T> registerWithItem(String name, Function<BlockBehaviour.Properties, T> block, BlockBehaviour.Properties properties, Item.Properties itemProperties) {
        var registryObject = REGISTER.registerBlock(name, block, properties);
        TestItems.REGISTER.registerSimpleBlockItem(registryObject, itemProperties);
        return registryObject;
    }
}