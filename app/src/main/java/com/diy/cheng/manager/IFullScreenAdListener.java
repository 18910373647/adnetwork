package com.diy.cheng.manager;

import com.diy.cheng.model.AdsModel;

/**
 * Created by 0 on 2017/6/16.
 */

public interface IFullScreenAdListener {
    void onLoading(Object tag);

    void onLoadError(String message, Object tag);

    void onLoadComplete(AdsModel data, Object tag);
}
