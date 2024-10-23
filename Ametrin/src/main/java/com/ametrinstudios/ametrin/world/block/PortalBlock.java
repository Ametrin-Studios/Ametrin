package com.ametrinstudios.ametrin.world.block;

import com.ametrinstudios.ametrin.world.dimension.portal.PortalData;
import com.ametrinstudios.ametrin.world.dimension.portal.PortalHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class PortalBlock extends Block implements Portal {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    protected static final VoxelShape X_AXIS_AABB = Block.box(0.0, 0.0, 6.0, 16.0, 16.0, 10.0);
    protected static final VoxelShape Z_AXIS_AABB = Block.box(6.0, 0.0, 0.0, 10.0, 16.0, 16.0);
    protected final PortalData data;
    protected final PortalHelper helper;

    public PortalBlock(PortalData data, Properties properties) {
        super(properties);
        this.data = data;
        this.helper = data.createHelper();
        registerDefaultState(stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

    @Override @ParametersAreNonnullByDefault
    protected @NotNull BlockState updateShape(BlockState thisState, LevelReader level, ScheduledTickAccess tickAccess, BlockPos thisPos, Direction facing, BlockPos facingPos, BlockState facingState, RandomSource random) {
        var facingAxis = facing.getAxis();
        var thisAxis = thisState.getValue(AXIS);
        boolean flag = thisAxis != facingAxis && facingAxis.isHorizontal();
        return !flag && !facingState.is(this) && !PortalShape.findAnyShape(level, thisPos, thisAxis).isComplete()
                ? Blocks.AIR.defaultBlockState()
                : super.updateShape(thisState, level, tickAccess, thisPos, facing, facingPos, facingState, random);
    }

    @Override @ParametersAreNonnullByDefault
    protected void entityInside(BlockState blockState, Level level, BlockPos pos, Entity entity) {
        if (entity.canUsePortal(true)) {
            entity.setAsInsidePortal(this, pos);
        }
    }

    @Override @ParametersAreNonnullByDefault
    public int getPortalTransitionTime(ServerLevel level, Entity entity) {
        return entity instanceof Player player
                ? Math.max(
                1,
                level.getGameRules()
                        .getInt(
                                player.getAbilities().invulnerable
                                        ? GameRules.RULE_PLAYERS_NETHER_PORTAL_CREATIVE_DELAY
                                        : GameRules.RULE_PLAYERS_NETHER_PORTAL_DEFAULT_DELAY
                        )
        )
                : 0;
    }


    @Override
    @ParametersAreNonnullByDefault
    public TeleportTransition getPortalDestination(ServerLevel level, Entity entity, BlockPos pos) {
        var destinationLevelKey = level.dimension() == data.dimensionA() ? data.dimensionB() : data.dimensionA();
        var destinationLevel = level.getServer().getLevel(destinationLevelKey);
        if (destinationLevel == null) {
            return null;
        }

        var isDestinationLevelB = destinationLevel.dimension() == data.dimensionB();
        var worldBorder = destinationLevel.getWorldBorder();
        var coordinateScale = DimensionType.getTeleportationScale(level.dimensionType(), destinationLevel.dimensionType());
        var scaledPosition = worldBorder.clampToBounds(entity.getX() * coordinateScale, entity.getY(), entity.getZ() * coordinateScale);
        return helper.getExitPortal(destinationLevel, entity, pos, scaledPosition, isDestinationLevelB, worldBorder);
    }

    @Override @ParametersAreNonnullByDefault
    public void animateTick(BlockState blockState, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(100) == 0) {
            data.soundEvent().ifPresent(soundEvent -> level.playLocalSound(
                    (double)pos.getX() + 0.5,
                    (double)pos.getY() + 0.5,
                    (double)pos.getZ() + 0.5,
                    soundEvent,
                    SoundSource.BLOCKS,
                    0.5F,
                    random.nextFloat() * 0.4F + 0.8F,
                    false
            ));
        }

        data.particles().ifPresent(particles -> {
            for (int i = 0; i < 4; i++) {
                var x = pos.getX() + random.nextDouble();
                var y = pos.getY() + random.nextDouble();
                var z = pos.getZ() + random.nextDouble();
                var xSpeed = (random.nextFloat() - 0.5) * 0.5;
                var ySpeed = (random.nextFloat() - 0.5) * 0.5;
                var zSpeed = (random.nextFloat() - 0.5) * 0.5;
                var j = random.nextInt(2) * 2 - 1;
                if (!level.getBlockState(pos.west()).is(this) && !level.getBlockState(pos.east()).is(this)) {
                    x = pos.getX() + 0.5 + 0.25 * j;
                    xSpeed = (random.nextFloat() * 2.0F * j);
                } else {
                    z = pos.getZ() + 0.5 + 0.25 * j;
                    zSpeed = (random.nextFloat() * 2.0F * j);
                }

                level.addParticle(particles, x, y, z, xSpeed, ySpeed, zSpeed);
            }
        });
    }

    @Override
    protected @NotNull BlockState rotate(@NotNull BlockState blockState, Rotation rotation) {
        return switch (rotation) {
            case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> switch (blockState.getValue(AXIS)) {
                case Z -> blockState.setValue(AXIS, Direction.Axis.X);
                case X -> blockState.setValue(AXIS, Direction.Axis.Z);
                default -> blockState;
            };
            default -> blockState;
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }

    @Override @ParametersAreNonnullByDefault
    protected @NotNull VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos pos, CollisionContext context) {
        return blockState.getValue(AXIS) == Direction.Axis.Z ? Z_AXIS_AABB : X_AXIS_AABB;
    }

    @Override @NotNull
    public Portal.Transition getLocalTransition() {
        return data.transition();
    }
}
