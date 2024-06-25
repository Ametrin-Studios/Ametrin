package com.ametrinstudios.ametrin.event;

import com.ametrinstudios.ametrin.Ametrin;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@EventBusSubscriber(modid = Ametrin.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class AmEvents {
    @SubscribeEvent
    public static void register(RegisterEvent event){
        event.register(BuiltInRegistries.STRUCTURE_PROCESSOR.key(), helper ->{
//            AmProcessorTypes.register();
        });
    }
}