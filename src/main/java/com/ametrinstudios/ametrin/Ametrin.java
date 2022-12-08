package com.ametrinstudios.ametrin;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;

@Mod(Ametrin.ModID)
public class Ametrin{
    public static final String ModID = "ametrin";
    @ApiStatus.Internal
    public static final Logger Logger = LogUtils.getLogger();
    public Ametrin(){
//        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
//        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
    }
}