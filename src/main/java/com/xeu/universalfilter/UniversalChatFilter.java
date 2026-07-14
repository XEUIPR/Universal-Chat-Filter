package com.xeu.universalfilter;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("universalfilter")
public class UniversalChatFilter {
    public UniversalChatFilter() {
        ModLoadingContext.get().registerConfig(Type.COMMON, com.xeu.universalfilter.ModConfig.SERVER_SPEC);
        
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ConsoleFilter::registerLoggerFilter);
    }
}