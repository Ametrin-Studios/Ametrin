package com.ametrinstudios.ametrin.world.block;

import com.ametrinstudios.ametrin.util.VanillaCompat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * use {@link VanillaCompat#addFlammable(Block, int, int)}
 */
@Deprecated(forRemoval = true)
public class FlammableBlock extends Block {
    protected final int flammability;
    protected final int fireSpreadSpeed;

    public FlammableBlock(int flammability, int fireSpreadSpeed, Properties properties) {
        super(properties);
        this.flammability = flammability;
        this.fireSpreadSpeed = fireSpreadSpeed;
    }

    @Override public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {return true;}
    @Override public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {return fireSpreadSpeed;}
    @Override public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {return fireSpreadSpeed;}
}