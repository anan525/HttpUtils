package com.httptools;

import java.io.IOException;
import java.util.List;

public class SelfRealChain implements SelfInterceptor.Chain {

    private List<SelfInterceptor> interceptors;

    private SelfRequest selfRequest;

    private int index;

    public SelfRequest getSelfRequest() {
        return selfRequest;
    }

    public SelfRealChain(List<SelfInterceptor> interceptors, SelfRequest selfRequest, int index) {
        this.interceptors = interceptors;
        this.index = index;
        this.selfRequest = selfRequest;
    }

    @Override
    public SelfRequest request() {
        return selfRequest;
    }

    @Override
    public SelfResponse proceed(SelfRequest selfRequest) throws IOException {
        return realProceed(selfRequest);
    }


    /**
     * 实际的责任链
     *
     * @param selfRequest
     * @return
     */
    private SelfResponse realProceed(SelfRequest selfRequest) throws IOException {

        if (index >= interceptors.size()) {
            throw new AssertionError();
        }

        SelfInterceptor selfInterceptor = interceptors.get(index);

        //创建下一个chain

        SelfRealChain nextChain = new SelfRealChain(interceptors, selfRequest, index + 1);

        SelfResponse response = selfInterceptor.interceptor(nextChain);

        return response;

    }
}
