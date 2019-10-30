package com.mob.adsdk.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.adsdk.splash.SplashAd;
import com.mob.adsdk.splash.SplashAdListener;
import com.mob.adsdk.splash.SplashAdLoader;
import com.mob.adsdk.splash.SplashInteractionListener;


public class SplashActivity extends Activity implements SplashAdListener {
    private static final String TAG = "SplashActivity";
    private ViewGroup adContainer;
    private TextView tvSkip;
    private TextView tvPld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        adContainer = findViewById(R.id.splash_container);
        tvPld = findViewById(R.id.tv_pld);
		tvSkip = findViewById(R.id.tv_skip);
        if (getIntent().getExtras()!=null){
            String posId = getIntent().getExtras().getString("posId");
            boolean customSkip = getIntent().getExtras().getBoolean("customSkip");
            boolean customBottom = getIntent().getExtras().getBoolean("customBottom");
            if (customBottom){
                tvPld.setVisibility(View.VISIBLE);
            }
            if (customSkip) {
				fetchSplashAD(this, adContainer, tvSkip, posId, this, 3000);
			} else {
            	tvSkip.setVisibility(View.GONE);
				fetchSplashAD(this, adContainer, null, posId, this, 3000);
			}
        }
    }

    private void fetchSplashAD(Activity activity, ViewGroup adContainer,View skipView, String posId, SplashAdListener adListener, int fetchDelay) {
        SplashAdLoader splashAD = new SplashAdLoader(activity, adContainer,skipView, posId, adListener, fetchDelay);
        splashAD.loadAd();
    }

    @Override
    public void onLoaded(SplashAd bannerAd) {
        bannerAd.setInteractionListener(new SplashInteractionListener() {
            @Override
            public void onAdClicked() {
                Log.d(TAG, "onAdClicked: 开屏广告被点击");
            }
        });
    }

    @Override
    public void onAdExposure() {
        Log.d(TAG, "onAdExposure: 开屏广告曝光");
    }

    @Override
    public void onAdClosed() {
        Log.d(TAG, "onAdClosed: 开屏广告被关闭");
        finish();
    }

    @Override
    public void onAdTick(long millisUntilFinished) {
		tvSkip.setText("跳过|"+Integer.valueOf(""+((millisUntilFinished/1000)+1)));
    }

    @Override
    public void onError(int code, String msg) {
        Log.d(TAG, "onError: 没有加载到广告");
        Toast.makeText(this, " cdoe : " + code + "  msg : " + msg, Toast.LENGTH_LONG).show();
    }
}
