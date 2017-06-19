package com.diy.cheng.network;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.UUID;

/**
 * 使用建造者模式吧
 * Created by 0 on 2017/6/15.
 */

public class RequestJsonParams {
    Context context;
    String requestUrl;
    static final String format = "%1$s?%2$s";
//    requestId	string	是	区分每次请求的唯一标识（UUID）
//    positionId	string	是	广告位编号由后台创建，用于区分广告投放位置
//    ip	string	是	外网IP
//    OS	string	是	操作系统	ios,android
//    ISO	string	是	国家代码：ISO-3166-1alpha-3中表明的国家代码
//    ISP	string	是	运营商名称	ChinaMobile,ChinaUnicom,ChinaTelecom
//    DPI	string	是	屏幕分辨率	800*100
//    OSVer	string	是	系统版本号
//    model	string	是	机型
//    locale	string	是	移动设备中的locale属性值
//    channel	string	是	渠道号
//    netWork	string	是	当前使用的网络	3G，WIFI
//    clientVer	string	是	客户端版本号
//    device	string	是	设备	iPhone,iPad,Android,AndroidTV,AndroidPad
//    deviceId	string	是	设备ID（安卓的AndroId ID，IOS的大写IDFA）
//    userAgent	string	是	移动设备WebView User-Angent值
//    advertisingId	string	是	Google Advertising ID（安卓）
//    latitude	string	是	GPS经度
//    longitude	string	是	GPS纬度
//    accuracy	string	是	经纬度的精确度
//    VV	string	否	VV值（TV必传）
//    userId	string	否	用户ID
//    macAddress	string	否	移动设备mac地址
//    IMEI	string	否	设备IMEI
//    isFirst	number	否	是否是当天首次请求广告
//
    private RequestJsonParams(Context context, JSONObject jsonObject, String server, boolean userDefault) {
        this.context = context;
        JSONObject json = jsonObject;
        if (userDefault) {
            try {
                json = createDefault(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            Iterator<String> iterator = json.keys();
            StringBuffer sb = null;
            String tmp = null;
            while (iterator.hasNext()) {
                if (sb == null) {
                    sb = new StringBuffer();
                } else {
                    sb.append("&");
                }
                tmp = iterator.next();
                sb.append(tmp);
                sb.append("=");
                sb.append(json.get(tmp).toString());
            }
            requestUrl = String.format(format, server, sb.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    private JSONObject createDefault(JSONObject jsonObject) throws JSONException {
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        jsonObject.put("requestId", UUID.randomUUID().toString());  // 区分每次请求的唯一标识（UUID）
        jsonObject.put("positionId", "1");   // 广告位编号由后台创建，用于区分广告投放位置
        jsonObject.put("ip", "124.193.159.226");   // 外网IP
        jsonObject.put("OS", "Android"); // 操作系统
//        jsonObject.put("ISO", Utils.getIso()); // 国家代码
        jsonObject.put("ISP", Utils.getIsp(context)); // 运营商名称
        jsonObject.put("DPI", Utils.getDpi(context)); // 屏幕分辨率
        jsonObject.put("OSVer", Utils.getOsVersion()); // 系统版本号
        jsonObject.put("model", Utils.getModel()); // 机型
        jsonObject.put("locale", Utils.getLocale()); // 移动设备中的locale属性值
        jsonObject.put("channel", "TLKG17"); // channel
        jsonObject.put("netWork", Utils.getNetwork(context)); // 当前使用的网络	3G，WIFI
        jsonObject.put("clientVer", "5.1.0"); // 客户端版本号
        jsonObject.put("device", "Android"); // 设备	iPhone,iPad,Android,AndroidTV,AndroidPad
        jsonObject.put("deviceId", Utils.getDeviceId(context)); // 设备ID
        jsonObject.put("userAgent", Utils.getUserAngent(context)); // 移动设备WebView User-Angent值
        jsonObject.put("IMEI", "123456789123456");
//        jsonObject.put("advertisingId", Utils.getAdvertisingId(context)); // Google Advertising ID（安卓）
//        jsonObject.put("latitude", Utils.getLatitude(context)); // GPS经度
//        jsonObject.put("longitude", Utils.getLongitude(context)); // GPS纬度
//        jsonObject.put("accuracy", "");
        return jsonObject;
    }

    public static class Builder {
        JSONObject jsonObject;
        String server;
        boolean userDefault;
        Context context;

        public Builder(Context context) {
            this.context = context;
            userDefault = false;
        }

        public Builder setJson(JSONObject json) {
            this.jsonObject  = json;
            return this;
        }

        public Builder setServer(String server) {
            this.server = server;
            return this;
        }

        public Builder userDefault() {
            userDefault = true;
            return this;
        }

        public RequestJsonParams build() {
            return new RequestJsonParams(context, jsonObject, server, userDefault);
        }
    }
}
