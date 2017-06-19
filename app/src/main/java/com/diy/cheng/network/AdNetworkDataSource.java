package com.diy.cheng.network;

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by 0 on 2017/6/15.
 */

public class AdNetworkDataSource implements IAdNetworkDataSource, Callback {
    private static final int TIME_OUT = 10 * 1000;
    OkHttpClient client;
    RequestJsonParams params;
    IDataSourceListener listener;

    public AdNetworkDataSource() {
        client = new OkHttpClient();
        client.setConnectTimeout(TIME_OUT, TimeUnit.MILLISECONDS);
        client.setReadTimeout(TIME_OUT, TimeUnit.MILLISECONDS);
        client.setWriteTimeout(TIME_OUT, TimeUnit.MILLISECONDS);
    }

    @Override
    public void setJsonParams(RequestJsonParams params) {
        this.params = params;
    }

    @Override
    public void execute() {

    }

    @Override
    public void enqueue() {
        if (params == null) {
            if (listener != null) {
                listener.onDataFail("未设置Json");
            }
            return ;
        }

        if (listener != null) {
            listener.onLoading();
        }

        String requestUrl = Constants.HOST + params.getRequestUrl();
        Log.e("chengqixiang", "requestUrl === " + requestUrl);
        Request request = new Request.Builder().url(requestUrl).build();
        client.newCall(request).enqueue(this);
    }

    @Override
    public void eventTracking(String[] urls) {
        for (int i = 0; i < urls.length; i++) {
            String requestUrl = urls[i];
            Request request = new Request.Builder().url(requestUrl).build();
            client.newCall(request).enqueue(this);
        }
    }

    @Override
    public void onFailure(Request request, IOException e) {
        Log.e("chengqixiang", "加载失败...");
        if (listener != null) {
            listener.onDataFail("加载失败");
        }
    }

    @Override
    public void onResponse(Response response) throws IOException {
        Log.e("chengqixiang", "加载成功..." + response.code());

        if (listener != null) {
            listener.onDataComplete(response.body().string());
        }
    }

    @Override
    public void setDataSourceListener(IDataSourceListener listener) {
        this.listener = listener;
    }

    @Override
    public void cancel() {

    }
}
