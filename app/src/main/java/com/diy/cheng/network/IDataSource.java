package com.diy.cheng.network;

/**
 * Created by 0 on 2017/6/15.
 */

public interface IDataSource {

    void setDataSourceListener(IDataSourceListener listener);

    void cancel();

    public interface IDataSourceListener {
        public void onLoading();

        /**
         * 数据请求返回成功
         * @param data 返回的数据
         */
        public void onDataComplete(String data);
        /**
         * 数据请求返回失败
         */
        public void onDataFail(String error);
    }

}
