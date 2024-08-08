## QoL API by Ametrin Studios
- [Visit CurseForge page](https://www.curseforge.com/minecraft/mc-mods/ametrin)
- [Visit modrinth page](https://modrinth.com/mod/ametrin)
- [How to use](https://github.com/Ametrin-Studios/maven)

Feel free to join our [Discord Server](https://discord.com/invite/hwA9dd5bVh) in case you got any questions. We're always there to help!

## Docs
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
    
        // don't forget to generate models/states for excluded blocks 
    }
}
```
adding custom provider rules
```java
public TestBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
    //...
    // parameter: block instance, block id, block texture (based on getTextureLocation (see below))
    blockStateProviderRules.add((block, name, texture)->{
        if(/*rule does not apply*/) { return false; }
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
If you want to use the included block model prefabs (e.g. when using `head(...)`) you need to add `'--existing-mod', 'ametrin'` to your data run configuration in your build.gradle
```gradle
programArguments.addAll '--mod', project.mod_id, ..., '--existing-mod', 'ametrin'
```

#### ExtendedItemModelProvider
#### ExtendedBlockTagsProvider
#### ExtendedItemTagsProvider
#### ExtendedRecipeProvider
#### LootTableProviders
#### ExtendedLanguageProvider

### Nether-like Portals
to create a nether-like portal you need:
- a custom portal block (use `PortalBlock`)
- Point of Interest matching the portal block
- a default frame block state (what ever you want)
- a tag matching all valid frame blocks
```java
public static final PortalData TEST_PORTAL = 
        PortalData.builder(Level.NETHER, Level.END)
        .poi(TestPoiTypes.TEST_PORTAL) //a deferred handler or a resource key
        .portal(TestBlocks.TEST_PORTAL) //or ()-> TestBlocks.TEST_PORTAL.get().defaultBlockState()
        .defaultFrame(TestBlocks.TEST_BLOCK) //or ()-> TestBlocks.TEST_BLOCK.get().defaultBlockState()
        .validFrames(TestTags.Blocks.TEST_PORTAL_FRAMES)
        //.transition(...) //NONE by default
        //.particles(...) //optional
        //.sounds(...) //optional
        .build();
```
You'll probably also want a Catalyst item to open the portal -> `PortalCatalystItem`

Note: I'll still have to get rid of the nether spiral when teleporting

### Custom Boats
_The [original custom boat helpers](https://github.com/Ametrin-Studios/Ametrin/tree/1.20.4?tab=readme-ov-file#custom-boats) have been removed_<br>
use [ExtensibleEnums](https://docs.neoforged.net/docs/advanced/extensibleenums/) and `BoatTypeHelper.createProxy`.<br>
use `BoatTypeHelper.getExtensionJson` to generate the extension json string
