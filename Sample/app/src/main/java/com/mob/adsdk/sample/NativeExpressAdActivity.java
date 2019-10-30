package com.mob.adsdk.sample;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mob.adsdk.nativ.express.ExpressAdInteractionListener;
import com.mob.adsdk.nativ.express.ExpressAdPadding;
import com.mob.adsdk.nativ.express.MobADSize;
import com.mob.adsdk.nativ.express.NativeExpressAd;
import com.mob.adsdk.nativ.express.NativeExpressAdListener;
import com.mob.adsdk.nativ.express.NativeExpressAdLoader;


public class NativeExpressAdActivity extends Activity implements View.OnClickListener, NativeExpressAdListener {
    private static final String TAG = "NativeExpressAdActivity";

    private NativeExpressAdLoader expressAdLoader;
    private NativeExpressAd expressAd;
    private EditText etPosId;
    private EditText etPadding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_ad);
        Button loadAdBtn = findViewById(R.id.loadAd);
        findViewById(R.id.tv_padding).setVisibility(View.VISIBLE);
        etPadding = findViewById(R.id.et_padding);
        etPadding.setVisibility(View.VISIBLE);
        loadAdBtn.setOnClickListener(this);
        loadAdBtn.setText("加载原生模板广告");
        etPosId = findViewById(R.id.et_pos_id);
        etPosId.setText("1005296");
    }

    @Override
    public void onClick(View v) {
        String posId = etPosId.getText().toString();
        String padding = etPadding.getText().toString();
        switch (v.getId()) {
            case R.id.loadAd:
                if (TextUtils.isEmpty(padding)) {
                    expressAdLoader = new NativeExpressAdLoader(this, new MobADSize(350, MobADSize.AUTO_HEIGHT),
                            posId, this);
                } else {
                    expressAdLoader = new NativeExpressAdLoader(this, new MobADSize(350, MobADSize.AUTO_HEIGHT),
                            new ExpressAdPadding(Integer.valueOf(padding)), posId, this);
                }
                expressAdLoader.loadAd();
                break;
        }
    }

    @Override
    public void onAdLoaded(NativeExpressAd expressAd) {
        if (this.expressAd != null) {
            this.expressAd.destroy();
        }
        this.expressAd = expressAd;
        ((ViewGroup) findViewById(R.id.container)).removeAllViews();
        ((ViewGroup) findViewById(R.id.container)).addView(expressAd.getExpressAdView());
        expressAd.setInteractionListener(new ExpressAdInteractionListener() {
            @Override
            public void onAdClicked() {
                Log.d(TAG, "onAdClicked: 广告被点击");
            }
        });
    }

    @Override
    public void onAdExposure() {
        Log.d(TAG, "onAdExposure: 广告曝光");
    }

    @Override
    public void onAdError(int code, String msg) {
        Log.d(TAG, "onError: 没有加载到广告");
        Toast.makeText(this, " cdoe : " + code + "  msg : " + msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAdClosed() {
        Log.d(TAG, "onAdClosed: 广告关闭");
    }

    @Override
    public void onRenderFail() {
        Log.d(TAG, "onRenderFail: 广告渲染失败");
    }

    @Override
    public void onRenderSuccess() {
        Log.d(TAG, "onRenderSuccess: 广告渲染成功");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.expressAd != null) {
            this.expressAd.destroy();
        }
    }
}
