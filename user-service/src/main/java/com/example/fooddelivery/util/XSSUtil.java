package com.example.fooddelivery.util;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class XSSUtil {

    private static final Safelist safelist = Safelist.relaxed();
    
    // Configure any additional whitelisted tags or attributes if necessary
    static {
        // For example, to allow img tags with src attributes pointing to http or https
        // safelist.addTags("img");
        // safelist.addAttributes("img", "src");
        // safelist.addProtocols("img", "src", "http", "https");
    }

    public static String sanitize(String input) {
        if (input == null) {
            return null;
        }
        return Jsoup.clean(input, safelist);
    }
}
