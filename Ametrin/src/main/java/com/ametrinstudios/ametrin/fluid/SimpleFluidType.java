package com.ametrinstudios.ametrin.fluid;

import net.neoforged.neoforge.fluids.FluidType;

/**
 * Basic implementation of {@link FluidType} that defaults to water texture.
 */
//public class SimpleFluidType extends FluidType {
//    public static final ResourceLocation stillTexture = ResourceLocation.withDefaultNamespace("block/water_still");
//    public static final ResourceLocation flowingTexture = ResourceLocation.withDefaultNamespace("block/water_flow");
//    private final int tintColor;
//    private final Vector3f fogColor;
//
//    public SimpleFluidType(final Color tintColor, final Color fogColor, final Properties properties) {
//        this(ColorHelper.colorToIntWithAlpha(tintColor), new Vector3f(fogColor.getRed()/255f, fogColor.getGreen()/255f, fogColor.getBlue()/255f), properties);
//    }
//
//    public SimpleFluidType(final int tintColor, final Vector3f fogColor, final Properties properties) {
//        super(properties);
//        this.tintColor = tintColor;
//        this.fogColor = fogColor;
//    }
//
//    @Override
//    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
//        consumer.accept(new IClientFluidTypeExtensions() {
//            @Override
//            public @NotNull ResourceLocation getStillTexture() { return stillTexture; }
//
//            @Override
//            public @NotNull ResourceLocation getFlowingTexture() { return flowingTexture; }
//
//            @Override
//            public int getTintColor() {return tintColor;}
//
//            @Override
//            public @NotNull Vector3f modifyFogColor(@NotNull Camera camera, float partialTick, @NotNull ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
//                return fogColor;
//            }
//
//            @Override
//            public void modifyFogRender(@NotNull Camera camera, @NotNull FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
//                RenderSystem.setShaderFogStart(1f);
//                RenderSystem.setShaderFogEnd(6f); // distance when the fog starts
//            }
//        });
//    }
//
//    public int getTintColor() { return tintColor; }
//
//    public Vector3f getFogColor() { return fogColor; }
//}
