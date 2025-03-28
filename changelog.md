## 0.2.9 (RELEASE)
* removed some `BlockRegisterHelper` methods
  * added replacements to be compatible with new registry behavior
* various improvements & fixes

## 0.2.8 (BETA)
* 1.21.4 port
* added `ExtendedModelProvider`
* no auto gen for block / item models for now
* still missing some features from `ExtendedItemModelProvider` and `ExtendedBlockStateProvider`

## 0.2.4 (BETA)
* rewrote Portal API
* enabled update checker
* disabled Fluid API (will be rewritten #4)
* fixed `ExtendedRecipeProvider.dying` saving to the wrong namespace
* fixed `ExtendedBlockStateProvider.head` failing to find head model

## 0.2.3 (BETA)
* added `BoatTypeHelper`
* qol improvements & refactoring

## 0.2.2 (ALPHA)
* added `ArmorMaterialBuilder`
* added `VanillaCompat.addFlowerPot` overload 
* re-added `BlockRegisterHelper.sapling`
* renamed `Extensions` to `ChunkPosHelper` (breaking)
* added `TimeHelper.hoursToTicks`
* added provider rule for armor tags
* removed custom boat stuff in favor of [EnumExtensions](https://docs.neoforged.net/docs/advanced/extensibleenums/) (breaking)
* `BlockRegisterHelper` rethinking (breaking)

## 0.2.1 (ALPHA)
* reworked `VanillaCompat` class

## 0.2.0 (ALPHA)
* split `Extensions` into `ColorHelper` and `TimeHelper` (breaking)
* disabled Armor API (will be reworked #3)
* 1.21 port

## 0.1.9 (BETA)
* added `ExtendedLanguageProvider`

## 0.1.8 (BETA)
* added DataProviderHelper
* fixes

## 0.1.7 (ALPHA)
* ported to NeoForge

## 0.1.6 (BETA)
* ported to 1.20.4
* disabled tree api (incompatible)

## 0.1.5 (BETA)
* fixed Boat API

## 0.1.4 (BETA)
* removed ColorSpawnEggItem class (use `ItemRegisterHelper.spawnEgg`)
* fixed Strippable API
* updated Flammable API
* clean up

## 0.1.3 (BETA)
* 1.20.2 port

## 0.1.2 (ALPHA)
* fixes

## 0.1.1 (ALPHA)
* added `ExtendedItemTagsProvider`
* fixes

## 0.1.0 (ALPHA)
* ported to 1.20
* removed deprecated stuff

## 0.0.39 (ALPHA)
* fixed `CustomTreeFeature` constructor
* added docs to build

## 0.0.38 (ALPHA)
* overhauled custom Tree API
* ExperimentalTerrainAnalyzer is now properly implemented
* old TerrainAnalyzer methods are now deprecated

## 0.0.37 (ALPHA)
* Boat API clean up
* added `ItemRegisterHelper`

## 0.0.36 (ALPHA)
* added Boat API

## 0.0.35 (BETA)
* added LootTableProviderHelper#tag()

## 0.0.34 (BETA)
* more `BlockBehaviourPropertiesHelper` methods
* fenceGate helper for non-wooden fence gates

## 0.0.33 (ALPHA)
* new `LootTableProvider` System
* refactoring

## 0.0.32 (ALPHA)
* ported to 1.19.4

## 0.0.31 (ALPHA)
* added pressurePlate helper methods
* removed flammable stuff

## 0.0.30 (ALPHA)
* added fenceGate, door and trapDoor to `BlockRegistry`
* added `ExtendedEntityLootSubProvider`
* extended `ExtendedBlockLootSubProvider`
* extended `BlockRegistry` button helper methods
* fixed `ExtendedBlockTagsProvider`
* optimisations & refactoring

## 0.0.29 (ALPHA)
* more DataProvider fixes

## 0.0.28 (ALPHA)
* fixed DataProviders
* bunch of VanillaCompat helper methods
* `StructurePieces` now use Weighted List
* deprecated FlammableBlocks

## 0.0.27 (ALPHA)
* added `flammableStair()` to BlockRegistry
* added `hasEnchantment()`
* added helper methods for buttons to `BlockRegistry`
* removed deprecated methods
* ported to 1.19.3

## 0.0.26 (ALPHA)
* added `getApplicableSlots()` to `Enchantment`
* added more flammable blocks
* renamed `FlammableRotatedPillarBlock` to `FlammableLogBlock`
* `LiquidBlock`s now generate an empty model with particle texture
* fixes mixins

## 0.0.25 (ALPHA)
* added `FlammableBlock` and default implementations for Leaves, Planks and Logs/Woods
* fixed flammability for some blocks
* fixed `addFlowerPot()`
* fixed mixins
* deprecated a lot of unnecessary `BlockRegistry` helper methods

## 0.0.24 (ALPHA)
* added `LockedSlot`
* added helper methods for smoking recipes
* added helper method for Flower Pots

## 0.0.23 (ALPHA)
* added `ColorToIntWithAlpha`
* added `SimpleFluidType` and `CustomFluidType`
* added `stoneButton()` to `BlockRegistry`

## 0.0.22 (ALPHA)
* added proper build method to `StructurePieces.Builder`
* added method for copying `BlockBehaviour.Properties` (experimental)

## 0.0.21 (ALPHA)
* added `addCompostable`
* `xpDroppingBlock` now takes an `IntProvider`
* added base for `ExtendedLootTableProvider`
* added `StructurePieces` (updated from DungeonsEnhanced `DEStructurePiece`)
* `PortalCatalystItem` now has a description

## 0.0.20 (ALPHA)
* added helper methods for `AxeItem#STRIPPABLES` and `PotionBrewing#addMix`
* `DataGenerator`s now use `Iterator`s (potentially breaking)

## 0.0.19 (ALPHA)
* added block registry helper methods
* added CustomTree API
* added `TerrainAnalyzer`

## 0.0.18 (ALPHA)
* fixed `PortalBlock` model generator

## 0.0.17 (ALPHA)
* added methods for converting `ChunkPos` to `BlockPos`
* added Amterin Portal API (thanks to KaupenJoe and The Undergarden mod)
* added `blockItem()` model provider with custom model
* fix `ExternalEffect` constructor was private (however that happened)
