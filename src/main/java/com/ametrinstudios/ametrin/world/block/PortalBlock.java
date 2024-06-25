package com.ametrinstudios.ametrin.world.block;

import com.ametrinstudios.ametrin.world.dimension.portal.PortalHelper;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;

public abstract class PortalBlock extends Block implements Portal {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    private static final Logger LOGGER = LogUtils.getLogger();
    protected static final VoxelShape X_AXIS_AABB = Block.box(0.0, 0.0, 6.0, 16.0, 16.0, 10.0);
    protected static final VoxelShape Z_AXIS_AABB = Block.box(6.0, 0.0, 0.0, 10.0, 16.0, 16.0);
    protected final PortalHelper helper;

    public PortalBlock(int lightLevel, PortalHelper helper){
        this(SoundType.GLASS, lightLevel, helper);
    }

    public PortalBlock(SoundType soundType, int lightLevel, PortalHelper helper) {
        this(Properties.of().strength(-1).noCollission().lightLevel((state) -> lightLevel).noLootTable().randomTicks().sound(soundType), helper);
    }

    public PortalBlock(BlockBehaviour.Properties properties, PortalHelper helper) {
        super(properties);
        this.helper = helper;
        registerDefaultState(stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

    public boolean trySpawnPortal(LevelAccessor level, BlockPos pos){
        return false;
    }

    @Override @ParametersAreNonnullByDefault
    protected @NotNull BlockState updateShape(BlockState thisState, Direction facing, BlockState facingState, LevelAccessor level, BlockPos thisPos, BlockPos facingPos) {
        var facingAxis = facing.getAxis();
        var thisAxis = thisState.getValue(AXIS);
        boolean flag = thisAxis != facingAxis && facingAxis.isHorizontal();
        return !flag && !facingState.is(this) && !new PortalShape(level, thisPos, thisAxis).isComplete()
                ? Blocks.AIR.defaultBlockState()
                : super.updateShape(thisState, facing, facingState, level, thisPos, facingPos);
    }

    @Override @ParametersAreNonnullByDefault
    protected void entityInside(BlockState blockState, Level level, BlockPos pos, Entity pEntity) {
        if (pEntity.canUsePortal(false)) {
            pEntity.setAsInsidePortal(this, pos);
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


    @Override @Nullable @ParametersAreNonnullByDefault
    public DimensionTransition getPortalDestination(ServerLevel level, Entity entity, BlockPos pos) {
        var destinationLevelKey = level.dimension() == Level.OVERWORLD ? targetLevel() : Level.OVERWORLD;
        var destinationLevel = level.getServer().getLevel(destinationLevelKey);
        if (destinationLevel == null) {
            return null;
        }

        var isDestinationTarget = destinationLevel.dimension() == targetLevel();
        var worldBorder = destinationLevel.getWorldBorder();
        var coordinateScale = DimensionType.getTeleportationScale(level.dimensionType(), destinationLevel.dimensionType());
        var scaledPosition = worldBorder.clampToBounds(entity.getX() * coordinateScale, entity.getY(), entity.getZ() * coordinateScale);
        return helper.getExitPortal(destinationLevel, entity, pos, scaledPosition, isDestinationTarget, worldBorder);
    }

    @Override
    public @NotNull Portal.Transition getLocalTransition() {
        return Portal.Transition.CONFUSION;
    }

    @Override @ParametersAreNonnullByDefault
    public void animateTick(BlockState blockState, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(100) == 0) {
            level.playLocalSound(
                    (double)pos.getX() + 0.5,
                    (double)pos.getY() + 0.5,
                    (double)pos.getZ() + 0.5,
                    SoundEvents.PORTAL_AMBIENT,
                    SoundSource.BLOCKS,
                    0.5F,
                    random.nextFloat() * 0.4F + 0.8F,
                    false
            );
        }

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

            level.addParticle(particle(), x, y, z, xSpeed, ySpeed, zSpeed);
        }
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

//    protected abstract PortalBlock registeredBlock();
//    protected abstract TagKey<Block> portalFrameBlocks();
    protected abstract ParticleOptions particle();
    protected abstract ResourceKey<Level> targetLevel();
//    protected abstract ResourceKey<PoiType> poi();
//    protected abstract Block defaultPortalFrameBlock();
}