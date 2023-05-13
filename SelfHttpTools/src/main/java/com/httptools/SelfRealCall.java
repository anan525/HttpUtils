package com.httptools;

import java.io.IOException;

public class SelfRealCall implements SelfCall {

    private SelfHttpClient selfHttpClient;

    private SelfRequest request;

    public SelfHttpClient getSelfHttpClient() {
        return selfHttpClient;
    }

    public SelfRequest getRequest() {
        return request;
    }

    private boolean isExecute = false;

    public SelfRealCall(SelfHttpClient selfHttpClient, SelfRequest request) {
        this.selfHttpClient = selfHttpClient;
        this.request = request;
    }

    public static SelfCall newCall(SelfHttpClient selfHttpClient, SelfRequest request) {
        return new SelfRealCall(selfHttpClient, request);
    }


    @Override
    public void enqune(HttpCallBack callBack) {
        synchronized (this) {
            //防止一个执行duoci
            if (this.isExecute()) {
                throw new IllegalStateException("execute the call more than once");
            }
            isExecute = true;
        }
        //交给dispatcher去执行
        selfHttpClient.dispatcher().enqune(new AsyncRunnable(this, callBack, true));

    }

    @Override
    public SelfResponse execute() {
        synchronized (this) {
            //防止一个执行duoci
            if (this.isExecute()) {
                throw new IllegalStateException("execute the call more than once");
            }
            isExecute = true;
        }
        return selfHttpClient.dispatcher().execute(new AsyncRunnable(this, null, false));
    }

    @Override
    public void cancle() {

    }

    @Override
    public boolean isCancle() {
        return false;
    }

    @Override
    public boolean isExecute() {
        return isExecute;
    }
}
