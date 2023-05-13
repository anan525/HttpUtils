package com.httptools;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

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
        try {
            body = convertStream(inputStream);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 转换
     *
     * @param in
     * @return
     */
    public String convertStream(InputStream in) throws UnsupportedEncodingException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int len = 0;
        //缓存
        byte[] bytes = new byte[1024];

        try {
            while ((len = in.read(bytes)) != -1) {
                byteArrayOutputStream.write(bytes, 0, len);
            }
            in.close();
            byteArrayOutputStream.close();
            //转成byte
            byte[] data = byteArrayOutputStream.toByteArray();
            //识别编码
            String s = new String(data);
            if (s.contains("gbk")) {
                return new String(data, "gbk");
            } else if (s.contains("gb2312")) {
                return new String(data, "gb2312");
            } else {
                return new String(data, "utf-8");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
