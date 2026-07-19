package com.xeu.universalfilter;

import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.core.LogEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class ConsoleFilter {

    public static void registerLoggerFilter() {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        
        context.getConfiguration().addFilter(new AbstractFilter() {
            @Override
            public Result filter(LogEvent event) {
                if (event == null) return Result.NEUTRAL;

                //Get environment
                String target = ModConfig.FILTER_TARGET.get().toUpperCase();
                boolean isServerEnv = FMLEnvironment.dist.isDedicatedServer();

                //Skip filtering if target doesn't match the active environment
                if (target.equals("SERVER") && !isServerEnv) return Result.NEUTRAL;
                if (target.equals("CLIENT") && isServerEnv) return Result.NEUTRAL;

                String msg = event.getMessage().getFormattedMessage();
                String logger_name = event.getLoggerName();

                //Filter by Logger Name
                for (String logger : ModConfig.LOGGER_FILTERS.get()) {
                    if (logger_name != null && logger_name.contains(logger)) {
                        return Result.DENY;
                    }
                }

                //Filter by Basic Text
                for (String basic : ModConfig.BASIC_FILTERS.get()) {
                    if (msg.contains(basic)) {
                        return Result.DENY;
                    }
                }

                //Regex
                if(Caches.getPrecompiledCaches() == null){}
                else{
                    Pattern[] compiled = Caches.getPrecompiledCaches();

                    for(Pattern p : compiled){
                        if(p.matcher(msg).find()){
                            return Result.DENY;
                        }
                    }
                }

                return Result.NEUTRAL;
            }
        });
        
        context.updateLoggers();
    }
}