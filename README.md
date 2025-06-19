## QoL API by Ametrin Studios
- [Visit CurseForge page](https://www.curseforge.com/minecraft/mc-mods/ametrin)
- [Visit modrinth page](https://modrinth.com/mod/ametrin)
- [How to add to your project](https://github.com/Ametrin-Studios/maven)

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

    helper.addBlockAndItemTags(TestBlockTagsProvider::new, TestItemTagsProvider::new);

    helper.addLootTables(builder -> builder
        .AddBlockProvider(TestBlockLootSubProvider::new)
        //...
        .AddChestProvider(TestLootTableSubProvider::new));
}
```

#### ExtendedModelProvider
provides various helper methods
If you want to use the included block model templates (e.g. when using `head(...)`) you need to add `'--existing-mod', 'ametrin'` to your data run configuration in your build.gradle
```gradle
programArguments.addAll '--mod', project.mod_id, ..., '--existing-mod', 'ametrin'
```

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
