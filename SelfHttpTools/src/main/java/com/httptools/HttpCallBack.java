package com.httptools;

public interface HttpCallBack {

    void onSuccess(SelfCall call, SelfResponse selfResponse);

    void onError(SelfCall call, Exception exception);
}
