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
        BoatResources = createBoatResources(context, variant);
    }

    private static <B extends Boat & ICustomBoat> Map<CustomBoatType, Pair<ResourceLocation, ListModel<Boat>>> createBoatResources(EntityRendererProvider.Context context, BoatVariant<B> variant){
        return CustomBoatType.getAll().stream().collect(ImmutableMap.toImmutableMap((type) -> type,
                (type) -> Pair.of(new ResourceLocation(type.modID(), "textures/entity/" + variant.textureFolder() + type.name() + ".png"),
                        createBoatModel(context, type, variant))));
    }

    private static <B extends Boat & ICustomBoat> ListModel<Boat> createBoatModel(EntityRendererProvider.Context context, CustomBoatType type, BoatVariant<B> variant) {
        var modelLayerLocation = new ModelLayerLocation(new ResourceLocation(variant.textureFolder() + "oak"), "main");
        var modelPart = context.bakeLayer(modelLayerLocation);
        return variant.getModel(modelPart, type);
    }

    @Override
    public void render(B boat, float yaw, float partialTicks, PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0, 0.375, 0);
        poseStack.mulPose(Axis.YP.rotationDegrees(180 - yaw));
        var hurtTimeReminder = boat.getHurtTime() - partialTicks;
        var damageReminder = boat.getDamage() - partialTicks;
        if (damageReminder < 0) damageReminder = 0;

        if (hurtTimeReminder > 0) {
            poseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(hurtTimeReminder) * hurtTimeReminder * damageReminder / 10 * boat.getHurtDir()));
        }

        var bubbleAngle = boat.getBubbleAngle(partialTicks);
        if (!Mth.equal(bubbleAngle, 0)) {
            poseStack.mulPose(new Quaternionf().setAngleAxis(bubbleAngle * (Math.PI / 180), 1, 0, 1));
        }

        var pair = getModelWithLocation(boat);
        var textureLocation = pair.getFirst();
        var model = pair.getSecond();
        poseStack.scale(-1, -1, 1);
        poseStack.mulPose(Axis.YP.rotationDegrees(90));
        model.setupAnim(boat, partialTicks, 0, -0.1f, 0, 0);
        var vertexConsumer = buffer.getBuffer(model.renderType(textureLocation));
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        if (!boat.isUnderWater()) {
            var vertexConsumer1 = buffer.getBuffer(RenderType.waterMask());
            if (model instanceof WaterPatchModel waterPatchModel) {
                waterPatchModel.waterPatch().render(poseStack, vertexConsumer1, packedLight, OverlayTexture.NO_OVERLAY);
            }
        }

        poseStack.popPose();
        super.render(boat, yaw, partialTicks, poseStack, buffer, packedLight);
    }

    public Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(B boat) { return BoatResources.get(boat.getBoatType()); }
    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull B boat) {
        return BoatResources.get(boat.getBoatType()).getFirst();
    }
}
