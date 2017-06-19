package com.diy.cheng.network;

/**
 * Created by 0 on 2017/6/15.
 */

public interface IAdNetworkDataSource extends IDataSource {
    public void setJsonParams(RequestJsonParams params);

    public void execute();

    public void enqueue();

    public void eventTracking(String[] urls);
}
