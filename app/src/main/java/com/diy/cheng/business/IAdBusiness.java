package com.diy.cheng.business;

import com.diy.cheng.model.BannersAdModel;
import com.diy.cheng.model.FullScreenAdModel;
import com.diy.cheng.model.NewsFeedsAdModel;
import com.diy.cheng.network.RequestJsonParams;

/**
 * Created by 0 on 2017/6/16.
 */

public interface IAdBusiness {
    public void getFullScreenAd(RequestJsonParams params, IAdBusinessListener<FullScreenAdModel> listener, Object tag);

    public void getBannersAd(RequestJsonParams params, IAdBusinessListener<BannersAdModel> listener, Object tag);

    public void getNewsFeedsAd(RequestJsonParams params, IAdBusinessListener<NewsFeedsAdModel> listener, Object tag);

    public void toEventTracking(String[] urls);

    public interface IAdBusinessListener<T> {
        void onLoading(Object tag);

        void onLoadError(String message, Object tag);

        void onLoadComplete(T data, Object tag);
    }
}
