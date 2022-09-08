package com.ametrinstudios.ametrin.datagen;

import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.ametrinstudios.ametrin.AmUtil.*;

public abstract class ExtendedBlockStateProvider extends BlockStateProvider {
    public static ArrayList<Block> exclude = new ArrayList<>();
    public static ArrayList<Type> excludeTypes = new ArrayList<>();
    public static ArrayList<CustomAutomatedBlockStateProvider> customAutomations = new ArrayList<>();
    public static ArrayList<Block> useCutoutRenderType = new ArrayList<>(); //only use when it is unclear (e.g. only some Doors should be cutout) view usages to see where this actually takes affect

    static {
        excludeTypes.add(SignBlock.class); //may be automated in the future;
        excludeTypes.add(BaseFireBlock.class); //may be automated in the future;
        excludeTypes.add(LiquidBlock.class); //may be automated in the future;
    }

    public ExtendedBlockStateProvider(DataGenerator generator, String modID, ExistingFileHelper existingFileHelper) {
        super(generator, modID, existingFileHelper);
    }

    protected <B extends Block> void handleDefaults(List<B> blocks){ //call to automatically generate block models
        blocks.forEach(block -> {
            if(excludeTypes.contains(block.getClass())) {return;}
            if(exclude.contains(block)) {return;}
            final String name = getBlockName(block);
            String texture = getTexture(name);

            for(CustomAutomatedBlockStateProvider provider : customAutomations){
                if(provider.block(block, name, texture)) {return;}
            }

            if(block instanceof StairBlock){
                if(usePlankTexture(name)) {texture = texture.replace("stairs", "planks");}
                else if(shouldAppendS(name)) {texture = texture.replace("_stairs", "s");}
                else {texture = texture.replace("_stairs", "");}
                stairsBlock((StairBlock) block, modBlockLoc(texture));
            }else if(block instanceof SlabBlock){
                if(usePlankTexture(name)) {texture = texture.replace("slab", "planks");}
                else if(shouldAppendS(name)) {texture = texture.replace("_slab", "s");}
                else {texture = texture.replace("_slab", "");}
                slabBlock((SlabBlock) block, modBlockLoc(texture), modBlockLoc(texture));
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
                    axisBlock((RotatedPillarBlock) block, modBlockLoc(texture));
                }
            }else if(block instanceof FenceBlock){
                if(usePlankTexture(name)) {texture = texture.replace("fence", "planks");}
                else {texture = texture.replace("_fence", "");}
                fenceBlock((FenceBlock) block, modBlockLoc(texture));
            }else if(block instanceof FenceGateBlock){
                if(usePlankTexture(name)) {texture = texture.replace("fence_gate", "planks");}
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
                if(useCutoutRenderType.contains(block)){
                    trapdoorBlockWithRenderType((TrapDoorBlock) block, modBlockLoc(name), true, RenderTypes.Cutout);
                } else{
                    trapdoorBlock((TrapDoorBlock) block, modBlockLoc(name), true);
                }
            }else if(block instanceof DoorBlock){
                if(useCutoutRenderType.contains(block)){
                    doorBlockWithRenderType((DoorBlock) block, modBlockLoc(name + "_bottom"), modBlockLoc(name + "_top"), RenderTypes.Cutout);
                } else{
                    doorBlock((DoorBlock) block, modBlockLoc(name + "_bottom"), modBlockLoc(name + "_top"));
                }
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
        getVariantBuilder(block).forAllStatesExcept(blockState -> ConfiguredModel.builder().modelFile(blockState.getValue(CampfireBlock.LIT) ? model : modelOff).rotationY(horizontalDirectionToYAngle(blockState.getValue(CampfireBlock.FACING))).build(), CampfireBlock.WATERLOGGED, CampfireBlock.SIGNAL_FIRE);
    }
    protected void wallTorchBlock(WallTorchBlock block, String name, String texture){
        ModelFile model = models().withExistingParent(name, "block/template_torch_wall").texture("torch", modBlockLoc(texture.replace("_wall", ""))).renderType(RenderTypes.Cutout);
        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(model).rotationY(horizontalDirectionToYAngle(state.getValue(WallTorchBlock.FACING))).build());
    }

    protected static int horizontalDirectionToYAngle(Direction direction){
        return direction == Direction.NORTH ? 270 : direction == Direction.EAST ? 0 : direction == Direction.SOUTH ? 90 : 180;
    }
    protected ResourceLocation modBlockLoc(String texture) {return modLoc("block/" + texture);}

    protected String getTexture(String name){  //allows to modify the texture for a block name
        return name;
    }

    public static class RenderTypes{
        public static final String Cutout = "cutout";
        public static final String Translucent = "translucent";
    }
}
