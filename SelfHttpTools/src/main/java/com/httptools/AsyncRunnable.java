package com.httptools;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class AsyncRunnable implements Runnable {

    private SelfRealCall call;

    private HttpCallBack callBack;

    private boolean isEnqune;

    public SelfRealCall getCall() {
        return call;
    }

    public AsyncRunnable(SelfRealCall call, HttpCallBack callBack, boolean isEnqune) {
        this.call = call;
        this.callBack = callBack;
        this.isEnqune = isEnqune;
    }

    @Override
    public void run() {
        execute();
    }

    /**
     * 执行任务.
     */
    public SelfResponse execute() {
        boolean isSignExecute = false;
        try {
            SelfResponse response = getResponseWithInterceptorChain();
            if (call.isCancle()) {
                isSignExecute = true;
                if (isEnqune) {
                    callBack.onError(call, new Exception("task is cancled"));
                }
            } else {
                isSignExecute = true;
                if (isEnqune) {
                    if (response.getCode() == 200) {
                        callBack.onSuccess(call, response);
                    } else {
                        callBack.onError(call, new Exception(response.getMsg()));
                    }
                }
            }
            return response;
        } catch (Exception e) {
            if (!isSignExecute) {
                Log.e("错误", "非工具本生的错误" + e.getMessage());
            } else {
                if (isEnqune) {
                    callBack.onError(call, e);
                }
            }
        } finally {
            //这里需要删除对应的任务，并触发新的任务
            call.getSelfHttpClient().dispatcher().finishTask(this, isEnqune);
        }
        return null;
    }

    private SelfResponse getResponseWithInterceptorChain() throws IOException {
        ArrayList<SelfInterceptor> selfInterceptors = new ArrayList<>();
        selfInterceptors.add(new SelfBridegeIntercepter());
        selfInterceptors.add(new SelfCallServerInterceptor());
//        selfInterceptors.add(new CacheInterceptor(client.internalCache()));
//        selfInterceptors.add(new ConnectInterceptor(client));
        SelfRealChain selfRealChain = new SelfRealChain(selfInterceptors, call.getRequest(), 0);

        return selfRealChain.proceed(call.getRequest());
    }
}
