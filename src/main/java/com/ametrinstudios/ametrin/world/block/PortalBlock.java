package com.ametrinstudios.ametrin.world.block;

import com.ametrinstudios.ametrin.world.dimension.portal.CustomTeleporter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

public abstract class PortalBlock extends Block {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    protected static final VoxelShape X_AABB = Block.box(0, 0, 6, 16, 16, 10);
    protected static final VoxelShape Z_AABB = Block.box(6, 0, 0, 10, 16, 16);

    public PortalBlock(int lightLevel){
        this(SoundType.GLASS, lightLevel);
    }

    public PortalBlock(SoundType soundType, int lightLevel) {
        super(Properties.of().strength(-1).noCollission().lightLevel((state) -> lightLevel).noLootTable().randomTicks().sound(soundType));
        registerDefaultState(stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

    public boolean trySpawnPortal(LevelAccessor worldIn, BlockPos pos){
        PortalBlock.Size size = getPortalSize(worldIn, pos);
        if (size != null && !onTrySpawnPortal(worldIn, pos, size)){
            size.placePortalBlocks();
            return true;
        }else{
            return false;
        }
    }

    public static boolean onTrySpawnPortal(LevelAccessor world, BlockPos pos, PortalBlock.Size size){
        return MinecraftForge.EVENT_BUS.post(new PortalSpawnEvent(world, pos, world.getBlockState(pos), size));
    }

    @Cancelable
    public static class PortalSpawnEvent extends BlockEvent{
        private final PortalBlock.Size size;

        public PortalSpawnEvent(LevelAccessor world, BlockPos pos, BlockState state, PortalBlock.Size size){
            super(world, pos, state);
            this.size = size;
        }

        public PortalBlock.Size getPortalSize() {return size;}
    }

    @Nullable
    public PortalBlock.Size getPortalSize(LevelAccessor world, BlockPos pos){
        PortalBlock.Size size = new Size(world, pos, Direction.Axis.X, registeredBlock(), portalFrameBlocks());
        if (size.isValid() && size.portalBlockCount == 0) {
            return size;
        }else{
            PortalBlock.Size size2 = new Size(world, pos, Direction.Axis.Z, registeredBlock(), portalFrameBlocks());
            return size2.isValid() && size2.portalBlockCount == 0 ? size2 : null;
        }
    }

    @Override @ParametersAreNonnullByDefault
    public @NotNull BlockState updateShape(BlockState blockState, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        Direction.Axis direction$axis = facing.getAxis();
        Direction.Axis direction = blockState.getValue(AXIS);
        boolean flag = direction != direction$axis && direction$axis.isHorizontal();
        return !flag && facingState.getBlock() != this && !(new Size(level, currentPos, direction, registeredBlock(), portalFrameBlocks())).validatePortal() ? Blocks.AIR.defaultBlockState() : super.updateShape(blockState, facing, facingState, level, currentPos, facingPos);
    }

    @Override @ParametersAreNonnullByDefault
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity){
        if(!entity.isPassenger() && !entity.isVehicle() && entity.canChangeDimensions()){
            if(entity.isOnPortalCooldown()){
                entity.setPortalCooldown();
            }
            else {
                if(!entity.level().isClientSide && !pos.equals(entity.portalEntrancePos)){
                    entity.portalEntrancePos = pos.immutable();
                }
                Level entityWorld = entity.level();
                MinecraftServer server = entityWorld.getServer();
                ResourceKey<Level> destination = entity.level().dimension() == targetLevel() ? Level.OVERWORLD : targetLevel();
                if(server != null) {
                    ServerLevel destinationWorld = server.getLevel(destination);
                    if(destinationWorld != null && server.isNetherEnabled() && !entity.isPassenger()){
                        entity.level().getProfiler().push("kaupen_portal");
                        entity.setPortalCooldown();
                        entity.changeDimension(destinationWorld, new CustomTeleporter(destinationWorld, poi(), targetLevel(), registeredBlock(), defaultPortalFrameBlock()));
                        entity.level().getProfiler().pop();
                    }
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override @ParametersAreNonnullByDefault
    public void animateTick(BlockState blockState, Level level, BlockPos pos, RandomSource rand) {
        if (rand.nextInt(100) == 0) {
            level.playLocalSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D,
                    (double)pos.getZ() + 0.5D, SoundEvents.PORTAL_AMBIENT,
                    SoundSource.BLOCKS, 0.5F, rand.nextFloat() * 0.4F + 0.8F, false);
        }

        for(int i = 0; i < 4; ++i) {
            double x = (double)pos.getX() + rand.nextDouble();
            double y = (double)pos.getY() + rand.nextDouble();
            double z = (double)pos.getZ() + rand.nextDouble();
            double xSpeed = ((double)rand.nextFloat() - 0.5D) * 0.5D;
            double ySpeed = ((double)rand.nextFloat() - 0.5D) * 0.5D;
            double zSpeed = ((double)rand.nextFloat() - 0.5D) * 0.5D;
            int j = rand.nextInt(2) * 2 - 1;
            if (!level.getBlockState(pos.west()).is(this) && !level.getBlockState(pos.east()).is(this)) {
                x = (double)pos.getX() + 0.5D + 0.25D * (double)j;
                xSpeed = rand.nextFloat() * 2.0F * (float)j;
            } else {
                z = (double)pos.getZ() + 0.5D + 0.25D * (double)j;
                zSpeed = rand.nextFloat() * 2.0F * (float)j;
            }

            level.addParticle(particle(), x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {return ItemStack.EMPTY;}

    @Override
    public @NotNull BlockState rotate(@NotNull BlockState blockState, Rotation rot){
        return switch (rot) {
            case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> switch (blockState.getValue(AXIS)) {
                case Z -> blockState.setValue(AXIS, Direction.Axis.X);
                case X -> blockState.setValue(AXIS, Direction.Axis.Z);
                default -> blockState;
            };
            default -> blockState;
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {builder.add(AXIS);}

    public static class Size {
        private final LevelAccessor level;
        private final Direction.Axis axis;
        private final Direction rightDir;
        private final Direction leftDir;
        private int portalBlockCount;
        private BlockPos bottomLeft;
        private int height;
        private int width;
        protected final TagKey<Block> portalFrameBlocks;
        protected final PortalBlock registeredBlock;

        public Size(LevelAccessor level, BlockPos pos, Direction.Axis axis, PortalBlock registeredBlock, TagKey<Block> portalFrameBlocks) {
            this.level = level;
            this.axis = axis;
            this.portalFrameBlocks = portalFrameBlocks;
            this.registeredBlock = registeredBlock;
            if (axis == Direction.Axis.X) {
                this.leftDir = Direction.EAST;
                this.rightDir = Direction.WEST;
            } else {
                this.leftDir = Direction.NORTH;
                this.rightDir = Direction.SOUTH;
            }

            for(BlockPos blockpos = pos; pos.getY() > blockpos.getY() - 21 && pos.getY() > 0 && this.canConnect(level.getBlockState(pos.below())); pos = pos.below()) {}

            int i = this.getDistanceUntilEdge(pos, this.leftDir) - 1;
            if (i >= 0) {
                this.bottomLeft = pos.relative(this.leftDir, i);
                this.width = this.getDistanceUntilEdge(this.bottomLeft, this.rightDir);
                if (this.width < 2 || this.width > 21) {
                    this.bottomLeft = null;
                    this.width = 0;
                }
            }

            if (this.bottomLeft != null) {
                this.height = this.calculatePortalHeight();
            }

        }

        protected int getDistanceUntilEdge(BlockPos pos, Direction directionIn) {
            int i;
            for(i = 0; i < 22; ++i) {
                BlockPos blockpos = pos.relative(directionIn, i);
                if(!this.canConnect(this.level.getBlockState(blockpos)) ||
                        !(this.level.getBlockState(blockpos.below()).is(portalFrameBlocks))) {
                    break;
                }
            }

            BlockPos framePos = pos.relative(directionIn, i);
            return this.level.getBlockState(framePos).is(portalFrameBlocks) ? i : 0;
        }

        public int getHeight() {return this.height;}

        public int getWidth() {return this.width;}

        protected int calculatePortalHeight() {
            label56:
            for(height = 0; height < 21; ++height) {
                for(int i = 0; i < width; ++i) {
                    BlockPos blockpos = bottomLeft.relative(rightDir, i).above(height);
                    BlockState blockstate = level.getBlockState(blockpos);
                    if (!canConnect(blockstate)) {
                        break label56;
                    }

                    Block block = blockstate.getBlock();
                    if (block == registeredBlock) {
                        ++portalBlockCount;
                    }

                    if (i == 0) {
                        BlockPos framePos = blockpos.relative(leftDir);
                        if (!(level.getBlockState(framePos).is(portalFrameBlocks))) {
                            break label56;
                        }
                    } else if (i == width - 1) {
                        BlockPos framePos = blockpos.relative(rightDir);
                        if (!(level.getBlockState(framePos).is(portalFrameBlocks))) {
                            break label56;
                        }
                    }
                }
            }

            for(int j = 0; j < width; ++j) {
                BlockPos framePos = bottomLeft.relative(rightDir, j).above(height);
                if (!(level.getBlockState(framePos).is(portalFrameBlocks))) {
                    height = 0;
                    break;
                }
            }

            if (height <= 21 && height >= 3) {
                return height;
            } else {
                bottomLeft = null;
                width = 0;
                height = 0;
                return 0;
            }
        }

        protected boolean canConnect(BlockState pos) {
            Block block = pos.getBlock();
            return pos.isAir() || block == registeredBlock;
        }

        public boolean isValid() {
            return bottomLeft != null && width >= 2 && width <= 21 && height >= 3 && height <= 21;
        }

        public void placePortalBlocks() {
            for(int i = 0; i < width; ++i) {
                BlockPos blockpos = bottomLeft.relative(rightDir, i);

                for(int j = 0; j < height; ++j) {
                    level.setBlock(blockpos.above(j), registeredBlock.defaultBlockState().setValue(PortalBlock.AXIS, axis), 18);
                }
            }

        }

        private boolean isPortalCountValidForSize() {return portalBlockCount >= width * height;}
        public boolean validatePortal() {return isValid() && isPortalCountValidForSize();}
    }


    @Override @ParametersAreNonnullByDefault
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return state.getValue(AXIS) == Direction.Axis.Z ? Z_AABB : X_AABB;
    }

    protected abstract PortalBlock registeredBlock();
    protected abstract TagKey<Block> portalFrameBlocks();
    protected abstract SimpleParticleType particle();
    protected abstract ResourceKey<Level> targetLevel();
    protected abstract PoiType poi();
    protected abstract Block defaultPortalFrameBlock();
}