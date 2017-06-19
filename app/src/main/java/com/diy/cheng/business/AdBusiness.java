package com.diy.cheng.business;

import android.util.Log;

import com.diy.cheng.model.BannersAdModel;
import com.diy.cheng.model.FullScreenAdModel;
import com.diy.cheng.model.NewsFeedsAdModel;
import com.diy.cheng.network.RequestJsonParams;

import org.json.JSONObject;

/**
 * Created by 0 on 2017/6/16.
 */

public class AdBusiness extends AdBaseBusiness implements IAdBusiness {
    private static final int FULL_SCREENS_AD_TYPE = 4;
    private static final int BANNERS_AD_TYPE = 5;
    private static final int NEWS_FEEDS_AD_TYPE = 6;
    private static AdBusiness instance = null;

    private AdBusiness() {
        super();
    }

    public static AdBusiness getInstance() {
        if (instance == null) {
            synchronized (AdBusiness.class) {
                if (instance == null) {
                    instance = new AdBusiness();
                }
            }
        }
        return instance;
    }

    @Override
    public void getFullScreenAd(RequestJsonParams params, IAdBusiness.IAdBusinessListener<FullScreenAdModel> listener, Object tag) {
//        if () // 读缓冲 -- 过期时间 sharepreference，在线url，是否下载下来，本地文件（磁盘缓冲）
        initBusiness(tag, FULL_SCREENS_AD_TYPE, listener);
        dataSource.setJsonParams(params);
        dataSource.enqueue();
    }

    @Override
    public void getBannersAd(RequestJsonParams params, IAdBusinessListener<BannersAdModel> listener, Object tag) {
        initBusiness(tag, BANNERS_AD_TYPE, listener);
        dataSource.setJsonParams(params);
        dataSource.enqueue();
    }

    @Override
    public void getNewsFeedsAd(RequestJsonParams params, IAdBusinessListener<NewsFeedsAdModel> listener, Object tag) {
        initBusiness(tag, NEWS_FEEDS_AD_TYPE, listener);
        dataSource.setJsonParams(params);
        dataSource.enqueue();
    }

    @Override
    public void toEventTracking(String[] urls) {
        dataSource.eventTracking(urls);
    }

    @Override
    protected void onLoadComplete(JSONObject jsonObject) {
        Log.e("chengqixiang", "response === " + jsonObject.toString());
        if (mBussinessType == FULL_SCREENS_AD_TYPE) {
            FullScreenAdModel fullScreenAdModel = gson.fromJson(jsonObject.toString(), FullScreenAdModel.class);
            post(fullScreenAdModel);
        } else if (mBussinessType == BANNERS_AD_TYPE) {
            BannersAdModel bannersAdModel = gson.fromJson(jsonObject.toString(), BannersAdModel.class);
            post(bannersAdModel);
        } else if (mBussinessType == NEWS_FEEDS_AD_TYPE) {
            NewsFeedsAdModel newsFeedsAdModel = gson.fromJson(jsonObject.toString(), NewsFeedsAdModel.class);
            post(newsFeedsAdModel);
        }
    }
}
