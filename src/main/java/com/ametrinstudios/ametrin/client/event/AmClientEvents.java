package com.ametrinstudios.ametrin.client.event;

import com.ametrinstudios.ametrin.Ametrin;
import com.ametrinstudios.ametrin.client.renderer.CustomBoatRenderer;
import com.ametrinstudios.ametrin.world.AmetrinEntityTypes;
import com.ametrinstudios.ametrin.world.entity.boat.BoatVariants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@Mod.EventBusSubscriber(modid = Ametrin.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class AmClientEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(AmetrinEntityTypes.BOAT.get(), context -> new CustomBoatRenderer<>(context, BoatVariants.DEFAULT));
        event.registerEntityRenderer(AmetrinEntityTypes.CHEST_BOAT.get(), context -> new CustomBoatRenderer<>(context, BoatVariants.CHEST));
    }
}
