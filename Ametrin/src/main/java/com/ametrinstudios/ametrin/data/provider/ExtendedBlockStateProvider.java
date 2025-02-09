//package com.ametrinstudios.ametrin.data.provider;
//
//import com.ametrinstudios.ametrin.Ametrin;
//import com.ametrinstudios.ametrin.data.BlockStateProviderRule;
//import com.ametrinstudios.ametrin.world.block.*;
//import net.minecraft.client.data.models.BlockModelGenerators;
//import net.minecraft.client.data.models.ModelProvider;
//import net.minecraft.client.renderer.RenderType;
//import net.minecraft.core.Direction;
//import net.minecraft.data.PackOutput;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.level.block.*;
//import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
//import net.neoforged.neoforge.registries.DeferredRegister;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.function.Supplier;
//
//import static com.ametrinstudios.ametrin.data.DataProviderExtensions.*;
//
//public abstract class ExtendedBlockStateProvider extends BlockModelGenerators {
//    /**
//     * Blocks in this list will be ignored by the generator
//     */
//    public ArrayList<Block> excludedBlocks = new ArrayList<>();
//    /**
//     * blocks based on classes in this list will be ignored by the generator
//     */
//    public ArrayList<Class<? extends Block>> excludedClasses = new ArrayList<>();
//    /**
//     * add custom rules here, gets called before the build-in rules
//     */
//    public ArrayList<BlockStateProviderRule> blockStateProviderRules = new ArrayList<>();
//    /**
//     * define blocks that should use the CutoutRenderType.
//     * Only used in unclear situations (e.g. only some Doors should be cutout) view usages to see where this actually takes affect
//     */
//    public ArrayList<Block> useCutoutRendererType = new ArrayList<>();
//
//    {
//        excludedClasses.add(SignBlock.class); //may be automated in the future;
//        excludedClasses.add(BaseFireBlock.class); //may be automated in the future;
//        excludedClasses.add(AgeableDoublePlantBlock.class);
//    }
//
//    public ExtendedBlockStateProvider() {
//        super();
//    }
//
//    /**
//     * override this if you want to modify the texture
//     * @param name name of the block
//     * @return texture used for the given block
//     */
//    protected String getTextureLocation(String name) {
//        return name;
//    }
//
//    /**
//     * automatically generates block models based on all given {@link #blockStateProviderRules} and some build-in rules
//     * @param blockRegistry mod block registry
//     */
//    protected void runProviderRules(DeferredRegister.Blocks blockRegistry) {
//        runProviderRules(blockRegistry.getEntries().stream().map(Supplier::get).iterator());
//    }
//
//    /**
//     * automatically generates block models based on all given {@link #blockStateProviderRules} and some build-in rules
//     * @param blocks list of all blocks this should run on
//     */
//    protected <B extends Block> void runProviderRules(Iterator<B> blocks) {
//        blocks.forEachRemaining(block -> {
//            for(Class<?> clazz : excludedClasses) {
//                if(clazz.isInstance(block)) { return; }
//            }
//            if(excludedBlocks.contains(block)) { return; }
//
//            final var name = getBlockName(block);
//            var texture = getTextureLocation(name);
//
//            for(var provider : blockStateProviderRules) {
//                if(provider.generate(block, name, texture)) { return; }
//            }
//
//            if(block instanceof StairBlock) {
//                if(isPlank(name)) {texture = texture.replace("stairs", "planks"); }
//                else if(shouldAppendS(name)) {texture = texture.replace("_stairs", "s"); }
//                else {texture = texture.replace("_stairs", ""); }
//                stairsBlock((StairBlock) block, modBlockLoc(texture));
//            }else if(block instanceof SlabBlock) {
//                String doubleSlab = name;
//                if(isPlank(name)) {
//                    texture = texture.replace("slab", "planks");
//                    doubleSlab = doubleSlab.replace("slab", "planks");
//                }
//                else if(shouldAppendS(name)) {
//                    texture = texture.replace("_slab", "s");
//                    doubleSlab = doubleSlab.replace("_slab", "s");
//                }
//                else {
//                    texture = texture.replace("_slab", "");
//                    doubleSlab = doubleSlab.replace("_slab", "");
//                }
//                slabBlock((SlabBlock) block, modBlockLoc(doubleSlab), modBlockLoc(texture));
//            }else if(block instanceof WallBlock) {
//                if(isPlank(name)) {texture = texture.replace("wall", "planks"); }
//                else if(shouldAppendS(name)) {texture = texture.replace("_wall", "s"); }
//                else {texture = texture.replace("_wall", ""); }
//                wallBlock((WallBlock) block, modBlockLoc(texture));
//            }else if(block instanceof RotatedPillarBlock) {
//                if(isLog(name)) {
//                    logBlock((RotatedPillarBlock) block);
//                }else if(isWood(name)) {
//                    texture = texture.replace("wood", "log").replace("hyphae", "stem");
//                    axisBlock((RotatedPillarBlock) block, modBlockLoc(texture), modBlockLoc(texture));
//                }
//            }else if(block instanceof FenceBlock) {
//                if(isPlank(name)) {texture = texture.replace("fence", "planks"); }
//                else if(shouldAppendS(name)) {texture = texture.replace("_fence", "s"); }
//                else {texture = texture.replace("_fence", ""); }
//                fenceBlock((FenceBlock) block, modBlockLoc(texture));
//            }else if(block instanceof FenceGateBlock) {
//                if(isPlank(name)) {texture = texture.replace("fence_gate", "planks"); }
//                else if(shouldAppendS(name)) {texture = texture.replace("_fence_gate", "s"); }
//                else {texture = texture.replace("_fence_gate", ""); }
//                fenceGateBlock((FenceGateBlock) block, modBlockLoc(texture));
//            }else if(block instanceof ButtonBlock) {
//                if(isPlank(name)) {texture = texture.replace("button", "planks"); }
//                else if(shouldAppendS(name)) {texture = texture.replace("_button", "s"); }
//                else {texture = texture.replace("_button", ""); }
//                buttonBlock((ButtonBlock) block, modBlockLoc(texture));
//            }else if(block instanceof PressurePlateBlock) {
//                if(isPlank(name)) {texture = texture.replace("pressure_plate", "planks"); }
//                else if(shouldAppendS(name)) {texture = texture.replace("_pressure_plate", "s"); }
//                else {texture = texture.replace("_pressure_platen", ""); }
//                pressurePlateBlock((PressurePlateBlock) block, modBlockLoc(texture));
//            }else if(block instanceof TrapDoorBlock) {
//                if(useCutoutRendererType.contains(block)) {
//                    trapdoorBlockWithRenderType((TrapDoorBlock) block, modBlockLoc(name), true, RenderType.CUTOUT.name);
//                } else{
//                    trapdoorBlock((TrapDoorBlock) block, modBlockLoc(name), true);
//                }
//            }else if(block instanceof DoorBlock) {
//                if(useCutoutRendererType.contains(block)) {
//                    doorBlockWithRenderType((DoorBlock) block, modBlockLoc(name + "_bottom"), modBlockLoc(name + "_top"), RenderType.CUTOUT.name);
//                } else{
//                    doorBlock((DoorBlock) block, modBlockLoc(name + "_bottom"), modBlockLoc(name + "_top"));
//                }
//            }else if(block instanceof DoublePlantBlock) {
//                var top = models().withExistingParent(name + "_top", "block/tinted_cross").texture("cross", modBlockLoc(texture + "_top")).renderType(RenderType.CUTOUT.name);
//                var bottom = models().withExistingParent(name + "_bottom", "block/tinted_cross").texture("cross", modBlockLoc(texture + "_bottom")).renderType(RenderType.CUTOUT.name);
//                getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER ? bottom : top).build());
//            }else if(block instanceof AgeableBushBlock) {
//                simpleAgeableBushBlock((AgeableBushBlock) block, name, texture);
//            }else if(block instanceof BushBlock || block instanceof GrowingPlantBlock) {
//                simpleBlock(block, models().withExistingParent(name, "block/tinted_cross").texture("cross", modBlockLoc(name)).renderType(RenderType.CUTOUT.name));
//            }else if(block instanceof FlowerPotBlock) {
//                simpleBlock(block, models().withExistingParent(name, "block/flower_pot_cross").texture("plant", modBlockLoc(texture.replace("potted_", ""))));
//            }else if(block instanceof LeavesBlock) {
//                simpleBlock(block, models().withExistingParent(name, "block/leaves").texture("all", modBlockLoc(texture)));
//            }else if(block instanceof LanternBlock) {
//                lanternBlock((LanternBlock) block, name, texture);
//            }else if(block instanceof CampfireBlock) {
//                campfireBlock((CampfireBlock) block, name, texture);
//            }else if(block instanceof WallTorchBlock) {
//                wallTorchBlock((WallTorchBlock) block, name, texture);
//            }else if(block instanceof PortalBlock) {
//                portalBlock((PortalBlock) block, name, texture);
//            }else if(block instanceof TorchBlock) {
//                simpleBlock(block, models().withExistingParent(name, "block/template_torch").texture("torch", modBlockLoc(texture)).renderType(RenderType.CUTOUT.name));
//            }else if(block instanceof LiquidBlock) {
//                simpleBlock(block, models().getBuilder(name).texture("particle", mcBlockLoc("water_still")));
//            }else if(block instanceof CustomHeadBlock) {
//                if(useCutoutRendererType.contains(block)) {
//                    headCutout((CustomHeadBlock) block, name);
//                } else{
//                    head((CustomHeadBlock) block, name);
//                }
//            }else if(block instanceof CustomWallHeadBlock) {
//                if(useCutoutRendererType.contains(block)) {
//                    headCutoutWall((CustomWallHeadBlock) block, name);
//                } else{
//                    headWall((CustomWallHeadBlock) block, name);
//                }
//            }else{
//                simpleBlock(block);
//            }
//        });
//    }
//
//    protected void portalBlock(PortalBlock portal, String name, String texture) {
//        ResourceLocation textureLoc = modBlockLoc(texture);
//        var ew = models().withExistingParent(name + "_ew", "nether_portal_ew").texture("portal", textureLoc).texture("particle", textureLoc).renderType(RenderType.TRANSLUCENT.name);
//        var ns = models().withExistingParent(name + "_ns", "nether_portal_ns").texture("portal", textureLoc).texture("particle", textureLoc).renderType(RenderType.TRANSLUCENT.name);
//        getVariantBuilder(portal).forAllStates(state -> ConfiguredModel.builder().modelFile(state.getValue(PortalBlock.AXIS) == Direction.Axis.X ? ns : ew).build());
//    }
//
//    protected void lanternBlock(LanternBlock lantern, String name, String texture) {
//        var model = models().withExistingParent(name, "block/template_lantern").texture("lantern", modBlockLoc(texture)).renderType(RenderType.CUTOUT.name);
//        var modelHanging = models().withExistingParent(name + "_hanging", "block/template_hanging_lantern").texture("lantern", modBlockLoc(texture)).renderType(RenderType.CUTOUT.name);
//        getVariantBuilder(lantern).forAllStatesExcept(state -> ConfiguredModel.builder().modelFile(state.getValue(LanternBlock.HANGING) ? modelHanging : model).build(), LanternBlock.WATERLOGGED);
//    }
//    protected void campfireBlock(CampfireBlock campfire, String name, String texture) {
//        var model = models().withExistingParent(name, "block/template_campfire").texture("fire", modBlockLoc(texture + "_fire")).texture("lit_log", modBlockLoc(texture + "_log_lit")).renderType(RenderType.CUTOUT.name);
//        var modelOff = models().getExistingFile(ResourceLocation.withDefaultNamespace("block/campfire_off"));
//        getVariantBuilder(campfire).forAllStatesExcept(blockState -> ConfiguredModel.builder().modelFile(blockState.getValue(CampfireBlock.LIT) ? model : modelOff).rotationY(horizontalDirectionToYAngleForCampfire(blockState.getValue(CampfireBlock.FACING))).build(), CampfireBlock.WATERLOGGED, CampfireBlock.SIGNAL_FIRE);
//    }
//    protected void wallTorchBlock(WallTorchBlock wallTorch, String name, String texture) {
//        var model = models().withExistingParent(name, "block/template_torch_wall").texture("torch", modBlockLoc(texture.replace("_wall", ""))).renderType(RenderType.CUTOUT.name);
//        getVariantBuilder(wallTorch).forAllStates(state -> ConfiguredModel.builder().modelFile(model).rotationY(horizontalDirectionToYAngleForWallTorch(state.getValue(WallTorchBlock.FACING))).build());
//    }
//
//    protected void head(CustomHeadBlock head, String name) {
//        getVariantBuilder(head).forAllStatesExcept(state -> {
//            int rotation = state.getValue(CustomHeadBlock.ROTATION);
//            var model = models().withExistingParent( "block/" + name + "/" + rotation, Ametrin.locate("block/head/" + rotation)).texture("texture", modBlockLoc(name));
//            return ConfiguredModel.builder().modelFile(model).build();
//        }, AbstractHeadBlock.WATERLOGGED);
//    }
//    protected void headWall(CustomWallHeadBlock wallHead, String name) {
//        var model = models().withExistingParent( "block/" + name, Ametrin.locate("block/head_wall")).texture("texture", modBlockLoc(name.replace("_wall", "")));
//        getVariantBuilder(wallHead).forAllStatesExcept(state -> ConfiguredModel.builder().modelFile(model).rotationY(horizontalDirectionToYAngle(state.getValue(CustomWallHeadBlock.FACING))).build(), AbstractHeadBlock.WATERLOGGED);
//    }
//    protected void headCutout(CustomHeadBlock head, String name) {
//        getVariantBuilder(head).forAllStatesExcept(state -> {
//            int rotation = state.getValue(CustomHeadBlock.ROTATION);
//            var model = models().withExistingParent( "block/" + name + "/" + rotation, Ametrin.locate("block/head/cutout/" + rotation)).texture("texture", modBlockLoc(name)).renderType(RenderType.CUTOUT.name);
//            return ConfiguredModel.builder().modelFile(model).build();
//        }, AbstractHeadBlock.WATERLOGGED);
//    }
//    protected void headCutoutWall(CustomWallHeadBlock wallHead, String name) {
//        var model = models().withExistingParent( "block/" + name, Ametrin.locate("block/head_wall_cutout")).texture("texture", modBlockLoc(name.replace("_wall", ""))).renderType(RenderType.CUTOUT.name);
//        getVariantBuilder(wallHead).forAllStatesExcept(state -> ConfiguredModel.builder().modelFile(model).rotationY(horizontalDirectionToYAngle(state.getValue(CustomWallHeadBlock.FACING))).build(), AbstractHeadBlock.WATERLOGGED);
//    }
//
//    protected void simpleAgeableBushBlock(AgeableBushBlock bush, String name, String texture) {
//        var Age0 = models().cross("block/" + name + "/stage0", mcLoc("block/sweet_berry_bush_stage0")).renderType(RenderType.CUTOUT.name);
//        var Age1 = models().cross("block/" + name + "/stage1", mcLoc("block/sweet_berry_bush_stage1")).renderType(RenderType.CUTOUT.name);
//        var Age2 = models().cross("block/" + name + "/stage2", modBlockLoc(texture + "/stage2")).renderType(RenderType.CUTOUT.name);
//        var Age3 = models().cross("block/" + name + "/stage3", modBlockLoc(texture + "/stage3")).renderType(RenderType.CUTOUT.name);
//        getVariantBuilder(bush).forAllStates(state -> {
//            final int age = state.getValue(IAgeablePlant.AGE);
//            return ConfiguredModel.builder().modelFile((age == 0) ? Age0 : (age == 1) ? Age1 : (age == 2) ? Age2 : Age3).build();
//        });
//    }
//    protected void ageableBushBlock(AgeableBushBlock bush, String name, String texture) {
//        var Age0 = models().cross("block/" + name + "/stage0", modBlockLoc(texture + "/stage0")).renderType(RenderType.CUTOUT.name);
//        var Age1 = models().cross("block/" + name + "/stage1", modBlockLoc(texture + "/stage1")).renderType(RenderType.CUTOUT.name);
//        var Age2 = models().cross("block/" + name + "/stage2", modBlockLoc(texture + "/stage2")).renderType(RenderType.CUTOUT.name);
//        var Age3 = models().cross("block/" + name + "/stage3", modBlockLoc(texture + "/stage3")).renderType(RenderType.CUTOUT.name);
//        getVariantBuilder(bush).forAllStates(state -> {
//            final int age = state.getValue(IAgeablePlant.AGE);
//            return ConfiguredModel.builder().modelFile((age == 0) ? Age0 : (age == 1) ? Age1 : (age == 2) ? Age2 : Age3).build();
//        });
//    }
//
//    /**
//     * converts a horizontal direction to the corresponding Y angle
//     * @param direction horizontal direction
//     * @return y angle of the direction
//     */
//    protected static int horizontalDirectionToYAngle(Direction direction) {
//        return direction == Direction.NORTH ? 0 : direction == Direction.EAST ? 90 : direction == Direction.SOUTH ? 180 : 270;
//    }
//
//    /**
//     * converts a horizontal direction to a wall torch specific Y angle use {@link #horizontalDirectionToYAngle(Direction)} for a standardised method
//     * @param direction horizontal direction
//     * @return y angle of the direction
//     */
//    protected static int horizontalDirectionToYAngleForWallTorch(Direction direction) {
//        return direction == Direction.NORTH ? 270 : direction == Direction.EAST ? 0 : direction == Direction.SOUTH ? 90 : 180;
//    }
//    /**
//     * converts a horizontal direction to a campfire specific Y angle use {@link #horizontalDirectionToYAngle(Direction)} for a standardised method
//     * @param direction horizontal direction
//     * @return y angle of the direction
//     */
//    protected static int horizontalDirectionToYAngleForCampfire(Direction direction) {
//        return direction == Direction.NORTH ? 270 : direction == Direction.EAST ? 0 : direction == Direction.SOUTH ? 90 : 180;
//    }
//
//    protected ResourceLocation modBlockLoc(String key) { return modLoc("block/" + key); }
//    protected ResourceLocation mcBlockLoc(String key) { return mcLoc("block/" + key); }
//}
