package com.ametrinstudios.ametrin.event;

import com.ametrinstudios.ametrin.Ametrin;
import com.ametrinstudios.ametrin.world.gen.structure.processor.AmProcessorTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = Ametrin.ModID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AmEvents {
    @SubscribeEvent
    public static void register(RegisterEvent event){
        event.register(BuiltInRegistries.STRUCTURE_PROCESSOR.key(), helper ->{
            AmProcessorTypes.register();
        });
    }
}