package com.httptools;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SelfDispatcher {
    //最大并发数量
    private static final int MaxRequestCount = 16;
    //同一个host并发的数量
    private static final int MaxHostCount = 5;
    //线程池
    private static volatile ThreadPoolExecutor threadPoolExecutor;
    //存储等待的任务
    private Deque<AsyncRunnable> waittingAsyncs = new ArrayDeque<>();
    //存储正在运行的任务
    private Deque<AsyncRunnable> runningAsyncs = new ArrayDeque<>();
    //存储用同步方式运行的call
    private Deque<SelfCall> runningAsyncCall = new ArrayDeque<>();


    public synchronized ThreadPoolExecutor executorService() {
        if (threadPoolExecutor == null) {
            threadPoolExecutor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue());
        }
        return threadPoolExecutor;
    }

    public void enqune(AsyncRunnable asyncRunnable) {
        if (runningAsyncs.size() < MaxRequestCount && getHostCount(asyncRunnable) < MaxHostCount) {
            runningAsyncs.add(asyncRunnable);
            //执行任务
            executorService().execute(asyncRunnable);
        } else {
            waittingAsyncs.add(asyncRunnable);
        }
    }

    /**
     * 根据当前的call里面的url判断是否是同一个主机
     *
     * @param asyncRunnable
     * @return
     */
    private int getHostCount(AsyncRunnable asyncRunnable) {
        String url = asyncRunnable.getCall().getRequest().getUrl();
        int hostCount = 0;
        for (AsyncRunnable runningAsync : runningAsyncs) {
            if (runningAsync.getCall().getRequest().getUrl().equals(url)) {
                hostCount++;
            }
        }
        return hostCount;
    }

    /**
     * 请求结束.
     *
     * @param asyncRunnable
     * @param isEnqune
     */
    public void finishTask(AsyncRunnable asyncRunnable, boolean isEnqune) {
        if (isEnqune) {
            runningAsyncs.remove(asyncRunnable);
            promotList();
        } else {
            runningAsyncCall.remove(asyncRunnable.getCall());
        }
    }

    /**
     * 重新触发。
     */
    private void promotList() {
        if (runningAsyncs.size() >= MaxRequestCount) {
            //超过了最大并发量
            return;
        }
        if (waittingAsyncs.size() == 0) {
            //没有等待中的
            return;
        }

        //遍历
        Iterator<AsyncRunnable> iterator = waittingAsyncs.iterator();
        while (iterator.hasNext()) {
            AsyncRunnable next = iterator.next();
            waittingAsyncs.remove(next);
            if (getHostCount(next) < MaxHostCount) {
                runningAsyncs.add(next);
                executorService().execute(next);
            }
            if (runningAsyncs.size() >= MaxRequestCount) {
                //超过最大运行限制
                return;
            }
        }
    }

    public SelfResponse execute(AsyncRunnable asyncRunnable) {
        runningAsyncCall.add(asyncRunnable.getCall());
        return asyncRunnable.execute();
    }
}
