package com.ametrinstudios.ametrin.fluid;

import com.ametrinstudios.ametrin.util.Extensions;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.awt.*;
import java.util.function.Consumer;

/**
 * Basic implementation of {@link FluidType} that defaults to water texture.
 */
public class SimpleFluidType extends FluidType {
    public static final ResourceLocation stillTexture = new ResourceLocation("block/water_still");
    public static final ResourceLocation flowingTexture = new ResourceLocation("block/water_flow");
    private final int tintColor;
    private final Vector3f fogColor;

    public SimpleFluidType(final Color tintColor, final Color fogColor, final Properties properties) {
        this(Extensions.ColorToIntWithAlpha(tintColor), new Vector3f(fogColor.getRed()/255f, fogColor.getGreen()/255f, fogColor.getBlue()/255f), properties);
    }

    public SimpleFluidType(final int tintColor, final Vector3f fogColor, final Properties properties) {
        super(properties);
        this.tintColor = tintColor;
        this.fogColor = fogColor;
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {return stillTexture;}

            @Override
            public ResourceLocation getFlowingTexture() {return flowingTexture;}

            @Override
            public int getTintColor() {return tintColor;}

            @Override
            public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                return fogColor;
            }

            @Override
            public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
                RenderSystem.setShaderFogStart(1f);
                RenderSystem.setShaderFogEnd(6f); // distance when the fog starts
            }
        });
    }

    public int getTintColor() {return tintColor;}

    public Vector3f getFogColor() {return fogColor;}
}
