package com.ametrinstudios.ametrin.world.dimension.portal;

import com.ametrinstudios.ametrin.world.block.PortalBlock;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Optional;

public final class PortalHelper {

    private final PortalData data;

    public PortalHelper(PortalData data) {
        this.data = data;
    }

    @Nullable
    public TeleportTransition getExitPortal(
            ServerLevel level, Entity entity, BlockPos entryPos, BlockPos exitPos, boolean isTarget, WorldBorder worldBorder
    ) {
        var closestPortalPosition = findClosestPortalPosition(level, exitPos, isTarget, worldBorder);
        BlockUtil.FoundRectangle portalRect;
        TeleportTransition.PostTeleportTransition postTransition;
        if (closestPortalPosition.isPresent()) {
            var portalPos = closestPortalPosition.get();
            var blockstate = level.getBlockState(portalPos);
            portalRect = BlockUtil.getLargestRectangleAround(
                    portalPos,
                    blockstate.getValue(BlockStateProperties.HORIZONTAL_AXIS),
                    21,
                    Direction.Axis.Y,
                    21,
                    pos -> level.getBlockState(pos) == blockstate
            );
            postTransition = TeleportTransition.PLAY_PORTAL_SOUND.then(traveller -> traveller.placePortalTicket(portalPos));
        } else {
            var portalAxis = entity.level().getBlockState(entryPos).getOptionalValue(PortalBlock.AXIS).orElse(Direction.Axis.X);
            var portal = createPortal(level, exitPos, portalAxis);
            if (portal.isEmpty()) {
                return null;
            }

            portalRect = portal.get();
            postTransition = TeleportTransition.PLAY_PORTAL_SOUND.then(TeleportTransition.PLACE_PORTAL_TICKET);
        }

        return getTeleportTransitionFromExit(entity, entryPos, portalRect, level, postTransition);
    }

    private Optional<BlockPos> findClosestPortalPosition(ServerLevel level, BlockPos exitPos, boolean isNether, WorldBorder worldBorder) {
        PoiManager poimanager = level.getPoiManager();
        int i = isNether ? 16 : 128;
        poimanager.ensureLoadedAndValid(level, exitPos, i);
        return poimanager.getInSquare(poiType -> poiType.is(data.portalPoi()), exitPos, i, PoiManager.Occupancy.ANY)
                .map(PoiRecord::getPos)
                .filter(worldBorder::isWithinBounds)
                .filter(pos -> level.getBlockState(pos).hasProperty(BlockStateProperties.HORIZONTAL_AXIS))
                .min(Comparator.<BlockPos>comparingDouble(p_352046_ -> p_352046_.distSqr(exitPos)).thenComparingInt(Vec3i::getY));
    }

    public static TeleportTransition getTeleportTransitionFromExit(
            Entity entity, BlockPos entryPos, BlockUtil.FoundRectangle portalRect, ServerLevel level, TeleportTransition.PostTeleportTransition postTransition
    ) {
        var blockState = entity.level().getBlockState(entryPos); //?
        Direction.Axis portalAxis;
        Vec3 relativePosition;
        if (blockState.hasProperty(BlockStateProperties.HORIZONTAL_AXIS)) {
            portalAxis = blockState.getValue(BlockStateProperties.HORIZONTAL_AXIS);
            var blockutil$foundrectangle = BlockUtil.getLargestRectangleAround(
                    entryPos, portalAxis, 21, Direction.Axis.Y, 21, pos -> entity.level().getBlockState(pos) == blockState
            );
            relativePosition = entity.getRelativePortalPosition(portalAxis, blockutil$foundrectangle);
        } else {
            portalAxis = Direction.Axis.X;
            relativePosition = new Vec3(0.5, 0.0, 0.0);
        }

        return createTeleportTransition(
                level, portalRect, portalAxis, relativePosition, entity, entity.getDeltaMovement(), entity.getYRot(), entity.getXRot(), postTransition
        );
    }

    public static TeleportTransition createTeleportTransition(
            ServerLevel level,
            BlockUtil.FoundRectangle portalRect,
            Direction.Axis portalAxis,
            Vec3 relativeOffset,
            Entity entity,
            Vec3 entitySpeed,
            float entityYRot,
            float entityXRot,
            TeleportTransition.PostTeleportTransition postTransition
    ) {
        var cornerPos = portalRect.minCorner;
        var frameBlockState = level.getBlockState(cornerPos);
        var frameDirection = frameBlockState.getOptionalValue(BlockStateProperties.HORIZONTAL_AXIS).orElse(Direction.Axis.X);
        var entitydimensions = entity.getDimensions(entity.getPose());
        var rotationIncrease = portalAxis == frameDirection ? 0f : 90f;
        var newEntitySpeed = portalAxis == frameDirection ? entitySpeed : new Vec3(entitySpeed.z, entitySpeed.y, -entitySpeed.x);
        var d2 = (double)entitydimensions.width() / 2.0 + (portalRect.axis1Size - (double)entitydimensions.width()) * relativeOffset.x();
        var d3 = (portalRect.axis2Size - (double)entitydimensions.height()) * relativeOffset.y();
        var d4 = 0.5 + relativeOffset.z();
        var isXAxis = frameDirection == Direction.Axis.X;
        var rawPos = new Vec3((double)cornerPos.getX() + (isXAxis ? d2 : d4), (double)cornerPos.getY() + d3, (double)cornerPos.getZ() + (isXAxis ? d4 : d2));
        var newEntityPos = PortalShape.findCollisionFreePosition(rawPos, level, entity, entitydimensions);
        return new TeleportTransition(level, newEntityPos, newEntitySpeed, entityYRot + rotationIncrease, entityXRot, postTransition);
    }

    public Optional<BlockUtil.FoundRectangle> createPortal(ServerLevel level, BlockPos pos, Direction.Axis axis) {
        var direction = Direction.get(Direction.AxisDirection.POSITIVE, axis);
        double d0 = -1.0;
        BlockPos blockpos = null;
        double d1 = -1.0;
        BlockPos blockpos1 = null;
        WorldBorder worldborder = level.getWorldBorder();
        int i = Math.min(level.getMaxY(), level.getMinY() + level.getLogicalHeight() - 1);
        BlockPos.MutableBlockPos mutablePos = pos.mutable();

        for (var spiralPos : BlockPos.spiralAround(pos, 16, Direction.EAST, Direction.SOUTH)) {
            int k = Math.min(i, level.getHeight(Heightmap.Types.MOTION_BLOCKING, spiralPos.getX(), spiralPos.getZ()));
            if (worldborder.isWithinBounds(spiralPos) && worldborder.isWithinBounds(spiralPos.move(direction, 1))) {
                spiralPos.move(direction.getOpposite(), 1);

                for (int l = k; l >= level.getMinY(); l--) {
                    spiralPos.setY(l);
                    if (canPortalReplaceBlock(level, spiralPos)) {
                        int i1 = l;

                        while (l > level.getMinY() && canPortalReplaceBlock(level, spiralPos.move(Direction.DOWN))) {
                            l--;
                        }

                        if (l + 4 <= i) {
                            int j1 = i1 - l;
                            if (j1 <= 0 || j1 >= 3) {
                                spiralPos.setY(l);
                                if (canHostFrame(level, spiralPos, mutablePos, direction, 0)) {
                                    double d2 = pos.distSqr(spiralPos);
                                    if (canHostFrame(level, spiralPos, mutablePos, direction, -1)
                                            && canHostFrame(level, spiralPos, mutablePos, direction, 1)
                                            && (d0 == -1.0 || d0 > d2)) {
                                        d0 = d2;
                                        blockpos = spiralPos.immutable();
                                    }

                                    if (d0 == -1.0 && (d1 == -1.0 || d1 > d2)) {
                                        d1 = d2;
                                        blockpos1 = spiralPos.immutable();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (d0 == -1.0 && d1 != -1.0) {
            blockpos = blockpos1;
            d0 = d1;
        }

        var frameState = data.frameBlock().get();
        if (d0 == -1.0) {
            int k1 = Math.max(level.getMinY() - -1, 70);
            int i2 = i - 9;
            if (i2 < k1) {
                return Optional.empty();
            }

            blockpos = new BlockPos(pos.getX() - direction.getStepX(), Mth.clamp(pos.getY(), k1, i2), pos.getZ() - direction.getStepZ())
                    .immutable();
            blockpos = worldborder.clampToBounds(blockpos);
            Direction direction1 = direction.getClockWise();

            for (int i3 = -1; i3 < 2; i3++) {
                for (int j3 = 0; j3 < 2; j3++) {
                    for (int k3 = -1; k3 < 3; k3++) {
                        var blockState = k3 < 0 ? frameState : Blocks.AIR.defaultBlockState();
                        mutablePos.setWithOffset(
                                blockpos, j3 * direction.getStepX() + i3 * direction1.getStepX(), k3, j3 * direction.getStepZ() + i3 * direction1.getStepZ()
                        );
                        level.setBlockAndUpdate(mutablePos, blockState);
                    }
                }
            }
        }

        for (int l1 = -1; l1 < 3; l1++) {
            for (int j2 = -1; j2 < 4; j2++) {
                if (l1 == -1 || l1 == 2 || j2 == -1 || j2 == 3) {
                    mutablePos.setWithOffset(blockpos, l1 * direction.getStepX(), j2, l1 * direction.getStepZ());
                    level.setBlock(mutablePos, frameState, 3);
                }
            }
        }

        var portalState = data.portalBlock().get().setValue(PortalBlock.AXIS, axis);

        for (int k2 = 0; k2 < 2; k2++) {
            for (int l2 = 0; l2 < 3; l2++) {
                mutablePos.setWithOffset(blockpos, k2 * direction.getStepX(), l2, k2 * direction.getStepZ());
                level.setBlock(mutablePos, portalState, 18);
            }
        }

        return Optional.of(new BlockUtil.FoundRectangle(blockpos.immutable(), 2, 3));
    }

    private static boolean canPortalReplaceBlock(Level level, BlockPos.MutableBlockPos pos) {
        BlockState blockstate = level.getBlockState(pos);
        return blockstate.canBeReplaced() && blockstate.getFluidState().isEmpty();
    }

    private static boolean canHostFrame(Level level, BlockPos originalPos, BlockPos.MutableBlockPos offsetPos, Direction direction, int offsetScale) {
        Direction actualDirection = direction.getClockWise();

        for (int i = -1; i < 3; i++) {
            for (int j = -1; j < 4; j++) {
                offsetPos.setWithOffset(
                        originalPos, actualDirection.getStepX() * i + actualDirection.getStepX() * offsetScale, j, direction.getStepZ() * i + actualDirection.getStepZ() * offsetScale
                );
                if (j < 0 && !level.getBlockState(offsetPos).isSolid()) {
                    return false;
                }

                if (j >= 0 && !canPortalReplaceBlock(level, offsetPos)) {
                    return false;
                }
            }
        }

        return true;
    }
}
