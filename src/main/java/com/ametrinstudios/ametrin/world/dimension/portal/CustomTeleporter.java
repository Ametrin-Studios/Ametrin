package com.ametrinstudios.ametrin.world.dimension.portal;

import com.ametrinstudios.ametrin.world.block.PortalBlock;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;

public class CustomTeleporter implements ITeleporter {
    protected final ServerLevel level;

    protected final PoiType poi;
    protected final PortalBlock portalBlock;
    protected final Block portalFrameBlock;
    protected final ResourceKey<Level> targetLevel;

    public CustomTeleporter(ServerLevel level, PoiType poi, ResourceKey<Level> targetLevel, PortalBlock portalBlock, Block portalFrameBlock){
        this.level = level;
        this.poi = poi;
        this.targetLevel = targetLevel;
        this.portalBlock = portalBlock;
        this.portalFrameBlock = portalFrameBlock;
    }

    public Optional<BlockUtil.FoundRectangle> getExistingPortal(BlockPos pos){
        PoiManager poiManager = this.level.getPoiManager();
        poiManager.ensureLoadedAndValid(this.level, pos, 64);
        Optional<PoiRecord> optional = poiManager.getInSquare((poiType) ->
                poiType.get() == poi, pos, 64, PoiManager.Occupancy.ANY).sorted(Comparator.<PoiRecord>comparingDouble((poi) ->
                poi.getPos().distSqr(pos)).thenComparingInt((poi) ->
                poi.getPos().getY())).filter((poi) ->
                this.level.getBlockState(poi.getPos()).hasProperty(BlockStateProperties.HORIZONTAL_AXIS)).findFirst();
        return optional.map((poi) -> {
            BlockPos blockpos = poi.getPos();
            this.level.getChunkSource().addRegionTicket(TicketType.PORTAL, new ChunkPos(blockpos), 3, blockpos);
            BlockState blockstate = this.level.getBlockState(blockpos);
            return BlockUtil.getLargestRectangleAround(blockpos, blockstate.getValue(BlockStateProperties.HORIZONTAL_AXIS), 21, Direction.Axis.Y, 21, (posIn) ->
                    this.level.getBlockState(posIn) == blockstate);
        });
    }

    public Optional<BlockUtil.FoundRectangle> makePortal(BlockPos pos, Direction.Axis axis){
        Direction direction = Direction.get(Direction.AxisDirection.POSITIVE, axis);
        double d0 = -1.0D;
        BlockPos blockpos = null;
        double d1 = -1.0D;
        BlockPos blockpos1 = null;
        WorldBorder worldborder = this.level.getWorldBorder();
        int dimensionLogicalHeight = this.level.getHeight() - 1;
        BlockPos.MutableBlockPos mutablePos = pos.mutable();

        for(BlockPos.MutableBlockPos currentPos : BlockPos.spiralAround(pos, 16, Direction.EAST, Direction.SOUTH)){
            int j = Math.min(dimensionLogicalHeight, this.level.getHeight(Heightmap.Types.MOTION_BLOCKING, currentPos.getX(), currentPos.getZ()));
            if (worldborder.isWithinBounds(currentPos) && worldborder.isWithinBounds(currentPos.move(direction, 1))) {
                currentPos.move(direction.getOpposite(), 1);

                for(int l = j; l >= 0; --l) {
                    currentPos.setY(l);
                    if (this.level.isEmptyBlock(currentPos)) {
                        int i1;
                        for(i1 = l; l > 0 && this.level.isEmptyBlock(currentPos.move(Direction.DOWN)); --l) {} //Do not delete!

                        if (l + 4 <= dimensionLogicalHeight) {
                            int j1 = i1 - l;
                            if (j1 <= 0 || j1 >= 3) {
                                currentPos.setY(l);
                                if (this.checkRegionForPlacement(currentPos, mutablePos, direction, 0)) {
                                    double d2 = pos.distSqr(currentPos);
                                    if (this.checkRegionForPlacement(currentPos, mutablePos, direction, -1) && this.checkRegionForPlacement(currentPos, mutablePos, direction, 1) && (d0 == -1.0D || d0 > d2)) {
                                        d0 = d2;
                                        blockpos = currentPos.immutable();
                                    }

                                    if (d0 == -1.0D && (d1 == -1.0D || d1 > d2)) {
                                        d1 = d2;
                                        blockpos1 = currentPos.immutable();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (d0 == -1.0D && d1 != -1.0D){
            blockpos = blockpos1;
            d0 = d1;
        }

        if (d0 == -1.0D) {
            blockpos = (new BlockPos(pos.getX(), Mth.clamp(pos.getY(), 70, this.level.getHeight() - 10), pos.getZ())).immutable();
            Direction direction1 = direction.getClockWise();
            if (!worldborder.isWithinBounds(blockpos)){
                return Optional.empty();
            }

            for(int l1 = -1; l1 < 2; ++l1){
                for(int k2 = 0; k2 < 2; ++k2){
                    for(int i3 = -1; i3 < 3; ++i3){
                        BlockState blockstate1 = i3 < 0 ? portalFrameBlock.defaultBlockState() : Blocks.AIR.defaultBlockState();
                        mutablePos.setWithOffset(blockpos, k2 * direction.getStepX() + l1 * direction1.getStepX(), i3, k2 * direction.getStepZ() + l1 * direction1.getStepZ());
                        this.level.setBlockAndUpdate(mutablePos, blockstate1);
                    }
                }
            }
        }

        for(int k1 = -1; k1 < 3; ++k1) {
            for(int i2 = -1; i2 < 4; ++i2) {
                if (k1 == -1 || k1 == 2 || i2 == -1 || i2 == 3) {
                    mutablePos.setWithOffset(blockpos, k1 * direction.getStepX(), i2, k1 * direction.getStepZ());
                    this.level.setBlock(mutablePos, portalFrameBlock.defaultBlockState(), 3);
                }
            }
        }

        BlockState kaupenPortal = portalBlock.defaultBlockState().setValue(PortalBlock.AXIS, axis);

        for(int j2 = 0; j2 < 2; ++j2) {
            for(int l2 = 0; l2 < 3; ++l2) {
                mutablePos.setWithOffset(blockpos, j2 * direction.getStepX(), l2, j2 * direction.getStepZ());
                this.level.setBlock(mutablePos, kaupenPortal, 18);
            }
        }

        return Optional.of(new BlockUtil.FoundRectangle(blockpos.immutable(), 2, 3));
    }

    private boolean checkRegionForPlacement(BlockPos originalPos, BlockPos.MutableBlockPos offsetPos, Direction directionIn, int offsetScale) {
        Direction direction = directionIn.getClockWise();

        for(int i = -1; i < 3; ++i) {
            for(int j = -1; j < 4; ++j) {
                offsetPos.setWithOffset(originalPos, directionIn.getStepX() * i + direction.getStepX() * offsetScale, j, directionIn.getStepZ() * i + direction.getStepZ() * offsetScale);
                if (j < 0 && !this.level.getBlockState(offsetPos).getMaterial().isSolid()) {
                    return false;
                }

                if (j >= 0 && !this.level.isEmptyBlock(offsetPos)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Nullable
    @Override
    public PortalInfo getPortalInfo(Entity entity, ServerLevel level, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        boolean destinationIsUG = level.dimension() == targetLevel;
        if (entity.level.dimension() != targetLevel && !destinationIsUG) {
            return null;
        }
        else {
            WorldBorder border = level.getWorldBorder();
            var minX = Math.max(-2.9999872E7D, border.getMinX() + 16.0D);
            var minZ = Math.max(-2.9999872E7D, border.getMinZ() + 16.0D);
            var maxX = Math.min(2.9999872E7D, border.getMaxX() - 16.0D);
            var maxZ = Math.min(2.9999872E7D, border.getMaxZ() - 16.0D);
            var coordinateDifference = DimensionType.getTeleportationScale(entity.level.dimensionType(), level.dimensionType());
            var x = (int)Mth.clamp(entity.getX() * coordinateDifference, minX, maxX);
            var y = (int)Mth.clamp(entity.getZ() * coordinateDifference, minZ, maxZ);
            var blockpos = new BlockPos(x, (int)entity.getY(), y);
            return this.getOrMakePortal(entity, blockpos).map((result) -> {
                var blockstate = entity.level.getBlockState(entity.portalEntrancePos);
                Direction.Axis axis;
                Vec3 vector3d;
                if (blockstate.hasProperty(BlockStateProperties.HORIZONTAL_AXIS)) {
                    axis = blockstate.getValue(BlockStateProperties.HORIZONTAL_AXIS);
                    BlockUtil.FoundRectangle rectangle = BlockUtil.getLargestRectangleAround(entity.portalEntrancePos, axis, 21, Direction.Axis.Y, 21, (pos) -> entity.level.getBlockState(pos) == blockstate);
                    vector3d = PortalShape.getRelativePosition(rectangle, axis, entity.position(), entity.getDimensions(entity.getPose()));
                } else {
                    axis = Direction.Axis.X;
                    vector3d = new Vec3(0.5D, 0.0D, 0.0D);
                }

                return PortalShape.createPortalInfo(level, result, axis, vector3d, entity, entity.getDeltaMovement(), entity.getYRot(), entity.getXRot());
            }).orElse(null);
        }
    }

    protected Optional<BlockUtil.FoundRectangle> getOrMakePortal(Entity entity, BlockPos pos) {
        Optional<BlockUtil.FoundRectangle> existingPortal = this.getExistingPortal(pos);
        if(existingPortal.isPresent()) {
            return existingPortal;
        }
        else {
            Direction.Axis portalAxis = this.level.getBlockState(entity.portalEntrancePos).getOptionalValue(PortalBlock.AXIS).orElse(Direction.Axis.X);
            return this.makePortal(pos, portalAxis);
        }
    }

    @Override
    public boolean playTeleportSound(ServerPlayer player, ServerLevel sourceLevel, ServerLevel destLevel) {return false;}
}