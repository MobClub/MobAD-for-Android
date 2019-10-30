package com.mob.adsdk.sample;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mob.adsdk.reward.MobRewardVideo;
import com.mob.adsdk.reward.RewardVideoAdListener;
import com.mob.adsdk.reward.RewardVideoAdLoader;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;


public class RewardVideoAdActivity extends Activity implements View.OnClickListener, RewardVideoAdListener {
    private static final String TAG = "RewardVideoAdActivity";

    private RewardVideoAdLoader rewardVideoAdLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewardvideo_ad);
        Button rewardAd = findViewById(R.id.loadRewardAd);
        rewardAd.setOnClickListener(this);
        findViewById(R.id.changeOrientation).setOnClickListener(this);

        rewardVideoAdLoader = new RewardVideoAdLoader(this, "1005336", this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changeOrientation:
                int currentOrientation = getResources().getConfiguration().orientation;
                if (currentOrientation == ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else if (currentOrientation == ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                break;
            case R.id.loadRewardAd:
                rewardVideoAdLoader.setOrientation(getResources().getConfiguration().orientation)// 设置期望视频播放的方向
                        //必传参数，表来标识应用侧唯一用户；若非服务器回调模式或不需sdk透传
                        //可设置为空字符串
                        .setUserID("userid")
                        .setRewardName("金币")//奖励的名称
                        .setRewardAmount(3) //奖励的数量
                        .loadAd();
                break;
        }
    }

    @Override
    public void onVideoCached() {
        Log.d(TAG, "onVideoCached");
    }

    @Override
    public void onAdLoad(MobRewardVideo rewardVideo) {
        Log.d(TAG, "onAdLoad: 广告加载成功");
		rewardVideo.showAd();
    }

    @Override
    public void onAdShow() {
        Log.d(TAG, "onAdShow: 广告展示");
    }

    @Override
    public void onAdExpose() {
        Log.d(TAG, "onAdExpose: 广告曝光");
    }

    @Override
    public void onReward(boolean rewardVerify, int rewardAmount, String rewardName) {
        Log.d(TAG, "onReward: 广告激励");
    }

    @Override
    public void onVideoComplete() {
        Log.d(TAG, "onVideoComplete: 广告播放完成");
    }

    @Override
    public void onAdClosed() {
        Log.d(TAG, "onAdClosed: 广告关闭");
    }

    @Override
    public void onAdError(int code, String msg) {
        Log.d(TAG, "onError: 没有加载到广告");
        Toast.makeText(this, " cdoe : " + code + "  msg : " + msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAdClick() {
        Log.d(TAG, "onAdClick: 广告点击");
    }

}
