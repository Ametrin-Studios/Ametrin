package com.ametrinstudios.ametrin.event;

import com.ametrinstudios.ametrin.world.gen.structure.processor.AmProcessorTypes;
import net.minecraft.core.Registry;
import net.minecraftforge.registries.RegisterEvent;

public class AmEvents {
    public static void register(RegisterEvent event){
        event.register(Registry.STRUCTURE_PROCESSOR.key(), helper ->{
            AmProcessorTypes.register();
        });
    }
}