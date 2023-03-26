package com.ametrinstudios.ametrin.data.provider;

import com.ametrinstudios.ametrin.data.BlockItemModelProviderRule;
import com.ametrinstudios.ametrin.data.ItemModelProviderRule;
import com.ametrinstudios.ametrin.world.item.CustomHeadBlockItem;
import com.ametrinstudios.ametrin.world.item.ItemNameDoubleHighBlockItem;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Iterator;

import static com.ametrinstudios.ametrin.data.DataProviderExtensions.*;

public abstract class ExtendedItemModelProvider extends ItemModelProvider{
    protected final ModelFile generated = getExistingFile(mcLoc("item/generated"));
    private final ModelFile spawnEgg = getExistingFile(mcLoc("item/template_spawn_egg"));
    protected final ModelFile handheld = getExistingFile(mcLoc("item/handheld"));

    /**
     * Items in this list will be ignored by the generator
     */
    public ArrayList<Item> excludedItems = new ArrayList<>();
    /**
     * Items based on classes in this list will be ignored by the generator
     */
    public ArrayList<Class<? extends Item>> excludedClasses = new ArrayList<>();
    /**
     * {@link  BlockItem}s in this list will use a normal item model instead of the block model
     */
    public ArrayList<BlockItem> useItemModel = new ArrayList<>();
    public ArrayList<BlockItem> useItemModelWithBlockTexture = new ArrayList<>();
    /**
     * those rules get called before the build-in rules
     * you can add custom rules
     */
    public ArrayList<ItemModelProviderRule> priorityItemModelProviderRules = new ArrayList<>();
    /**
     * contains all rules
     * you can add custom rules
     */
    public ArrayList<ItemModelProviderRule> itemModelProviderRules = new ArrayList<>();
    public ArrayList<BlockItemModelProviderRule> blockItemModelProviderRules = new ArrayList<>();

    public ExtendedItemModelProvider(PackOutput output, String modID, ExistingFileHelper existingFileHelper){
        super(output, modID, existingFileHelper);
    }

    {
        blockItemModelProviderRules.add((item, block, name, texture)->{
            if(!(block instanceof WallBlock)) {return false;}
            if(shouldAppendS(name)) {texture = texture.replace("_wall", "s");}
            else if(name.contains("sandstone") && name.contains("smooth")) {texture = texture.replace("wall", "top").replace("smooth_", "");}
            else {texture = texture.replace("_wall", "");}
            wallInventory(name, modBlockLoc(texture));
            return true;
        });
        blockItemModelProviderRules.add((item, block, name, texture)->{
            if(!(block instanceof DoublePlantBlock)) {return false;}
            blockItem(name, texture + "_top");
            return true;
        });
        blockItemModelProviderRules.add((item, block, name, texture)->{
            if(!(block instanceof BushBlock || block instanceof GrowingPlantHeadBlock)) {return false;}
            blockItem(name, texture);
            return true;
        });
        blockItemModelProviderRules.add((item, block, name, texture)->{
            if(!(block instanceof FenceBlock)) {return false;}
            if(isPlank(name)) {texture = texture.replace("fence", "planks");}
            else if(shouldAppendS(name)) {texture = texture.replace("_fence", "s");}
            else {texture = texture.replace("_fence", "");}
            fenceInventory(name, modBlockLoc(texture));
            return true;
        });
        blockItemModelProviderRules.add((item, block, name, texture)->{
            if(!(block instanceof ButtonBlock)) {return false;}
            if(isWooden(name)){
                buttonInventory(name, modBlockLoc(name.replace("button", "planks")));
                return true;
            }

            if (shouldAppendS(name)) {texture = texture.replace("_button", "s");}
            else {texture = texture.replace("_button", "");}
            buttonInventory(name, modBlockLoc(texture));

            return true;
        });
        blockItemModelProviderRules.add((item, block, name, texture)->{
            if(!(block instanceof TrapDoorBlock)) {return false;}
            block(name, name + "_bottom");
            return true;
        });
        blockItemModelProviderRules.add((item, block, name, texture)->{
            if(!(block instanceof CampfireBlock || block instanceof LanternBlock || block instanceof DoorBlock)) {return false;}
            item(name, texture);
            return true;
        });
        blockItemModelProviderRules.add((item, block, name, texture)->{
            if(!(block instanceof TorchBlock)) {return false;}
            blockItem(name, texture);
            return true;
        });



        itemModelProviderRules.add((item, name, texture)-> {
            if(!(item instanceof CustomHeadBlockItem)) {return false;}
            block(name, name + "/0");
            return true;
        });

        itemModelProviderRules.add((item, name, texture)-> {
            if(!(item instanceof SignItem)) {return false;}
            item(name, texture);
            return true;
        });

        itemModelProviderRules.add((item, name, texture)-> {
            if(!(item instanceof DoubleHighBlockItem)) {return false;}
            item(name, texture + "_top");
            return true;
        });

        itemModelProviderRules.add((item, name, texture)-> {
            if(!(item instanceof BlockItem) || (item instanceof ItemNameBlockItem || item instanceof ItemNameDoubleHighBlockItem)) {return false;}
            Block block = ((BlockItem) item).getBlock();

            for(BlockItemModelProviderRule provider : blockItemModelProviderRules){
                if(provider.generate(item, block, name, texture)) {return true;}
            }

            if(useItemModel.contains(item)){
                item(name, texture);
                return true;
            }
            if(useItemModelWithBlockTexture.contains(item)){
                blockItem(name);
                return true;
            }

            block(name);
            return true;
        });

        itemModelProviderRules.add((item, name, texture)-> {
            if(!(item instanceof TieredItem)) {return false;}
            item(name, handheld, texture);
            return true;
        });

        itemModelProviderRules.add((item, name, texture)-> {
            if(!(item instanceof SpawnEggItem)) {return false;}
            getBuilder(name).parent(spawnEgg);
            return true;
        });
    }

    protected void runProviderRules(DeferredRegister<Item> itemRegister){
        runProviderRules(itemRegister.getEntries().stream().map(RegistryObject::get).iterator());
    }
    protected void runProviderRules(Iterator<Item> items){
        items.forEachRemaining(item -> {
            for(Class<?> clazz : excludedClasses){
                if(clazz.isInstance(item)) {return;}
            }
            if(excludedItems.contains(item)) {return;}

            final var name = getItemName(item);
            var texture = getTexture(name);

            for(var provider : priorityItemModelProviderRules){
                if(provider.generate(item, name, texture)) {return;}
            }
            for(var provider : itemModelProviderRules){
                if(provider.generate(item, name, texture)) {return;}
            }

            item(name, texture);
        });
    }

    protected void item(String name, String texture) {item(name, generated, texture);}
    protected void item(String name, ModelFile parent, String texture) {getBuilder(name).parent(parent).texture("layer0", modItemLoc(texture));}

    protected void blockItem(String name) {blockItem(name, name);}
    protected void blockItem(String name, String texture) {blockItem(name, generated, texture);}
    protected void blockItem(String name, ModelFile parent, String texture) {getBuilder(name).parent(parent).texture("layer0", modBlockLoc(texture));}

    protected void block(String name) {block(name, name);}
    protected void block(String name, String parent) {withExistingParent(itemLoc(name), modBlockLoc(parent));}

    protected ResourceLocation modBlockLoc(String key) {return modLoc(blockLoc(key));}
    protected ResourceLocation modItemLoc(String key) {return modLoc(itemLoc(key));}
    protected String itemLoc(String key) {return ITEM_FOLDER + "/" + key;}
    protected String blockLoc(String key) {return BLOCK_FOLDER + "/" + key;}

    protected String getTexture(String name) {return name;}
}