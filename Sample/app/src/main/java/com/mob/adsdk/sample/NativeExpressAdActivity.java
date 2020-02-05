package com.mob.adsdk.sample;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.mob.adsdk.nativ.express.ExpressAdInteractionListener;
import com.mob.adsdk.nativ.express.ExpressAdMediaListener;
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
    private CheckBox cbMute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express_ad);
        Button loadAdBtn = findViewById(R.id.loadAd);
        etPadding = findViewById(R.id.et_padding);
        loadAdBtn.setOnClickListener(this);
        loadAdBtn.setText("加载原生模板广告");
        etPosId = findViewById(R.id.et_pos_id);
        cbMute = findViewById(R.id.cb_mute);
        etPosId.setText(MobConstants.native_express_id);
    }

    @Override
    public void onClick(View v) {
        String posId = etPosId.getText().toString();
        String padding = etPadding.getText().toString();
        switch (v.getId()) {
            case R.id.loadAd:
                if (TextUtils.isEmpty(padding)) {
                    if (cbMute.isChecked()){
                        expressAdLoader = new NativeExpressAdLoader(this, new MobADSize(350, MobADSize.AUTO_HEIGHT),
                                new ExpressAdPadding(0), posId, this,cbMute.isChecked());
                    }else {
                        expressAdLoader = new NativeExpressAdLoader(this, new MobADSize(350, MobADSize.AUTO_HEIGHT),
                                posId, this);
                    }
                } else {
                    expressAdLoader = new NativeExpressAdLoader(this, new MobADSize(350, MobADSize.AUTO_HEIGHT),
                            new ExpressAdPadding(Integer.valueOf(padding)), posId, this,cbMute.isChecked());
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
        //在加入视图时必须调用render()方法，否则不进行展示和曝光上报
        this.expressAd.render();
        ((ViewGroup) findViewById(R.id.container)).removeAllViews();
        ((ViewGroup) findViewById(R.id.container)).addView(expressAd.getExpressAdView());
        expressAd.setInteractionListener(new ExpressAdInteractionListener() {
            @Override
            public void onAdClicked() {
                Log.d(TAG, "onAdClicked: 广告被点击");
            }
        });
        expressAd.setExpressAdMediaListener(new ExpressAdMediaListener() {
            @Override
            public void onVideoLoaded() {
                Log.d(TAG, "onVideo: onVideoLoaded");
            }

            @Override
            public void onVideoStart() {
                Log.d(TAG, "onVideo: onVideoStart");
            }

            @Override
            public void onVideoPause() {
                Log.d(TAG, "onVideo: onVideoPause");
            }

            @Override
            public void onVideoResume() {
                Log.d(TAG, "onVideo: onVideoResume");
            }

            @Override
            public void onVideoError(int code, String msg) {
                Log.d(TAG, "onVideo: onVideoError");
                Toast.makeText(NativeExpressAdActivity.this, " code : " + code + "  msg : " + msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onVideoCompleted() {
                Log.d(TAG, "onVideo: onVideoCompleted");
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
        Toast.makeText(this, " code : " + code + "  msg : " + msg, Toast.LENGTH_LONG).show();
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
