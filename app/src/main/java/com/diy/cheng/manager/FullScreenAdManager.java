package com.diy.cheng.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.diy.cheng.business.AdBusiness;
import com.diy.cheng.business.IAdBusiness;
import com.diy.cheng.model.AdsModel;
import com.diy.cheng.model.FullScreenAdModel;
import com.diy.cheng.network.RequestJsonParams;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by 0 on 2017/6/16.
 */

public class FullScreenAdManager implements IAdBusiness.IAdBusinessListener<FullScreenAdModel> {
    private Context context;
    private IAdBusiness adBusiness;
    private IFullScreenAdListener mListener;
    private static final String CACHE_KEY = "ads";
    private static final String FULL_SCREEN_AD_CACHE_KEY = "full_screen_ads";
    private static final int NO_AVAIL_AD = -1;
    FullScreenAdModel fullScreenAdModel;
    Gson gson;

    public FullScreenAdManager(Context context) {
        this.context = context;
        adBusiness = AdBusiness.getInstance();
        gson = new Gson();
    }

    private void getCacheAdList() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CACHE_KEY, Context.MODE_PRIVATE);
        String model = sharedPreferences.getString(FULL_SCREEN_AD_CACHE_KEY, "");
        if (!TextUtils.isEmpty(model)) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(model);
                fullScreenAdModel = gson.fromJson(jsonObject.toString(), FullScreenAdModel.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getAd(RequestJsonParams requestJsonParams, IFullScreenAdListener listener, Object tag) {
        this.mListener = listener;
        getCacheAdList();
        if (fullScreenAdModel != null) {
            int i = isAdsAvail(fullScreenAdModel);
            if (i == NO_AVAIL_AD) {
                Log.e("chengqixiang", "网络获取....");
                adBusiness.getFullScreenAd(requestJsonParams, this, tag);
            } else {
                Log.e("chengqixiang", "缓冲加载....");
                onLoadComplete(fullScreenAdModel, tag, i);
            }
        } else {
            Log.e("chengqixiang", "网络获取....");
            adBusiness.getFullScreenAd(requestJsonParams, this, tag);
        }

    }

    @Override
    public void onLoading(Object tag) {
        if (mListener != null) {
            mListener.onLoading(tag);
        }
    }

    @Override
    public void onLoadError(String message, Object tag) {
        if (mListener != null) {
            mListener.onLoadError(message, tag);
        }
    }

    @Override
    public void onLoadComplete(FullScreenAdModel data, Object tag) {
        // 缓冲，返回可用的广告model
        this.fullScreenAdModel = data;
        int i = isAdsAvail(fullScreenAdModel);
        if (i != NO_AVAIL_AD) {
            onLoadComplete(fullScreenAdModel, tag, i);
        } else {
            if (mListener != null) {
                mListener.onLoadError("无可用广告...", tag);
            }
        }
    }

    private void onLoadComplete(FullScreenAdModel data, Object tag, int checkedNum) {
        // TODO 如果是视频广告，做缓冲
        if (mListener != null) {
            mListener.onLoadComplete(data.data.ads.get(checkedNum), tag);
        }
        saveAds(data);
    }

    private void saveAds(FullScreenAdModel data) {
        String saveResult = data.prase();
        SharedPreferences sharedPreferences = context.getSharedPreferences(CACHE_KEY, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(FULL_SCREEN_AD_CACHE_KEY, saveResult).commit();
    }

    private int isAdsAvail(FullScreenAdModel model) {
        if (model == null) {
            return NO_AVAIL_AD;
        }

        if (model.data.ads == null || model.data.ads.size() <= 0) {
            return NO_AVAIL_AD;
        }

        List<AdsModel> ads = model.data.ads;
        int checkNum = NO_AVAIL_AD;
        for (int i = 0; i < ads.size(); i++) {
            AdsModel adsModel = ads.get(i);
            if (adsModel.maxDisplayed < 0) {
                fullScreenAdModel.data.ads.remove(i);
                continue;
            } else if (adsModel.maxClicked < 0) {
                fullScreenAdModel.data.ads.remove(i);
                continue;
            } /*else if (System.currentTimeMillis() >= adsModel.expireTime * 1000) { // 服务器返回的时间戳是以秒为单位的,现在返回的不对，暂时注释掉
                fullScreenAdModel.data.ads.remove(i);
                continue;
            } */else {    // 如果发现可用的，后边的不检查。是否有问题？？？
                checkNum = i;
                break;
            }
        }
        return checkNum;
    }

    public void adClicked(String adCode) {
        if (TextUtils.isEmpty(adCode)) {
            return ;
        }
        int adCount = fullScreenAdModel.data.ads.size();
        for (int i = 0; i < adCount; i++) {
            if (adCode.contains(fullScreenAdModel.data.ads.get(i).adCode)) {
                // 上传
                adBusiness.toEventTracking(fullScreenAdModel.data.ads.get(i).eventTracking.clickedUrl);
                if (fullScreenAdModel.data.ads.get(i).maxClicked == 1) {
                    fullScreenAdModel.data.ads.get(i).maxClicked -= 2; // 因为服务器返回0代表无限，所以 -= 2；
                } else if (fullScreenAdModel.data.ads.get(i).maxClicked != 0) {
                    fullScreenAdModel.data.ads.get(i).maxClicked--;
                }
            }
        }
        saveAds(fullScreenAdModel);
    }

    public void adDisplayed(String adCode) {
        if (TextUtils.isEmpty(adCode)) {
            return ;
        }
        int adCount = fullScreenAdModel.data.ads.size();
        for (int i = 0; i < adCount; i++) {
            if (adCode.contains(fullScreenAdModel.data.ads.get(i).adCode)) {
                adBusiness.toEventTracking(fullScreenAdModel.data.ads.get(i).eventTracking.displayedUrl);
                if (fullScreenAdModel.data.ads.get(i).maxDisplayed == 1) {
                    fullScreenAdModel.data.ads.get(i).maxDisplayed -= 2; // 因为服务器返回0代表无限，所以 -= 2；
                } else if (fullScreenAdModel.data.ads.get(i).maxDisplayed != 0) {
                    fullScreenAdModel.data.ads.get(i).maxDisplayed--;
                }
            }
        }
        saveAds(fullScreenAdModel);
    }

    public void destroy() {
        // 不想让用户手动调用
    }
}


