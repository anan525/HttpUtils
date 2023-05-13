package com.httptools;

import java.util.HashMap;

public class SelfHeader {

    private HashMap<String, String> mHeader;

    public HashMap<String, String> getmHeader() {
        return mHeader;
    }

    public SelfHeader() {
        mHeader = new HashMap<>();
    }

    public boolean hasHeader(String key) {
        return mHeader.containsKey(key);
    }

    public void addHeader(String key, String value) {
        mHeader.put(key, value);
    }

    public void removeHeader(String key) {
        if (mHeader.containsKey(key)) {
            mHeader.remove(key);
        }
    }
}
