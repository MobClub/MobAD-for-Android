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
    private EditText etPosId;
    private boolean loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_ad);
        etPosId = findViewById(R.id.et_pos_id);
        etPosId.setText(MobConstants.banner_id);

        Button bannerAD = findViewById(R.id.loadAd);
        bannerAD.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loadAd:
                String posId = etPosId.getText().toString();
                if (!posId.isEmpty()){
                    if (this.bannerView != null) { //加载之前先销毁之前的视图
                        this.bannerView.destroy();
                    }
                    //回调结果返回前不要重复loadAd
                    if (loading) {
                        Toast.makeText(this, "正在加载广告，不要急!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    bannerView = new BannerAdLoader(this,new MobADSize(MobADSize.FULL_WIDTH, MobADSize.AUTO_HEIGHT), posId, this);
                    bannerView.loadAd();
                    loading = true;
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
        loading = false;
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
        Toast.makeText(this, " code : " + code + "  msg : " + msg, Toast.LENGTH_LONG).show();
        loading = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.bannerView != null) {
            this.bannerView.destroy();
        }
    }
}
