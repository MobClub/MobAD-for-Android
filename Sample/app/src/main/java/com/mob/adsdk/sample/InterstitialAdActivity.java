package com.mob.adsdk.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mob.adsdk.interstitial.InteractionListener;
import com.mob.adsdk.interstitial.InterstitialAd;
import com.mob.adsdk.interstitial.InterstitialAdListener;
import com.mob.adsdk.interstitial.InterstitialAdLoader;


public class InterstitialAdActivity extends Activity implements View.OnClickListener, InterstitialAdListener {
    private static final String TAG = "InterstitialADActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial_ad);
        findViewById(R.id.showInterstitailAD).setOnClickListener(this);
    }

    private InterstitialAdLoader interstitialAD;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showInterstitailAD:
                EditText etPosId = findViewById(R.id.et_pos_id);
                String posId = etPosId.getText().toString();
                if (!posId.isEmpty()){
                    interstitialAD = new InterstitialAdLoader(this, posId, this);
                    interstitialAD.loadAd();
                }else {
                    Toast.makeText(this, "posId 为空", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onAdLoaded(InterstitialAd interstitialAd) {
        Log.d(TAG, "onAdLoaded: ");
        interstitialAd.setInteractionListener(new InteractionListener() {
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
    public void onAdError(int code, String msg) {
        Log.d(TAG, "onAdError: 没有加载到广告");
        Toast.makeText(this, " cdoe : " + code + "  msg : " + msg, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (interstitialAD != null) {
            interstitialAD.destroy();
        }
    }
}
