package com.httptools;

import java.io.IOException;

/**
 * 这里是用来写请求头和请求体的
 */
public class SelfBridegeIntercepter implements SelfInterceptor {
    private static final String Version = "selfhttp/1.0.0";

    @Override
    public SelfResponse interceptor(Chain chain) throws IOException {
        SelfRequest userRequest = chain.request();
        SelfRequest.Builder requestBuilder = userRequest.newBuilder();
        if (userRequest.getRequestBody() != null) {
            SelfRequestBody body = userRequest.getRequestBody();
            //写入body
            String contentType = body.getContentType();
            if (contentType != null) {
                requestBuilder.addHeader("Content-Type", contentType.toString());
            }

            long contentLength = body.getContentLength();
            if (contentLength != -1) {
                requestBuilder.addHeader("Content-Length", Long.toString(contentLength));
                requestBuilder.removeHeader("Transfer-Encoding");
            } else {
                requestBuilder.addHeader("Transfer-Encoding", "chunked");
                requestBuilder.removeHeader("Content-Length");
            }
        }


        //写一些默认的表头
        if (!userRequest.hasHeader("Host")) {
            requestBuilder.addHeader("Host", StringUtils.getHost(userRequest.getUrl()));
        }

        if (!userRequest.hasHeader("Connection")) {
            requestBuilder.addHeader("Connection", "Keep-Alive");
        }
        // the transfer stream.
        boolean transparentGzip = false;
        if (!userRequest.hasHeader("Accept-Encoding") && !userRequest.hasHeader("Range")) {
            transparentGzip = true;
            requestBuilder.addHeader("Accept-Encoding", "gzip");
        }

        if (!userRequest.hasHeader("User-Agent")) {
            requestBuilder.addHeader("User-Agent", Version);
        }

        SelfResponse response = chain.proceed(requestBuilder.build());

        return response;
    }
}
