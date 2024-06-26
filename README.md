## QoL API by Ametrin Studios
- [Visit the CurseForge page](https://www.curseforge.com/minecraft/mc-mods/ametrin)
- [How to use](https://github.com/BarionLP/MCModsMaven)

Feel free to join our [Discord Server](https://discord.com/invite/hwA9dd5bVh). We're always here to help!

## Docs
### Custom Boats
- put texture in `assets/{mod_id}/textures/entity/{boat|chest_boat}`
- create and register the custom boat type and items<br>(*id* should be `ResourceLocation(mod_id, boat_type_name)`)
```java
public static final CustomBoatType TEST_BOAT_TYPE = CustomBoatType.builder(id).boatItem(TestItems.TEST_BOAT::get).chestBoatItem(TestItems.TEST_CHEST_BOAT::get).register();
```

```java
public static final RegistryObject<CustomBoatItem> TEST_BOAT = REGISTRY.register("test_boat", ()-> CustomBoatItem.boat(TEST_BOAT_TYPE));
public static final RegistryObject<CustomBoatItem> TEST_CHEST_BOAT = REGISTRY.register("test_chest_boat", ()-> CustomBoatItem.chest(TEST_BOAT_TYPE));
```
- you can create new boat variations, check the `BoatVariants` class

### Data Providers
#### Registering
``DataProviderHelper`` cuts down boilerplate
```java
public static void gatherData(GatherDataEvent event){
        var helper = new DataProviderHelper(event);
        
        helper.add(TestBlockStateProvider::new);
        helper.add(TestItemModelProvider::new);
        helper.add(TestRecipeProvider::new);

        providers.addBlockAndItemTags(TestBlockTagsProvider::new, TestItemTagsProvider::new);

        helper.addLootTables(builder -> builder
                .AddBlockProvider(TestBlockLootSubProvider::new)
                //...
                .AddChestProvider(TestLootTableSubProvider::new));
    }
```

#### ExtendedBlockStateProvider
provides various helper methods<br>
`runProviderRules` automatically generates a matching model for each block
```java
public class TestBlockStateProvider extends ExtendedBlockStateProvider {
    public TestBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TestMod.MOD_ID, existingFileHelper);
        
        //you can exclude blocks and classes from the automation
        excludedClasses.add(SpecialBlock.class);
        excludedBlocks.add(TestBlocks.OTHER_SPECIAL_BLOCK);
    }
    
    @Override
    protected void registerStatesAndModels() {
        runProviderRules(TestBlocks.REGISTRY); // run the providers for all Blocks in TestBlocks.REGISTRY
        // runProviderRules(someIteratorOfBlocks); // you can also provide a custom collection or iterator
    
        someSpecialBlockModel(TestBlocks.SPECIAL_BLOCK); // generate models/states for excluded blocks 
    }
}
```
adding custom provider rules
```java
public TestBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
    //...
    // parameter: block instance, block id, block texture (based on getTextureLocation (see below))
    blockStateProviderRules.add((block, name, texture)->{
        if(/*rule does not apply*/) {return false;}
        // generate model/state
        return true;
    });   
}
```
other customization options
```java
public class TestBlockStateProvider extends ExtendedBlockStateProvider {

    @Override
    protected String getTextureLocation(String name) {
        //modify texture location
        //used when the texture of a block is named differently
        return name;
    }

    @Override
    protected ResourceLocation modBlockLoc(String key) {
        //change where to look for the block textures
        return modLoc("block/" + key);
        //return mcLoc("block/" + key); // would use vanilla textures
    }
}
```

#### ExtendedItemModelProvider
#### ExtendedBlockTagsProvider
#### ExtendedItemTagsProvider
#### ExtendedRecipeProvider
#### LootTableProvider

### Modifying Vanilla Behaviour
The `VanillaHack` class has a lot of helper functions for modifying/overriding vanilla behaviour.<br>
Call during `FMLCommonSetupEvent`.
#### Override Item Food Properties
```java
VanillaHack.overrideFoodProperties(Items.EXAMPLE_ITEM, Foods.EXAMPLE_FOOD);
```