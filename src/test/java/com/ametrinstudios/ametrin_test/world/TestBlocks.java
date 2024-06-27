package com.ametrinstudios.ametrin_test.world;

import com.ametrinstudios.ametrin_test.AmetrinTestMod;
import com.ametrinstudios.ametrin_test.world.block.TestPortalBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.ametrinstudios.ametrin.world.block.helper.BlockBehaviourPropertiesHelper.CopyProperties;

public final class TestBlocks {
    public static final DeferredRegister.Blocks REGISTER = DeferredRegister.createBlocks(AmetrinTestMod.MOD_ID);

    private static final BlockBehaviour.Properties DEFAULT_PROPERTIES = CopyProperties(Blocks.STONE);
    public static final Supplier<Block> TEST_BLOCK = REGISTER.register("test_block", ()-> new Block(CopyProperties(DEFAULT_PROPERTIES).friction(0.1f)));
    public static final Supplier<Block> TEST_PORTAL = REGISTER.register("test_portal", TestPortalBlock::new);

    private static <T extends Block>Supplier<T> register(String name, Supplier<T> block, Item.Properties itemProperties) {
        var registryObject = registerWithoutItem(name, block);
        TestItems.REGISTER.register(name, ()-> new BlockItem(registryObject.get(), itemProperties));
        return registryObject;
    }
    private static <T extends Block> Supplier<T> registerWithoutItem(String name, Supplier<T> block) { return REGISTER.register(name, block); }

}