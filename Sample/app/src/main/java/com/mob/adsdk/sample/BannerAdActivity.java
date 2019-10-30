package com.mob.adsdk.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mob.adsdk.banner.BannerAd;
import com.mob.adsdk.banner.BannerAdListener;
import com.mob.adsdk.banner.BannerAdLoader;
import com.mob.adsdk.banner.BannerInteractionListener;
import com.mob.adsdk.nativ.express.MobADSize;


public class BannerAdActivity extends Activity implements View.OnClickListener, BannerAdListener {
    private static final String TAG = "BannerADActivity";

    private BannerAdLoader bannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_ad);
        Button bannerAD = findViewById(R.id.loadAd);
        bannerAD.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loadAd:
                EditText etPosId = findViewById(R.id.et_pos_id);
                String posId = etPosId.getText().toString();
                if (!posId.isEmpty()){
                    bannerView = new BannerAdLoader(this,new MobADSize(MobADSize.FULL_WIDTH, MobADSize.AUTO_HEIGHT), posId, this);
                    bannerView.loadAd();
                }else {
                    Toast.makeText(this, "posId 为空", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onLoaded(BannerAd bannerAd) {
        ((ViewGroup) findViewById(R.id.container)).removeAllViews();
        ((ViewGroup) findViewById(R.id.container)).addView(bannerAd.getAdView());
        bannerAd.setInteractionListener(new BannerInteractionListener() {
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
    public void onAdClosed() {
        Log.d(TAG, "onAdClosed: 广告关闭");
    }

    @Override
    public void onError(int code, String msg) {
        Log.d(TAG, "onError: 没有加载到广告");
        Toast.makeText(this, " cdoe : " + code + "  msg : " + msg, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.bannerView != null) {
            this.bannerView.destroy();
        }
    }
}
