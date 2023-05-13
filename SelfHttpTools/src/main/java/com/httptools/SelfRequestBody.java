package com.httptools;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SelfRequestBody {
    // 表单格式
    public static final String FORM = "multipart/form-data";

    // 参数，文件
    private final HashMap<String, Object> params;
    private String boundary = createBoundary();
    private String type;
    private String startBoundary = "--" + boundary;
    private String endBoundary = startBoundary + "--";

    public SelfRequestBody() {
        params = new HashMap<>();
    }

    private String createBoundary() {
        return "OkHttp"+ UUID.randomUUID().toString();
    }
    // 都是一些规范
    public String getContentType() {
        return type + ";boundary = " + boundary;
    }

    // 多少个字节要给过去，写的内容做一下统计
    public long getContentLength() {
        long length=0;
        Set<Map.Entry<String, Object>> entries = params.entrySet();

        for(Map.Entry<String, Object> entry:entries){
            String key = entry.getKey();
            Object value = entry.getValue();
            if(value instanceof String){
                String text = getText(key, (String) value);
                Log.e("TAG",text);
                length+=text.getBytes().length;
            }
        }

        if(params.size()!=0){
            length+=endBoundary.getBytes().length;
        }
        return length;
    }

    //写内容
    public void onWriteBody(OutputStream outputStream) throws IOException {
        Set<Map.Entry<String, Object>> entries = params.entrySet();
        for(Map.Entry<String, Object> entry:entries){
            String key = entry.getKey();
            Object value = entry.getValue();
            if(value instanceof String){
                String text = getText(key, (String) value);
                outputStream.write(text.getBytes());
            }
        }
        if(params.size()!=0){
            outputStream.write(endBoundary.getBytes());
        }
    }

    /**
     startBoundary + "\r\n"
     Content-Disposition; form-data; name = "pageSize"
     Context-Type: text/plain


     1
     */
    private String getText(String key, String value) {
        return startBoundary+"\r\n"+
                "Content-Disposition: form-data; name = \""+key+"\"\r\n"+
                "Context-Type: text/plain\r\n"+
                "\r\n"+
                value+
                "\r\n";
    }

    public SelfRequestBody addParam(String key, String value) {
        params.put(key, value);
        return this;
    }

    public SelfRequestBody type(String type) {
        this.type = type;
        return this;
    }
}
