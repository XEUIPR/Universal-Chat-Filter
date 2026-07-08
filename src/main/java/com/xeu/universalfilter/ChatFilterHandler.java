package com.xeu.universalfilter;

import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import java.util.regex.Pattern;

@Mod.EventBusSubscriber(modid = "universalfilter")
public class ChatFilterHandler {

    @SubscribeEvent
    public static void onServerChat(ServerChatEvent event) {
        String target = ModConfig.FILTER_TARGET.get().toUpperCase();
        boolean isServerEnv = FMLEnvironment.dist.isDedicatedServer();

        //Skip chat blocking if configuration target doesn't match current environment
        if (target.equals("SERVER") && !isServerEnv) return;
        if (target.equals("CLIENT") && isServerEnv) return;

        String msg = event.getRawText();

        //Basic Filters
        for (String filter : ModConfig.BASIC_FILTERS.get()) {
            if (msg.contains(filter)) {
                event.setCanceled(true);
                return;
            }
        }

        //Regex Filters
        for (String regex : ModConfig.REGEX_FILTERS.get()) {
            try {
                if (Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(msg).find()) {
                    event.setCanceled(true);
                    return;
                }
            } catch (Exception e) {
                //Safeguard against broken config patterns
            }
        }
    }
}