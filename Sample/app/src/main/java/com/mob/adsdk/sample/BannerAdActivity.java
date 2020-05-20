package com.mob.adsdk.sample;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
    private ViewGroup adContainer;
    private EditText etRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_ad);
        etPosId = findViewById(R.id.et_pos_id);
        etPosId.setText(MobConstants.banner_id);
        adContainer = findViewById(R.id.container);
        etRefresh = findViewById(R.id.et_refresh);

        findViewById(R.id.loadAd).setOnClickListener(this);
        findViewById(R.id.iv_pos_del).setOnClickListener(this);
        findViewById(R.id.ivLeft).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loadAd:
                if (TextUtils.isEmpty(etPosId.getText().toString())) {
                    Toast.makeText(this, "广告位ID不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                String etRefreshTimes = etRefresh.getText().toString();
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
                    loading = true;
                    bannerView = new BannerAdLoader(this,adContainer,new MobADSize(MobADSize.FULL_WIDTH, MobADSize.AUTO_HEIGHT), posId, this);

                    if (!TextUtils.isEmpty(etRefreshTimes)) {
                        bannerView.setRefresh(Integer.valueOf(etRefreshTimes));
                    }
                    bannerView.loadAd();
                }else {
                    Toast.makeText(this, "posId 为空", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.iv_pos_del:
                etPosId.setText("");
                break;
            case R.id.ivLeft:
                finish();
                break;
        }
    }

    @Override
    public void onLoaded(BannerAd bannerAd) {
        adContainer.removeAllViews();
        adContainer.addView(bannerAd.getAdView());
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
