package com.httptools;

import java.net.URI;

public class SelfRequest {
    //请求头
    private SelfHeader header;
    //请求method
    private SelfMethod method;
    //请求的url
    private String url;
    //请求体
    private SelfRequestBody requestBody;

    public SelfRequestBody getRequestBody() {
        return requestBody;
    }

    public SelfHeader getHeader() {
        return header;
    }

    public SelfMethod getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    private SelfRequest(Builder builder) {
        header = builder.header;
        method = builder.method;
        url = builder.url;
    }

    public boolean hasHeader(String key) {
        return header != null && header.hasHeader(key);
    }

    public Builder newBuilder() {
        return new Builder(this);
    }


    public final static class Builder {
        //请求头
        private SelfHeader header;
        //请求method
        private SelfMethod method;
        //请求的url
        private String url;
        private SelfRequestBody requestBody;


        public Builder(SelfRequest selfRequest) {
            method = selfRequest.method;
            url = selfRequest.getUrl();
            header = selfRequest.header;
            requestBody = selfRequest.requestBody;
        }

        public Builder() {
            header = new SelfHeader();
        }

        public Builder addHeader(String key, String value) {
            header.addHeader(key, value);
            return this;
        }

        public Builder get() {
            this.method = new SelfMethod("GET");
            return this;
        }

        public Builder post() {
            this.method = new SelfMethod("POST");
            return this;
        }


        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public SelfRequest build() {
            return new SelfRequest(this);
        }

        public void removeHeader(String key) {
            if (header.hasHeader(key)) {
                header.removeHeader(key);
            }
        }
    }
}
