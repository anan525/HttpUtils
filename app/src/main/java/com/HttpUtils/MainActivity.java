package com.HttpUtils;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.httptools.HttpCallBack;
import com.httptools.SelfCall;
import com.httptools.SelfHttpClient;
import com.httptools.SelfRequest;
import com.httptools.SelfResponse;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //使用自己的okhttp
        doWithSelfHttp();
    }

    private void doWithOkhttp() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();
        Request request = new Request.Builder()
                .url("https://www.baidu.com")
                .get().build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.d("测试", "" + string);
            }
        });
    }


    private void doWithSelfHttp() {
        SelfHttpClient httpClient = new SelfHttpClient.Builder()
                .build();
        SelfRequest request = new SelfRequest.Builder()
                .get()
                .url("https://www.baidu.com")
                .build();
        SelfCall selfCall = httpClient.newCall(request);

        selfCall.enqune(new HttpCallBack() {
            @Override
            public void onSuccess(SelfCall call, SelfResponse selfResponse) {
                Log.d("Self", "" + selfResponse.getBody());
            }

            @Override
            public void onError(SelfCall call, Exception exception) {
                Log.d(" Self", "" + exception.getMessage());
            }
        });
    }
}