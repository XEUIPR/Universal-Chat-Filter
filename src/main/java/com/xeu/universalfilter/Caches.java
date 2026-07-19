package com.xeu.universalfilter;

import java.util.regex.Pattern;

public final class Caches {
    private static Pattern[] compiled = new Pattern[ModConfig.REGEX_FILTERS.get().size()];
    //regex caches & precompiled storage
    static{
        int i = 0;
        for(String pattern : ModConfig.REGEX_FILTERS.get()){ 
            try{
                compiled[i++] = Pattern.compile(pattern);
            } catch(Exception e){
                System.err.println("Invalid regex in: " + pattern);
            }
        }
    }

    public static Pattern[] getPrecompiledCaches() {
        return compiled;
    }
}