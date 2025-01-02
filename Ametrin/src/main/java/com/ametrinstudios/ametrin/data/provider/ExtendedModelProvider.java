package com.ametrinstudios.ametrin.data.provider;

import com.ametrinstudios.ametrin.Ametrin;
import com.ametrinstudios.ametrin.world.block.CustomHeadBlock;
import com.ametrinstudios.ametrin.world.block.CustomWallHeadBlock;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.blockstates.Variant;
import net.minecraft.client.data.models.blockstates.VariantProperties;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.Optional;

public class ExtendedModelProvider extends ModelProvider {
    public static final TextureSlot TEXTURE_SLOT_PORTAL = TextureSlot.create("portal", TextureSlot.TEXTURE);
    public static final ModelTemplate WALL_HEAD = new ModelTemplate(Optional.of(Ametrin.locate("block/head_wall")), Optional.empty(), TextureSlot.TEXTURE);
    public static final ModelTemplate HEAD_0 = new ModelTemplate(Optional.of(Ametrin.locate("block/head/0")), Optional.empty(), TextureSlot.TEXTURE);
    public static final ModelTemplate HEAD_1 = new ModelTemplate(Optional.of(Ametrin.locate("block/head/1")), Optional.empty(), TextureSlot.TEXTURE);
    public static final ModelTemplate HEAD_2 = new ModelTemplate(Optional.of(Ametrin.locate("block/head/2")), Optional.empty(), TextureSlot.TEXTURE);
    public static final ModelTemplate HEAD_3 = new ModelTemplate(Optional.of(Ametrin.locate("block/head/3")), Optional.empty(), TextureSlot.TEXTURE);
    public static final ModelTemplate WALL_HEAD_CUTOUT = new ModelTemplate(Optional.of(Ametrin.locate("block/head_wall_cutout")), Optional.empty(), TextureSlot.TEXTURE).extend().renderType("cutout").build();
    public static final ModelTemplate HEAD_0_CUTOUT = new ModelTemplate(Optional.of(Ametrin.locate("block/head/cutout/0")), Optional.empty(), TextureSlot.TEXTURE).extend().renderType("cutout").build();
    public static final ModelTemplate HEAD_1_CUTOUT = new ModelTemplate(Optional.of(Ametrin.locate("block/head/cutout/1")), Optional.empty(), TextureSlot.TEXTURE).extend().renderType("cutout").build();
    public static final ModelTemplate HEAD_2_CUTOUT = new ModelTemplate(Optional.of(Ametrin.locate("block/head/cutout/2")), Optional.empty(), TextureSlot.TEXTURE).extend().renderType("cutout").build();
    public static final ModelTemplate HEAD_3_CUTOUT = new ModelTemplate(Optional.of(Ametrin.locate("block/head/cutout/3")), Optional.empty(), TextureSlot.TEXTURE).extend().renderType("cutout").build();
    public static final ModelTemplate PORTAL_NS = new ModelTemplate(Optional.of(ResourceLocation.withDefaultNamespace("nether_portal_ns")), Optional.empty(), TEXTURE_SLOT_PORTAL, TextureSlot.PARTICLE);
    public static final ModelTemplate PORTAL_EW = new ModelTemplate(Optional.of(ResourceLocation.withDefaultNamespace("nether_portal_ew")), Optional.empty(), TEXTURE_SLOT_PORTAL, TextureSlot.PARTICLE);

    public ExtendedModelProvider(PackOutput output, String modId) {
        super(output, modId);
    }

    public void createCustomHead(BlockModelGenerators blockModels, Block head, Block wallHead) {
        createCustomHeadImpl(blockModels, head, wallHead, WALL_HEAD, HEAD_0, HEAD_1, HEAD_2, HEAD_3);
    }
    public void createCustomHeadCutout(BlockModelGenerators blockModels, Block head, Block wallHead) {
        createCustomHeadImpl(blockModels, head, wallHead, WALL_HEAD_CUTOUT, HEAD_0_CUTOUT, HEAD_1_CUTOUT, HEAD_2_CUTOUT, HEAD_3_CUTOUT);
    }

    private static void createCustomHeadImpl(BlockModelGenerators blockModels, Block head, Block wallHead, ModelTemplate wall, ModelTemplate m0, ModelTemplate m1, ModelTemplate m2, ModelTemplate m3) {
        var mapping = TextureMapping.defaultTexture(head);

        var wallModel = wall.create(wallHead, mapping, blockModels.modelOutput);
        blockModels.blockStateOutput.accept(MultiVariantGenerator.multiVariant(wallHead)
                .with(PropertyDispatch.property(CustomWallHeadBlock.FACING)
                        .select(Direction.NORTH, Variant.variant().with(VariantProperties.MODEL, wallModel))
                        .select(Direction.EAST, Variant.variant().with(VariantProperties.MODEL, wallModel).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                        .select(Direction.SOUTH, Variant.variant().with(VariantProperties.MODEL, wallModel).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                        .select(Direction.WEST, Variant.variant().with(VariantProperties.MODEL, wallModel).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                ));

        var groundState = PropertyDispatch.property(CustomHeadBlock.ROTATION);

        var model0 = m0.createWithSuffix(head, "/0", mapping, blockModels.modelOutput);
        var model1 = m1.createWithSuffix(head, "/1", mapping, blockModels.modelOutput);
        var model2 = m2.createWithSuffix(head, "/2", mapping, blockModels.modelOutput);
        var model3 = m3.createWithSuffix(head, "/3", mapping, blockModels.modelOutput);

        for (var i = 0; i < 4; i++) {
            var rotation = switch (i) {
                case 1 -> VariantProperties.Rotation.R90;
                case 2 -> VariantProperties.Rotation.R180;
                case 3 -> VariantProperties.Rotation.R270;
                default -> VariantProperties.Rotation.R0;
            };

            groundState
                    .select(i * 4, Variant.variant().with(VariantProperties.MODEL, model0).with(VariantProperties.Y_ROT, rotation))
                    .select(i * 4 + 1, Variant.variant().with(VariantProperties.MODEL, model1).with(VariantProperties.Y_ROT, rotation))
                    .select(i * 4 + 2, Variant.variant().with(VariantProperties.MODEL, model2).with(VariantProperties.Y_ROT, rotation))
                    .select(i * 4 + 3, Variant.variant().with(VariantProperties.MODEL, model3).with(VariantProperties.Y_ROT, rotation));
        }

        blockModels.blockStateOutput.accept(MultiVariantGenerator.multiVariant(head).with(groundState));

        blockModels.registerSimpleItemModel(head.asItem(), model0);
    }

    public void createPlanePortalBlock(BlockModelGenerators blockModels, Block portal) {
        var texture = ModelLocationUtils.getModelLocation(portal);
        var mapping = new TextureMapping().put(TextureSlot.PARTICLE, texture).put(TEXTURE_SLOT_PORTAL, texture);
        var ns = PORTAL_NS.create(texture.withSuffix("_ns"), mapping, blockModels.modelOutput);
        var ew = PORTAL_EW.create(texture.withSuffix("_ew"), mapping, blockModels.modelOutput);
        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.multiVariant(portal)
                        .with(PropertyDispatch.property(BlockStateProperties.HORIZONTAL_AXIS)
                                .select(Direction.Axis.X, Variant.variant().with(VariantProperties.MODEL, ns))
                                .select(Direction.Axis.Z, Variant.variant().with(VariantProperties.MODEL, ew))
                        )
        );
    }
}
