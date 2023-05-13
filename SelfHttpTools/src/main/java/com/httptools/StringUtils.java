package com.httptools;

import android.util.Log;

import java.net.URI;

public class StringUtils {

    public static final String EMPTY = "";

    /**
     * 判断是否以key开始
     *
     * @param string
     * @param key
     * @return
     */
    public static boolean startsWithIgnoreCase(String string, String key) {
        String s = string.toUpperCase();
        String s1 = key.toUpperCase();
        return s.startsWith(s1);
    }


    /**
     * 判断是否以key结束
     *
     * @param string
     * @param key
     * @return
     */
    public static boolean endsWithIgnoreCase(String string, String key) {
        String s = string.toUpperCase();
        String s1 = key.toUpperCase();
        return s.endsWith(s1);
    }


    /**
     * 获取主机
     *
     * @param url
     * @return
     */
    public static String getHost(String url) {
        if (!(StringUtils.startsWithIgnoreCase(url, "http://") || StringUtils
                .startsWithIgnoreCase(url, "https://"))) {
            url = "http://" + url;
        }
        String returnVal = StringUtils.EMPTY;
        try {
            URI uri = new URI(url);
            returnVal = uri.getHost();
        } catch (Exception e) {
            Log.e("", "" + e.getMessage());
        }
        return returnVal;
    }
}

