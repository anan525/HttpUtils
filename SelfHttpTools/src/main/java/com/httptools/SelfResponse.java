package com.httptools;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SelfResponse {

    private int code;

    private String msg;

    private String body;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getBody() {
        return body;
    }

    public SelfResponse(int code, String msg, InputStream inputStream) {
        this.code = code;
        this.msg = msg;
        body = convertStream(inputStream);
    }

    /**
     * 转换
     *
     * @param in
     * @return
     */
    public String convertStream(InputStream in) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();

    }
}
