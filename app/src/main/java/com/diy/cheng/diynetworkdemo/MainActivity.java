package com.diy.cheng.diynetworkdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.diy.cheng.manager.FullScreenAdManager;
import com.diy.cheng.manager.IFullScreenAdListener;
import com.diy.cheng.model.AdsModel;
import com.diy.cheng.network.Constants;
import com.diy.cheng.network.RequestJsonParams;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn;
    RequestJsonParams requestJsonParams;
    ImageView imageView;
    FullScreenAdManager adManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adManager = new FullScreenAdManager(this);

        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(this);
        imageView = (ImageView) findViewById(R.id.img);

        requestJsonParams = new RequestJsonParams.Builder(this)
                .setJson(null)
                .setServer(Constants.FULL_SCREENS_AD_API)
                .userDefault()
                .build();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn) {
            adManager.getAd(requestJsonParams, new IFullScreenAdListener() {
                @Override
                public void onLoading(Object tag) {

                }

                @Override
                public void onLoadError(String message, Object tag) {
                    Toast.makeText(MainActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onLoadComplete(AdsModel data, Object tag) {
                    Toast.makeText(MainActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
                    // TODO 广告具体数据
                }
            }, "");
        }
    }
}
