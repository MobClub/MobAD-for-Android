package com.mob.adsdk.sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.mob.adsdk.fullscreen.FullScreenAd;
import com.mob.adsdk.fullscreen.FullScreenAdListener;
import com.mob.adsdk.fullscreen.FullScreenAdLoader;

public class FullScreenVideoActivity extends Activity implements View.OnClickListener, FullScreenAdListener {

    private FullScreenAd fullScreenAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_video);
        initClickEvent();
    }

    private void initClickEvent(){
        findViewById(R.id.btn_reward_load).setOnClickListener(this);
        findViewById(R.id.btn_reward_load_vertical).setOnClickListener(this);
        findViewById(R.id.btn_reward_show).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FullScreenAdLoader fullScreenAdLoader = null;
        switch (v.getId()){
            case R.id.btn_reward_load:
                Toast.makeText(this,"横屏广告加载",Toast.LENGTH_SHORT).show();
                fullScreenAdLoader = new FullScreenAdLoader(this,"1005337",this,FullScreenAdLoader.HORIZONTAL);
                fullScreenAdLoader.loadAd();
                break;
            case R.id.btn_reward_load_vertical:
                Toast.makeText(this,"竖屏广告加载",Toast.LENGTH_SHORT).show();
                fullScreenAdLoader = new FullScreenAdLoader(this,"1005337",this,FullScreenAdLoader.VERTICAL);
                fullScreenAdLoader.loadAd();
                break;
            case R.id.btn_reward_show:
                if (fullScreenAd==null){
                    Toast.makeText(this,"fullScreenAd null",Toast.LENGTH_SHORT).show();
                }else {
                    fullScreenAd.showAd(this);
                    fullScreenAd = null;
                }
                break;
        }
    }

    @Override
    public void onLoaded(FullScreenAd fullScreenAd) {
        Toast.makeText(this,"广告已就绪",Toast.LENGTH_SHORT).show();
        this.fullScreenAd= fullScreenAd;
    }

    @Override
    public void onAdExposure() {
        Toast.makeText(this,"广告已曝光",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSkippedVideo() {
        Toast.makeText(this,"广告已跳过",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdClosed() {
        Toast.makeText(this,"广告已关闭",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onVideoComplete() {
        Toast.makeText(this,"onVideoComplete",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdVideoBarClick() {
        Toast.makeText(this,"onAdVideoBarClick",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(int code, String msg) {
        Toast.makeText(this, " cdoe : " + code + "  msg : " + msg, Toast.LENGTH_LONG).show();
    }
}
