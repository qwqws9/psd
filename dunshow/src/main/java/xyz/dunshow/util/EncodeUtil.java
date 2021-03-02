package xyz.dunshow.util;

import java.net.URLEncoder;

public class EncodeUtil {
    /**
     *  URL 인코딩
     * @param component
     * @return
     */
    public static String encodeURIComponent(String component) {
        String result = null;

        try {
            result = URLEncoder.encode(component, "UTF-8");
        } catch (Exception e) {
            result = component;
        }

        return result;
    }
}
