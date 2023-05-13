package com.httptools;

import java.io.IOException;
import java.net.MalformedURLException;

public interface SelfInterceptor {

    SelfResponse interceptor(Chain chain) throws IOException;

    public interface Chain {

        SelfRequest request();

        SelfResponse proceed(SelfRequest selfRequest) throws IOException;
    }
}
