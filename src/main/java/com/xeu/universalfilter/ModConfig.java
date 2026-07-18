package com.xeu.universalfilter;

import net.minecraftforge.common.ForgeConfigSpec;
import java.util.ArrayList;
import java.util.List;

public class ModConfig {
    public static final ForgeConfigSpec SERVER_SPEC;
    
    public static ForgeConfigSpec.ConfigValue<String> FILTER_TARGET;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> BASIC_FILTERS;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> REGEX_FILTERS;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> LOGGER_FILTERS;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Configuration for hiding specific chat and console messages.").push("filters");

        FILTER_TARGET = builder
                .comment("Where should the filters apply? Options: \"BOTH\", \"SERVER\", or \"CLIENT\"")
                .comment("Singleplayer/connected to server: CLIENT")
                .comment("---SERVER OWNERS---")
                .comment ("Filter Server Console: SERVER. Block console and client messages: BOTH")
                .define("filterTarget", "BOTH");

        BASIC_FILTERS = builder
                .comment("Any console/chat messages containing any of these strings will be hidden.")
                .defineListAllowEmpty(List.of("basicFilters"), ArrayList::new, o -> o instanceof String);

        REGEX_FILTERS = builder
                .comment("Any console/chat messages containing any of these regular expressions will be hidden. Uses Java regex syntax.")
                .comment("Double backslashes(\\\\\\\\a instead of \\\\a) or use a literal string('' instead of \"\")")
                .defineListAllowEmpty(List.of("regexFilters"), ArrayList::new, o -> o instanceof String);

        LOGGER_FILTERS = builder
                .comment("Any console messages from loggers with a name containing any of these strings will be hidden.")
                .defineListAllowEmpty(List.of("loggerFilters"), ArrayList::new, o -> o instanceof String);

        builder.pop();
        SERVER_SPEC = builder.build();
    }
}