package com.ametrinstudios.ametrin.client.renderer;

import com.ametrinstudios.ametrin.world.entity.boat.BoatVariant;
import com.ametrinstudios.ametrin.world.entity.boat.CustomBoatType;
import com.ametrinstudios.ametrin.world.entity.boat.ICustomBoat;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.WaterPatchModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.Boat;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class CustomBoatRenderer <B extends Boat & ICustomBoat> extends EntityRenderer<B> {
    private final Map<CustomBoatType, Pair<ResourceLocation, ListModel<Boat>>> BoatResources;

    public CustomBoatRenderer(EntityRendererProvider.Context context, BoatVariant<B> variant) {
        super(context);
        this.shadowRadius = 0.8F;
        BoatResources = createBoatResources(context, variant);
    }

    private static <B extends Boat & ICustomBoat> Map<CustomBoatType, Pair<ResourceLocation, ListModel<Boat>>> createBoatResources(EntityRendererProvider.Context context, BoatVariant<B> variant){
        return CustomBoatType.getAll().stream().collect(ImmutableMap.toImmutableMap((type) -> type,
                (type) -> Pair.of(ResourceLocation.fromNamespaceAndPath(type.modID(), "textures/entity/" + variant.textureFolder() + type.name() + ".png"),
                        createBoatModel(context, type, variant))));
    }

    private static <B extends Boat & ICustomBoat> ListModel<Boat> createBoatModel(EntityRendererProvider.Context context, CustomBoatType type, BoatVariant<B> variant) {
        var modelLayerLocation = new ModelLayerLocation(ResourceLocation.withDefaultNamespace(variant.textureFolder() + "oak"), "main");
        var modelPart = context.bakeLayer(modelLayerLocation);
        return variant.getModel(modelPart, type);
    }

    @Override
    public void render(B boat, float yaw, float partialTicks, PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.375F, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
        var remainingHurtTime = boat.getHurtTime() - partialTicks;
        var remainingDamage = Math.max(boat.getDamage() - partialTicks, 0) ;

        if (remainingHurtTime > 0.0F) {
            poseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(remainingHurtTime) * remainingHurtTime * remainingDamage / 10.0F * boat.getHurtDir()));
        }

        var bubbleAngle = boat.getBubbleAngle(partialTicks);
        if (!Mth.equal(bubbleAngle, 0.0F)) {
            poseStack.mulPose(new Quaternionf().setAngleAxis(boat.getBubbleAngle(partialTicks) * (float) (Math.PI / 180.0), 1.0F, 0.0F, 1.0F));
        }

        var pair = getModelWithLocation(boat);
        var textureLocation = pair.getFirst();
        var model = pair.getSecond();
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        model.setupAnim(boat, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
        var vertexConsumer = bufferSource.getBuffer(model.renderType(textureLocation));
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
        if (!boat.isUnderWater()) {
            var vertexConsumer1 = bufferSource.getBuffer(RenderType.waterMask());
            if (model instanceof WaterPatchModel waterPatchModel) {
                waterPatchModel.waterPatch().render(poseStack, vertexConsumer1, packedLight, OverlayTexture.NO_OVERLAY);
            }
        }

        poseStack.popPose();
        super.render(boat, yaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    public Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(B boat) { return BoatResources.get(boat.getBoatType()); }
    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull B boat) {
        return BoatResources.get(boat.getBoatType()).getFirst();
    }
}
