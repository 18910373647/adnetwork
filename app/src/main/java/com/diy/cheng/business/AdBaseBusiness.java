package com.diy.cheng.business;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.diy.cheng.model.AdBaseModel;
import com.diy.cheng.network.AdNetworkDataSource;
import com.diy.cheng.network.IDataSource;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 0 on 2017/6/16.
 */

public abstract class AdBaseBusiness implements IDataSource.IDataSourceListener{
    protected static final int LOADING = 1;
    protected static final int LOAD_FAILED = 2;
    protected static final int LOAD_COMPLETE = 3;
    protected IAdBusiness.IAdBusinessListener<?> mListener;
    protected Object mTag;
    protected int mBussinessType;
    protected AdNetworkDataSource dataSource;
    protected Gson gson;

    Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == LOADING) {
                if (mListener != null) {
                    mListener.onLoading(mTag);
                }
            } else if (msg.what == LOAD_FAILED) {
                if (mListener != null) {
                    mListener.onLoadError(msg.obj.toString(), mTag);
                }
            } else if (msg.what == LOAD_COMPLETE) {
                if (mListener != null) {
                    ((IAdBusiness.IAdBusinessListener<AdBaseModel>)mListener).onLoadComplete((AdBaseModel) msg.obj, mTag);
                }
            }
            return true;
        }
    });

    protected AdBaseBusiness() {
        gson = new Gson();
        dataSource = new AdNetworkDataSource();
        dataSource.setDataSourceListener(this);
    }

    protected void initBusiness(Object object, int type, IAdBusiness.IAdBusinessListener<?> listener) {
        this.mTag = object;
        this.mBussinessType = type;
        this.mListener = listener;
    }

    protected void post(Object result) {
        Message msg = new Message();
        msg.what = LOAD_COMPLETE;
        msg.obj = result;
        handler.sendMessage(msg);
    }

    @Override
    public void onLoading() {
        handler.sendEmptyMessage(LOADING);
    }

    @Override
    public void onDataComplete(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            int result = 0;
            if (jsonObject.has("result")) {
                result = jsonObject.getInt("result");
            }

            if (result == 0) {
                String error = "";
                if (jsonObject.has("message")) {
                    error += jsonObject.getString("message");
                }
                Message msg = new Message();
                msg.what = LOAD_FAILED;
                msg.obj = error;
                handler.sendMessage(msg);
            }

            onLoadComplete(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDataFail(String error) {
        Message msg = new Message();
        msg.what = LOAD_FAILED;
        msg.obj = error;
        handler.sendMessage(msg);
    }

    protected abstract void onLoadComplete(JSONObject jsonObject);
}
