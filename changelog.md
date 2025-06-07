## 0.3.0
* 1.21.5 port
* disabled some tag rules
  * they are now longer as simple as they were

## 0.2.9
* removed some `BlockRegisterHelper` methods
  * added replacements to be compatible with new registry behavior
* various improvements & fixes

## 0.2.8 - beta
* 1.21.4 port
* added `ExtendedModelProvider`
* no auto gen for block / item models for now
* still missing some features from `ExtendedItemModelProvider` and `ExtendedBlockStateProvider`

## 0.2.4
* rewrote Portal API
* enabled update checker
* disabled Fluid API (will be rewritten #4)
* fixed `ExtendedRecipeProvider.dying` saving to the wrong namespace
* fixed `ExtendedBlockStateProvider.head` failing to find head model

## 0.2.3 - beta
* added `BoatTypeHelper`
* qol improvements & refactoring

## 0.2.2 - alpha
* added `ArmorMaterialBuilder`
* added `VanillaCompat.addFlowerPot` overload 
* re-added `BlockRegisterHelper.sapling`
* renamed `Extensions` to `ChunkPosHelper` (breaking)
* added `TimeHelper.hoursToTicks`
* added provider rule for armor tags
* removed custom boat stuff in favor of [EnumExtensions](https://docs.neoforged.net/docs/advanced/extensibleenums/) (breaking)
* `BlockRegisterHelper` rethinking (breaking)

## 0.2.1 - alpha
* reworked `VanillaCompat` class

## 0.2.0 - alpha
* split `Extensions` into `ColorHelper` and `TimeHelper` (breaking)
* disabled Armor API (will be reworked #3)
* 1.21 port

## 0.1.9 - beta
* added `ExtendedLanguageProvider`

## 0.1.8
* added DataProviderHelper
* fixes

## 0.1.7 - alpha
* ported to NeoForge

## 0.1.6 - beta
* ported to 1.20.4
* disabled tree api (incompatible)

## 0.1.5 - beta
* fixed Boat API

## 0.1.4 - beta
* removed ColorSpawnEggItem class (use `ItemRegisterHelper.spawnEgg`)
* fixed Strippable API
* updated Flammable API
* clean up

## 0.1.3
* 1.20.2 port

## 0.1.2 - alpha
* fixes

## 0.1.1 - alpha
* added `ExtendedItemTagsProvider`
* fixes

## 0.1.0 - alpha
* ported to 1.20
* removed deprecated stuff

## 0.0.39 - alpha
* fixed `CustomTreeFeature` constructor
* added docs to build

## 0.0.38 - alpha
* overhauled custom Tree API
* ExperimentalTerrainAnalyzer is now properly implemented
* old TerrainAnalyzer methods are now deprecated

## 0.0.37 - alpha
* Boat API clean up
* added `ItemRegisterHelper`

## 0.0.36 - alpha
* added Boat API

## 0.0.35
* added LootTableProviderHelper#tag()

## 0.0.34
* more `BlockBehaviourPropertiesHelper` methods
* fenceGate helper for non-wooden fence gates

## 0.0.33 - alpha
* new `LootTableProvider` System
* refactoring

## 0.0.32 - alpha
* ported to 1.19.4

## 0.0.31 - alpha
* added pressurePlate helper methods
* removed flammable stuff

## 0.0.30 - alpha
* added fenceGate, door and trapDoor to `BlockRegistry`
* added `ExtendedEntityLootSubProvider`
* extended `ExtendedBlockLootSubProvider`
* extended `BlockRegistry` button helper methods
* fixed `ExtendedBlockTagsProvider`
* optimisations & refactoring

## 0.0.29 - alpha
* more DataProvider fixes

## 0.0.28 - alpha
* fixed DataProviders
* bunch of VanillaCompat helper methods
* `StructurePieces` now use Weighted List
* deprecated FlammableBlocks

## 0.0.27 - alpha
* added `flammableStair()` to BlockRegistry
* added `hasEnchantment()`
* added helper methods for buttons to `BlockRegistry`
* removed deprecated methods
* ported to 1.19.3

## 0.0.26 - alpha
* added `getApplicableSlots()` to `Enchantment`
* added more flammable blocks
* renamed `FlammableRotatedPillarBlock` to `FlammableLogBlock`
* `LiquidBlock`s now generate an empty model with particle texture
* fixes mixins

## 0.0.25 - alpha
* added `FlammableBlock` and default implementations for Leaves, Planks and Logs/Woods
* fixed flammability for some blocks
* fixed `addFlowerPot()`
* fixed mixins
* deprecated a lot of unnecessary `BlockRegistry` helper methods

## 0.0.24 - alpha
* added `LockedSlot`
* added helper methods for smoking recipes
* added helper method for Flower Pots

## 0.0.23 - alpha
* added `ColorToIntWithAlpha`
* added `SimpleFluidType` and `CustomFluidType`
* added `stoneButton()` to `BlockRegistry`

## 0.0.22 - alpha
* added proper build method to `StructurePieces.Builder`
* added method for copying `BlockBehaviour.Properties` (experimental)

## 0.0.21 - alpha
* added `addCompostable`
* `xpDroppingBlock` now takes an `IntProvider`
* added base for `ExtendedLootTableProvider`
* added `StructurePieces` (updated from DungeonsEnhanced `DEStructurePiece`)
* `PortalCatalystItem` now has a description

## 0.0.20 - alpha
* added helper methods for `AxeItem#STRIPPABLES` and `PotionBrewing#addMix`
* `DataGenerator`s now use `Iterator`s (potentially breaking)

## 0.0.19 - alpha
* added block registry helper methods
* added CustomTree API
* added `TerrainAnalyzer`

## 0.0.18 - alpha
* fixed `PortalBlock` model generator

## 0.0.17 - alpha
* added methods for converting `ChunkPos` to `BlockPos`
* added Amterin Portal API (thanks to KaupenJoe and The Undergarden mod)
* added `blockItem()` model provider with custom model
* fix `ExternalEffect` constructor was private (however that happened)
