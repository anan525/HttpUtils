package com.httptools;

public interface SelfCall {

    void enqune(HttpCallBack callBack);

    SelfResponse execute();

    void cancle();

    boolean isCancle();

    boolean isExecute();

}
