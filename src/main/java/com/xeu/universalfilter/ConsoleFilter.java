package com.xeu.universalfilter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.core.LogEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import java.util.regex.Pattern;

public class ConsoleFilter {

    public static void registerLoggerFilter() {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        
        context.getConfiguration().addFilter(new AbstractFilter() {
            @Override
            public Result filter(LogEvent event) {
                if (event == null) return Result.NEUTRAL;

                //Check target configuration
                String target = ModConfig.FILTER_TARGET.get().toUpperCase();
                boolean isServerEnv = FMLEnvironment.dist.isDedicatedServer();

                //Skip filtering if target doesn't match the active environment
                if (target.equals("SERVER") && !isServerEnv) return Result.NEUTRAL;
                if (target.equals("CLIENT") && isServerEnv) return Result.NEUTRAL;

                String message = event.getMessage().getFormattedMessage();
                String loggerName = event.getLoggerName();

                //Filter by Logger Name
                for (String logFilter : ModConfig.LOGGER_FILTERS.get()) {
                    if (loggerName != null && loggerName.contains(logFilter)) {
                        return Result.DENY;
                    }
                }

                //Filter by Basic Text
                for (String basic : ModConfig.BASIC_FILTERS.get()) {
                    if (message.contains(basic)) {
                        return Result.DENY;
                    }
                }

                //Filter by Regex
                for (String regex : ModConfig.REGEX_FILTERS.get()) {
                    try {
                        if (Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(message).find()) {
                            return Result.DENY;
                        }
                    } catch (Exception e) {
                        //Ignore syntax errors
                    }
                }

                return Result.NEUTRAL;
            }
        });
        
        context.updateLoggers();
    }
}