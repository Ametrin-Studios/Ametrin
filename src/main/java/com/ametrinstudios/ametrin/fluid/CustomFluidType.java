package com.ametrinstudios.ametrin.fluid;

import com.ametrinstudios.ametrin.util.Extensions;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.awt.*;
import java.util.function.Consumer;

/**
 * Basic implementation of {@link FluidType} that supports specifying still and flowing textures in the constructor.
 *
 * @author Choonster (<a href="https://github.com/Choonster-Minecraft-Mods/TestMod3/blob/1.19.x/LICENSE.txt">MIT License</a>)
 * Change by: Kaupenjoe
 * Added overlayTexture and tintColor as well. Also converts tint color into fog color
 * Change by: Barion
 * option to set colors using the {@link Color} class
 * made overlayTexture optional
 */
public class CustomFluidType extends FluidType {
    private final ResourceLocation stillTexture;
    private final ResourceLocation flowingTexture;
    @Nullable private final ResourceLocation overlayTexture;
    private final int tintColor;
    private final Vector3f fogColor;

    public CustomFluidType(final ResourceLocation stillTexture, final ResourceLocation flowingTexture, @Nullable final ResourceLocation overlayTexture, final Color tintColor, final Color fogColor, final Properties properties){
        this(stillTexture, flowingTexture, overlayTexture, Extensions.ColorToIntWithAlpha(tintColor), new Vector3f(fogColor.getRed()/255f, fogColor.getGreen()/255f, fogColor.getBlue()/255f), properties);
    }

    public CustomFluidType(final ResourceLocation stillTexture, final ResourceLocation flowingTexture, @Nullable final ResourceLocation overlayTexture, final int tintColor, final Vector3f fogColor, final Properties properties) {
        super(properties);
        this.stillTexture = stillTexture;
        this.flowingTexture = flowingTexture;
        this.overlayTexture = overlayTexture;
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
            public @Nullable ResourceLocation getOverlayTexture() {return overlayTexture;}

            @Override
            public int getTintColor() {return tintColor;}

            @Override
            public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                return fogColor;
            }

            @Override
            public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
                RenderSystem.setShaderFogStart(1f);
                RenderSystem.setShaderFogEnd(6f);
            }
        });
    }

    public ResourceLocation getStillTexture() {return stillTexture;}

    public ResourceLocation getFlowingTexture() {return flowingTexture;}

    public int getTintColor() {return tintColor;}

    public @Nullable ResourceLocation getOverlayTexture() {return overlayTexture;}

    public Vector3f getFogColor() {return fogColor;}
}
