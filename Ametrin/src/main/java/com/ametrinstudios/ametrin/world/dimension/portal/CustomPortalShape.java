package com.ametrinstudios.ametrin.world.dimension.portal;

import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;

// this is used by the PortalCatalystItem to see if a valid portal has been built and place the portal blocks inside it.
public final class CustomPortalShape {
    // copied and modified from PortalShape
    // this is a huge mess, need to clean it up

    //todo: unhardcode these
    private static final int MIN_WIDTH = 2;
    public static final int MAX_WIDTH = 21;
    private static final int MIN_HEIGHT = 3;
    public static final int MAX_HEIGHT = 21;
    private static final float SAFE_TRAVEL_MAX_ENTITY_XY = 4.0F;
    private static final double SAFE_TRAVEL_MAX_VERTICAL_DELTA = 1.0;

    private final PortalData data;
    private final LevelAccessor level;
    private final Direction.Axis axis;
    private final Direction rightDir;
    private int numPortalBlocks;
    @Nullable
    private BlockPos bottomLeft;
    private int height;
    private final int width;

    public static Optional<CustomPortalShape> findEmptyPortalShape(PortalData data, LevelAccessor level, BlockPos bottomLeft, Direction.Axis axis) {
        return findPortalShape(data, level, bottomLeft, shape -> shape.isValid() && shape.numPortalBlocks == 0, axis);
    }

    public static Optional<CustomPortalShape> findPortalShape(PortalData data, LevelAccessor level, BlockPos bottomLeft, Predicate<CustomPortalShape> predicate, Direction.Axis axis) {
        var optional = Optional.of(new CustomPortalShape(data, level, bottomLeft, axis)).filter(predicate);
        if (optional.isPresent()) {
            return optional;
        }

        var otherAxis = axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
        return Optional.of(new CustomPortalShape(data, level, bottomLeft, otherAxis)).filter(predicate);
    }

    public CustomPortalShape(PortalData data, LevelAccessor level, @Nullable BlockPos bottomLeft, Direction.Axis axis) {
        this.data = data;
        this.level = level;
        this.axis = axis;
        this.rightDir = axis == Direction.Axis.X ? Direction.WEST : Direction.SOUTH;
        this.bottomLeft = this.calculateBottomLeft(bottomLeft);
        if (this.bottomLeft == null) {
            this.bottomLeft = bottomLeft;
            this.width = 1;
            this.height = 1;
        } else {
            this.width = this.calculateWidth();
            if (this.width > 0) {
                this.height = this.calculateHeight();
            }
        }
    }

    @Nullable
    private BlockPos calculateBottomLeft(BlockPos pos) {
        int i = Math.max(this.level.getMinBuildHeight(), pos.getY() - MAX_HEIGHT);

        while (pos.getY() > i && isEmpty(this.level.getBlockState(pos.below()))) {
            pos = pos.below();
        }

        Direction direction = this.rightDir.getOpposite();
        int j = this.getDistanceUntilEdgeAboveFrame(pos, direction) - 1;
        return j < 0 ? null : pos.relative(direction, j);
    }

    private int calculateWidth() {
        int i = this.getDistanceUntilEdgeAboveFrame(this.bottomLeft, this.rightDir);
        return i >= 2 && i <= MAX_WIDTH ? i : 0;
    }

    private int getDistanceUntilEdgeAboveFrame(BlockPos pos, Direction direction) {
        var mutablePos = new BlockPos.MutableBlockPos();

        for (int i = 0; i <= 21; i++) {
            mutablePos.set(pos).move(direction, i);
            BlockState blockstate = this.level.getBlockState(mutablePos);
            if (!isEmpty(blockstate)) {
                if (isValidFrame(blockstate)) {
                    return i;
                }
                break;
            }

            BlockState state = this.level.getBlockState(mutablePos.move(Direction.DOWN));
            if (!isValidFrame(state)) {
                break;
            }
        }

        return 0;
    }

    private int calculateHeight() {
        var blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        int i = this.getDistanceUntilTop(blockpos$mutableblockpos);
        return i >= 3 && i <= 21 && this.hasTopFrame(blockpos$mutableblockpos, i) ? i : 0;
    }

    private boolean hasTopFrame(BlockPos.MutableBlockPos pos, int distanceToTop) {
        for (int i = 0; i < this.width; i++) {
            var mutableBlockPos = pos.set(this.bottomLeft).move(Direction.UP, distanceToTop).move(this.rightDir, i);
            if (!isValidFrame(this.level.getBlockState(mutableBlockPos))) {
                return false;
            }
        }

        return true;
    }

    private int getDistanceUntilTop(BlockPos.MutableBlockPos pos) {
        for (int i = 0; i < MAX_HEIGHT; i++) {
            pos.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, -1);
            if (!isValidFrame(this.level.getBlockState(pos))) {
                return i;
            }

            pos.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, this.width);
            if (!isValidFrame(this.level.getBlockState(pos))) {
                return i;
            }

            for (int j = 0; j < this.width; j++) {
                pos.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, j);
                BlockState blockstate = this.level.getBlockState(pos);
                if (!isEmpty(blockstate)) {
                    return i;
                }

                if (blockstate.is(Blocks.NETHER_PORTAL)) {
                    this.numPortalBlocks++;
                }
            }
        }

        return 21;
    }

    private boolean isValidFrame(BlockState state) {
        return state.is(data.validFrames());
    }

    private boolean isEmpty(BlockState state) {
        return state.isAir() || state.is(BlockTags.FIRE) || state.is(data.portalBlock().get().getBlock());
    }

    public boolean isValid() {
        return this.bottomLeft != null && this.width >= MIN_WIDTH && this.width <= MAX_WIDTH && this.height >= MIN_HEIGHT && this.height <= MAX_HEIGHT;
    }

    public void createPortalBlocks() {
        final var state = data.portalBlock().get().setValue(NetherPortalBlock.AXIS, axis);
        BlockPos.betweenClosed(bottomLeft, bottomLeft.relative(Direction.UP, height - 1).relative(rightDir, width - 1))
                .forEach(pos -> level.setBlock(pos, state, 18));
    }

    public boolean isComplete() {
        return this.isValid() && this.numPortalBlocks == this.width * this.height;
    }

    public static Vec3 getRelativePosition(BlockUtil.FoundRectangle foundRectangle, Direction.Axis axis, Vec3 pos, EntityDimensions entityDimensions) {
        double d0 = (double)foundRectangle.axis1Size - (double)entityDimensions.width();
        double d1 = (double)foundRectangle.axis2Size - (double)entityDimensions.height();
        BlockPos blockpos = foundRectangle.minCorner;
        double d2;
        if (d0 > 0.0) {
            double d3 = (double)blockpos.get(axis) + (double)entityDimensions.width() / 2.0;
            d2 = Mth.clamp(Mth.inverseLerp(pos.get(axis) - d3, 0.0, d0), 0.0, SAFE_TRAVEL_MAX_VERTICAL_DELTA);
        } else {
            d2 = 0.5;
        }

        double d5;
        if (d1 > 0.0) {
            Direction.Axis direction$axis = Direction.Axis.Y;
            d5 = Mth.clamp(Mth.inverseLerp(pos.get(direction$axis) - (double)blockpos.get(direction$axis), 0.0, d1), 0.0, SAFE_TRAVEL_MAX_VERTICAL_DELTA);
        } else {
            d5 = 0.0;
        }

        Direction.Axis direction$axis1 = axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
        double d4 = pos.get(direction$axis1) - ((double)blockpos.get(direction$axis1) + 0.5);
        return new Vec3(d2, d5, d4);
    }

    public static Vec3 findCollisionFreePosition(Vec3 pos, ServerLevel level, Entity entity, EntityDimensions dimensions) {
        if (!(dimensions.width() > SAFE_TRAVEL_MAX_ENTITY_XY) && !(dimensions.height() > SAFE_TRAVEL_MAX_ENTITY_XY)) {
            double d0 = (double)dimensions.height() / 2.0;
            Vec3 vec3 = pos.add(0.0, d0, 0.0);
            VoxelShape voxelshape = Shapes.create(
                    AABB.ofSize(vec3, dimensions.width(), 0.0, dimensions.width()).expandTowards(0.0, 1.0, 0.0).inflate(1.0E-6)
            );
            Optional<Vec3> optional = level.findFreePosition(
                    entity, voxelshape, vec3, dimensions.width(), dimensions.height(), dimensions.width()
            );
            Optional<Vec3> optional1 = optional.map(p_259019_ -> p_259019_.subtract(0.0, d0, 0.0));
            return optional1.orElse(pos);
        } else {
            return pos;
        }
    }
}
