package com.ametrinstudios.ametrin.datagen;

import com.ametrinstudios.ametrin.world.block.*;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

import static com.ametrinstudios.ametrin.AmetrinUtil.*;

public abstract class ExtendedBlockStateProvider extends BlockStateProvider {
    /**
     * Blocks in this list will be ignored by the generator
     */
    public ArrayList<Block> excludedBlocks = new ArrayList<>();
    /**
     * blocks based on classes in this list will be ignored by the generator
     */
    public ArrayList<Class<? extends Block>> excludedClasses = new ArrayList<>();
    /**
     * add custom rules here, gets called before the build-in rules
     */
    public ArrayList<BlockStateProviderRule> blockStateProviderRules = new ArrayList<>();
    /**
     * define blocks that should use the CutoutRenderType.
     * Only used in unclear situations (e.g. only some Doors should be cutout) view usages to see where this actually takes affect
     */
    public ArrayList<Block> useCutoutRendererType = new ArrayList<>();

    {
        excludedClasses.add(SignBlock.class); //may be automated in the future;
        excludedClasses.add(BaseFireBlock.class); //may be automated in the future;
        excludedClasses.add(LiquidBlock.class); //may be automated in the future;
        excludedClasses.add(AgeableDoublePlantBlock.class);
    }

    public ExtendedBlockStateProvider(DataGenerator generator, String modID, ExistingFileHelper existingFileHelper) {
        super(generator, modID, existingFileHelper);
    }

    /**
     * override this if you want to modify the texture
     * @param name name of the block
     * @return texture used for the given block
     */
    protected String getTexture(String name) {
        return name;
    }

    /**
     * automatically generates block models based on all given {@link #blockStateProviderRules} and some build-in rules
     * @param blockRegistry mod block registry
     */
    protected void runProviderRules(DeferredRegister<Block> blockRegistry){
        runProviderRules(blockRegistry.getEntries().stream().map(RegistryObject::get).toList());
    }

    /**
     * automatically generates block models based on all given {@link #blockStateProviderRules} and some build-in rules
     * @param blocks list of all blocks this should run on
     */
    protected <B extends Block> void runProviderRules(List<B> blocks){
        blocks.forEach(block -> {
            for(Class<?> clazz : excludedClasses){
                if(clazz.isInstance(block)) {return;}
            }
            if(excludedBlocks.contains(block)) {return;}
            final String name = getBlockName(block);
            String texture = getTexture(name);

            for(BlockStateProviderRule provider : blockStateProviderRules){
                if(provider.generate(block, name, texture)) {return;}
            }

            if(block instanceof StairBlock){
                if(usePlankTexture(name)) {texture = texture.replace("stairs", "planks");}
                else if(shouldAppendS(name)) {texture = texture.replace("_stairs", "s");}
                else {texture = texture.replace("_stairs", "");}
                stairsBlock((StairBlock) block, modBlockLoc(texture));
            }else if(block instanceof SlabBlock){
                String doubleSlab = name;
                if(usePlankTexture(name)) {
                    texture = texture.replace("slab", "planks");
                    doubleSlab = doubleSlab.replace("slab", "planks");
                }
                else if(shouldAppendS(name)) {
                    texture = texture.replace("_slab", "s");
                    doubleSlab = doubleSlab.replace("_slab", "s");
                }
                else {
                    texture = texture.replace("_slab", "");
                    doubleSlab = doubleSlab.replace("_slab", "");
                }
                slabBlock((SlabBlock) block, modBlockLoc(doubleSlab), modBlockLoc(texture));
            }else if(block instanceof WallBlock){
                if(usePlankTexture(name)) {texture = texture.replace("wall", "planks");}
                else if(shouldAppendS(name)) {texture = texture.replace("_wall", "s");}
                else {texture = texture.replace("_wall", "");}
                wallBlock((WallBlock) block, modBlockLoc(texture));
            }else if(block instanceof RotatedPillarBlock){
                if(isLog(name)){
                    logBlock((RotatedPillarBlock) block);
                }else if(isWood(name)){
                    texture = texture.replace("wood", "log").replace("hyphae", "stem");
                    axisBlock((RotatedPillarBlock) block, modBlockLoc(texture), modBlockLoc(texture));
                }
            }else if(block instanceof FenceBlock){
                if(usePlankTexture(name)) {texture = texture.replace("fence", "planks");}
                else if(shouldAppendS(name)) {texture = texture.replace("_fence", "s");}
                else {texture = texture.replace("_fence", "");}
                fenceBlock((FenceBlock) block, modBlockLoc(texture));
            }else if(block instanceof FenceGateBlock){
                if(usePlankTexture(name)) {texture = texture.replace("fence_gate", "planks");}
                else if(shouldAppendS(name)) {texture = texture.replace("_fence_gate", "s");}
                else {texture = texture.replace("_fence_gate", "");}
                fenceGateBlock((FenceGateBlock) block, modBlockLoc(texture));
            }else if(block instanceof ButtonBlock){
                if(usePlankTexture(name)) {texture = texture.replace("button", "planks");}
                else if(shouldAppendS(name)) {texture = texture.replace("_button", "s");}
                else {texture = texture.replace("_button", "");}
                buttonBlock((ButtonBlock) block, modBlockLoc(texture));
            }else if(block instanceof PressurePlateBlock){
                if(usePlankTexture(name)) {texture = texture.replace("pressure_plate", "planks");}
                else if(shouldAppendS(name)) {texture = texture.replace("_pressure_plate", "s");}
                else {texture = texture.replace("_pressure_platen", "");}
                pressurePlateBlock((PressurePlateBlock) block, modBlockLoc(texture));
            }else if(block instanceof TrapDoorBlock){
                if(useCutoutRendererType.contains(block)){
                    trapdoorBlockWithRenderType((TrapDoorBlock) block, modBlockLoc(name), true, RenderTypes.Cutout);
                } else{
                    trapdoorBlock((TrapDoorBlock) block, modBlockLoc(name), true);
                }
            }else if(block instanceof DoorBlock){
                if(useCutoutRendererType.contains(block)){
                    doorBlockWithRenderType((DoorBlock) block, modBlockLoc(name + "_bottom"), modBlockLoc(name + "_top"), RenderTypes.Cutout);
                } else{
                    doorBlock((DoorBlock) block, modBlockLoc(name + "_bottom"), modBlockLoc(name + "_top"));
                }
            }else if(block instanceof DoublePlantBlock){
                ModelFile top = models().withExistingParent(name + "_top", "block/tinted_cross").texture("cross", modBlockLoc(texture + "_top")).renderType(RenderTypes.Cutout);
                ModelFile bottom = models().withExistingParent(name + "_bottom", "block/tinted_cross").texture("cross", modBlockLoc(texture + "_bottom")).renderType(RenderTypes.Cutout);
                getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER ? bottom : top).build());
            }else if(block instanceof AgeableBushBlock){
                simpleGrowableBushBlock((AgeableBushBlock) block, name, texture);
            }else if(block instanceof BushBlock || block instanceof GrowingPlantBlock){
                simpleBlock(block, models().withExistingParent(name, "block/tinted_cross").texture("cross", modBlockLoc(name)).renderType(RenderTypes.Cutout));
            }else if(block instanceof FlowerPotBlock){
                simpleBlock(block, models().withExistingParent(name, "block/flower_pot_cross").texture("plant", modBlockLoc(texture.replace("potted_", ""))));
            }else if(block instanceof LeavesBlock) {
                simpleBlock(block, models().withExistingParent(name, "block/leaves").texture("all", modBlockLoc(texture)));
            }else if(block instanceof LanternBlock) {
                lanternBlock((LanternBlock) block, name, texture);
            }else if(block instanceof CampfireBlock) {
                campfireBlock((CampfireBlock) block, name, texture);
            }else if(block instanceof WallTorchBlock) {
                wallTorchBlock((WallTorchBlock) block, name, texture);
            }else if(block instanceof TorchBlock) {
                simpleBlock(block, models().withExistingParent(name, "block/template_torch").texture("torch", modBlockLoc(texture)).renderType(RenderTypes.Cutout));
            }else if(block instanceof CustomHeadBlock){
                if(useCutoutRendererType.contains(block)){
                    headCutout((CustomHeadBlock) block, name);
                } else{
                    head((CustomHeadBlock) block, name);
                }
            }else if(block instanceof CustomWallHeadBlock){
                if(useCutoutRendererType.contains(block)){
                    headCutoutWall((CustomWallHeadBlock) block, name);
                } else{
                    headWall((CustomWallHeadBlock) block, name);
                }
            }else{
                simpleBlock(block);
            }
        });
    }

    protected void lanternBlock(LanternBlock block, String name, String texture){
        ModelFile model = models().withExistingParent(name, "block/template_lantern").texture("lantern", modBlockLoc(texture)).renderType(RenderTypes.Cutout);
        ModelFile modelHanging = models().withExistingParent(name + "_hanging", "block/template_hanging_lantern").texture("lantern", modBlockLoc(texture)).renderType(RenderTypes.Cutout);
        getVariantBuilder(block).forAllStatesExcept(state -> ConfiguredModel.builder().modelFile(state.getValue(LanternBlock.HANGING) ? modelHanging : model).build(), LanternBlock.WATERLOGGED);
    }
    protected void campfireBlock(CampfireBlock block, String name, String texture){
        ModelFile model = models().withExistingParent(name, "block/template_campfire").texture("fire", modBlockLoc(texture + "_fire")).texture("lit_log", modBlockLoc(texture + "_log_lit")).renderType(RenderTypes.Cutout);
        ModelFile modelOff = models().getExistingFile(new ResourceLocation("block/campfire_off"));
        getVariantBuilder(block).forAllStatesExcept(blockState -> ConfiguredModel.builder().modelFile(blockState.getValue(CampfireBlock.LIT) ? model : modelOff).rotationY(horizontalDirectionToYAngleForCampfire(blockState.getValue(CampfireBlock.FACING))).build(), CampfireBlock.WATERLOGGED, CampfireBlock.SIGNAL_FIRE);
    }
    protected void wallTorchBlock(WallTorchBlock block, String name, String texture){
        ModelFile model = models().withExistingParent(name, "block/template_torch_wall").texture("torch", modBlockLoc(texture.replace("_wall", ""))).renderType(RenderTypes.Cutout);
        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(model).rotationY(horizontalDirectionToYAngleForWallTorch(state.getValue(WallTorchBlock.FACING))).build());
    }

    protected void head(CustomHeadBlock block, String name){
        getVariantBuilder(block).forAllStatesExcept(state -> {
            int rotation = state.getValue(CustomHeadBlock.Rotation);
            ModelFile model = models().withExistingParent( "block/" + name + "/" + rotation, mcLoc("block/head/" + rotation)).texture("texture", modBlockLoc(name));
            return ConfiguredModel.builder().modelFile(model).build();
        }, AmAbstractHeadBlock.Waterlogged);
    }
    protected void headWall(CustomWallHeadBlock block, String name){
        ModelFile model = models().withExistingParent( "block/" + name, mcLoc("block/head_wall")).texture("texture", modBlockLoc(name.replace("_wall", ""))).renderType(RenderTypes.Cutout);
        getVariantBuilder(block).forAllStatesExcept(state -> ConfiguredModel.builder().modelFile(model).rotationY(horizontalDirectionToYAngle(state.getValue(CustomWallHeadBlock.Facing))).build(), AmAbstractHeadBlock.Waterlogged);
    }
    protected void headCutout(CustomHeadBlock block, String name){
        getVariantBuilder(block).forAllStatesExcept(state -> {
            int rotation = state.getValue(CustomHeadBlock.Rotation);
            ModelFile model = models().withExistingParent( "block/" + name + "/" + rotation, mcLoc("block/head/cutout/" + rotation)).texture("texture", modBlockLoc(name)).renderType(RenderTypes.Cutout);
            return ConfiguredModel.builder().modelFile(model).build();
        }, AmAbstractHeadBlock.Waterlogged);
    }
    protected void headCutoutWall(CustomWallHeadBlock block, String name){
        ModelFile model = models().withExistingParent( "block/" + name, mcLoc("block/head_wall_cutout")).texture("texture", modBlockLoc(name.replace("_wall", ""))).renderType(RenderTypes.Cutout);
        getVariantBuilder(block).forAllStatesExcept(state -> ConfiguredModel.builder().modelFile(model).rotationY(horizontalDirectionToYAngle(state.getValue(CustomWallHeadBlock.Facing))).build(), AmAbstractHeadBlock.Waterlogged);
    }

    protected void simpleGrowableBushBlock(AgeableBushBlock bush, String name, String texture) {
        ModelFile Age0 = models().cross("block/" + name + "/stage0", mcLoc("block/sweet_berry_bush_stage0")).renderType(RenderTypes.Cutout);
        ModelFile Age1 = models().cross("block/" + name + "/stage1", mcLoc("block/sweet_berry_bush_stage1")).renderType(RenderTypes.Cutout);
        ModelFile Age2 = models().cross("block/" + name + "/stage2", modBlockLoc(texture + "/stage2")).renderType(RenderTypes.Cutout);
        ModelFile Age3 = models().cross("block/" + name + "/stage3", modBlockLoc(texture + "/stage3")).renderType(RenderTypes.Cutout);
        getVariantBuilder(bush).forAllStates(state -> {
            final int age = state.getValue(IAgeablePlant.Age);
            return ConfiguredModel.builder().modelFile((age == 0) ? Age0 : (age == 1) ? Age1 : (age == 2) ? Age2 : Age3).build();
        });
    }
    protected void growableBushBlock(AgeableBushBlock bush, String name, String texture) {
        ModelFile Age0 = models().cross("block/" + name + "/stage0", modBlockLoc(texture + "/stage0")).renderType(RenderTypes.Cutout);
        ModelFile Age1 = models().cross("block/" + name + "/stage1", modBlockLoc(texture + "/stage1")).renderType(RenderTypes.Cutout);
        ModelFile Age2 = models().cross("block/" + name + "/stage2", modBlockLoc(texture + "/stage2")).renderType(RenderTypes.Cutout);
        ModelFile Age3 = models().cross("block/" + name + "/stage3", modBlockLoc(texture + "/stage3")).renderType(RenderTypes.Cutout);
        getVariantBuilder(bush).forAllStates(state -> {
            final int age = state.getValue(IAgeablePlant.Age);
            return ConfiguredModel.builder().modelFile((age == 0) ? Age0 : (age == 1) ? Age1 : (age == 2) ? Age2 : Age3).build();
        });
    }

    /**
     * converts a horizontal direction to the corresponding Y angle
     * @param direction horizontal direction
     * @return y angle of the direction
     */
    protected static int horizontalDirectionToYAngle(Direction direction){
        return direction == Direction.NORTH ? 0 : direction == Direction.EAST ? 90 : direction == Direction.SOUTH ? 180 : 270;
    }

    /**
     * converts a horizontal direction to a wall torch specific Y angle use {@link #horizontalDirectionToYAngle(Direction)} for a standardised method
     * @param direction horizontal direction
     * @return y angle of the direction
     */
    protected static int horizontalDirectionToYAngleForWallTorch(Direction direction){
        return direction == Direction.NORTH ? 270 : direction == Direction.EAST ? 0 : direction == Direction.SOUTH ? 90 : 180;
    }
    /**
     * converts a horizontal direction to a campfire specific Y angle use {@link #horizontalDirectionToYAngle(Direction)} for a standardised method
     * @param direction horizontal direction
     * @return y angle of the direction
     */
    protected static int horizontalDirectionToYAngleForCampfire(Direction direction){
        return direction == Direction.NORTH ? 270 : direction == Direction.EAST ? 0 : direction == Direction.SOUTH ? 90 : 180;
    }

    protected ResourceLocation modBlockLoc(String key) {return modLoc("block/" + key);}

    public static class RenderTypes{
        public static final String Cutout = "cutout";
        public static final String Translucent = "translucent";
    }
}
