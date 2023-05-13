package com.httptools;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class SelfCallServerInterceptor implements SelfInterceptor {
    @Override
    public SelfResponse interceptor(Chain chain) throws IOException {
        SelfRequest request = chain.request();
        String url = request.getUrl();
        String method = request.getMethod().getMethod();
        SelfHeader header = request.getHeader();
        SelfRequestBody requestBody = request.getRequestBody();

        URL uri = new URL(url);
        URLConnection urlConnection = uri.openConnection();
        //设置参数
        if (urlConnection instanceof HttpURLConnection) {
            ((HttpURLConnection) urlConnection).setRequestMethod(method);
            if (header != null) {
                HashMap<String, String> mHeaders = header.getmHeader();
                if (mHeaders != null && mHeaders.size() > 0)
                    for (String key : mHeaders.keySet()) {
                        urlConnection.setRequestProperty(key, mHeaders.get(key));
                    }
            }

        }
        //请求
        urlConnection.connect();
        //写入body
        if (requestBody != null) {
            requestBody.onWriteBody(urlConnection.getOutputStream());
        }
        int responseCode = ((HttpsURLConnection) urlConnection).getResponseCode();
        InputStream inputStream = urlConnection.getInputStream();
        String responseMessage = ((HttpsURLConnection) urlConnection).getResponseMessage();
        SelfResponse response = new SelfResponse(responseCode, responseMessage, inputStream);
        return response;

    }
}
