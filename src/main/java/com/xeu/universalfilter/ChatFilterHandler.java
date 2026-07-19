package com.xeu.universalfilter;

import java.util.regex.Pattern;

import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod.EventBusSubscriber(modid = "universalfilter")
public class ChatFilterHandler {

    @SubscribeEvent
    public static void onServerChat(ServerChatEvent event) { 
        String target = ModConfig.FILTER_TARGET.get().toUpperCase();
        boolean isServerEnv = FMLEnvironment.dist.isDedicatedServer();

        //Skip chat filtering if config doesn't match current environment
        if (target.equals("SERVER") && !isServerEnv) return;
        if (target.equals("CLIENT") && isServerEnv) return;

        String msg = event.getRawText(); //message sent in chat


        //Basic Filters
        for (String filter : ModConfig.BASIC_FILTERS.get()) {
            if (msg.contains(filter)) {
                event.setCanceled(true); //if message contains string, cancel event
                return;
            }
        }

        //Regex
        if(Caches.getPrecompiledCaches() == null){}
        else{
            Pattern[] compiled = Caches.getPrecompiledCaches();

            for(Pattern p : compiled){
                if(p.matcher(msg).find()){
                    event.setCanceled(true);
                    return;
                }
            }
        }

    }
}