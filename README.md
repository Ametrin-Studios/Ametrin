## QoL API by Ametrin Studios
- [Visit the CurseForge page](https://www.curseforge.com/minecraft/mc-mods/ametrin)
- [How to use](https://github.com/BarionLP/MCModsMaven)

Feel free to join our [Discord Server](https://discord.com/invite/hwA9dd5bVh). We're always here to help!

## Docs
### Custom Boats
- put texture in `assets/{mod_id}/textures/entity/{boat/chest_boat}`
- create and register the custom boat type and items<br>(*id* should be `ResourceLocation(mod_id, boat_type_name)`)
```java
public static final CustomBoatType TEST_BOAT_TYPE = CustomBoatType.builder(id).boatItem(TestItems.TEST_BOAT::get).chestBoatItem(TestItems.TEST_CHEST_BOAT::get).register();
```

```java
public static final RegistryObject<CustomBoatItem> TEST_BOAT = REGISTRY.register("test_boat", ()-> CustomBoatItem.boat(TEST_BOAT_TYPE));
public static final RegistryObject<CustomBoatItem> TEST_CHEST_BOAT = REGISTRY.register("test_chest_boat", ()-> CustomBoatItem.chest(TEST_BOAT_TYPE));
```
- also can create new boat variations check the `BoatVariants` class 