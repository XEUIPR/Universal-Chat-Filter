package com.xeu.universalfilter;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

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

        //Compile regex before the loop
        ArrayList<Pattern> compiled = new ArrayList<>();
        for(String pattern : ModConfig.REGEX_FILTERS.get()){ //for each regex in config
            try{
                compiled.add(Pattern.compile(pattern));}
            catch(Exception e){
                System.err.println("Invalid regex in: " + pattern);}
        }
        
        //find regex in messages
        for(int i = 0; compiled.size() > i; i++){
            Matcher finder = compiled.get(i).matcher(msg);
            if(finder.find()){
                event.setCanceled(true);
                return;
            }
        }


        /* 
        for (String msgs : msg) {
            for (Pattern pattern : patterns) {
                if (pattern.matcher(msgs).find()) {
                    event.setCanceled(true);
                    return;
                }
            }
        }
            */
    }
}