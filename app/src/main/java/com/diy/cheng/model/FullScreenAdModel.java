package com.diy.cheng.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by 0 on 2017/6/16.
 */

public class FullScreenAdModel extends AdBaseModel {
    public String requestId;
    public AdDataModel data;

    public String prase() {
        try {
            JSONObject rootJson = new JSONObject();
            rootJson.put("requestId", this.requestId);
            rootJson.put("result", this.result);
            rootJson.put("errorCode", this.errorCode);
            rootJson.put("message", this.message);

            JSONObject dataJson = new JSONObject();
            dataJson.put("positionId", this.data.positionId);

            List<AdsModel> list = data.ads;
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                JSONObject adJson = new JSONObject();
                AdsModel adsModel = list.get(i);
                adJson.put("adCode", adsModel.adCode);
                adJson.put("displayTime", adsModel.displayTime);
                adJson.put("expireTime", adsModel.expireTime);
                adJson.put("maxDisplayed", adsModel.maxDisplayed);
                adJson.put("maxClicked", adsModel.maxClicked);
                adJson.put("maxDownloaded", adsModel.maxDownloaded);
                adJson.put("maxBought", adsModel.maxBought);
                adJson.put("maxInstalled", adsModel.maxInstalled);
                adJson.put("reportFrequency", adsModel.reportFrequency);
                adJson.put("title", adsModel.title);
                adJson.put("description", adsModel.description);
                adJson.put("fileType", adsModel.fileType);

                JSONObject imageJson = new JSONObject();
                JSONObject iconJson = new JSONObject();
                iconJson.put("url", adsModel.image.icon.url);
                iconJson.put("width", adsModel.image.icon.width);
                iconJson.put("height", adsModel.image.icon.height);
                JSONObject bigJson = new JSONObject();
                bigJson.put("url", adsModel.image.big.url);
                bigJson.put("width", adsModel.image.big.width);
                bigJson.put("height", adsModel.image.big.height);
                imageJson.put("icon", iconJson);
                imageJson.put("big", bigJson);
                adJson.put("image", imageJson);

                adJson.put("toView", adsModel.toView);

                JSONObject eventJson = new JSONObject();
                JSONArray displayedJsonArray = new JSONArray();
                for (int j = 0; j <adsModel.eventTracking.displayedUrl.length; j++) {
                    displayedJsonArray.put(j, adsModel.eventTracking.displayedUrl[j]);
                }
                JSONArray clickedJsonArray = new JSONArray();
                for (int j = 0; j <adsModel.eventTracking.clickedUrl.length; j++) {
                    clickedJsonArray.put(j, adsModel.eventTracking.clickedUrl[j]);
                }
                eventJson.put("displayedUrl", displayedJsonArray);
                eventJson.put("clickedUrl", clickedJsonArray);
                adJson.put("eventTracking", eventJson);

                jsonArray.put(i, adJson);
            }

            dataJson.put("ads", jsonArray);
            rootJson.put("data", dataJson);

            return rootJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

}
