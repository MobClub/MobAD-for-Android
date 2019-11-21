package com.mob.adsdk.sample;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mob.adsdk.reward.MobRewardVideo;
import com.mob.adsdk.reward.RewardOption;
import com.mob.adsdk.reward.RewardVideoAdListener;
import com.mob.adsdk.reward.RewardVideoAdLoader;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;


public class RewardVideoAdActivity extends Activity implements View.OnClickListener, RewardVideoAdListener {
    private static final String TAG = "RewardVideoAdActivity";

    private RewardVideoAdLoader rewardVideoAdLoader;
    private EditText editText;
    private Button btnChangeOrientation;
    private int currentOrientation;
    private boolean isAllowShow = true;
    private MobRewardVideo rewardVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewardvideo_ad);
        Button rewardAd = findViewById(R.id.loadRewardAd);
        rewardAd.setOnClickListener(this);
        editText = findViewById(R.id.et_pos_id);
        editText.setText(MobConstants.reward_id);
        btnChangeOrientation = findViewById(R.id.changeOrientation);
        btnChangeOrientation.setOnClickListener(this);
        currentOrientation = getResources().getConfiguration().orientation;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changeOrientation:
                if (currentOrientation == ORIENTATION_PORTRAIT) {
                    currentOrientation = ORIENTATION_LANDSCAPE;
                    btnChangeOrientation.setText("当前是竖屏-切换横屏");
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else if (currentOrientation == ORIENTATION_LANDSCAPE) {
                    currentOrientation = ORIENTATION_PORTRAIT;
                    btnChangeOrientation.setText("当前是横屏-切换竖屏");
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                break;
            case R.id.loadRewardAd:
				String posID = MobConstants.reward_id;
				if (!editText.getText().toString().isEmpty()){
					posID = editText.getText().toString();
				}
                RewardOption rewardOption = new RewardOption.Builder(this)
                        .setOrientation(currentOrientation)
                        .setSkipLongTime(true)
                        .setUserId("userid")
                        .setRewardName("金币")//奖励的名称
                        .setRewardAmount(3).build(); //奖励的数量
                rewardVideoAdLoader = new RewardVideoAdLoader(this, posID, this,rewardOption);
                rewardVideoAdLoader.loadAd();
                Toast.makeText(this,"广告开始加载",Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onVideoCached() {
        Log.d(TAG, "onVideoCached");
        if (rewardVideo != null && isAllowShow){
            rewardVideo.showAd();
        }
    }

    @Override
    public void onAdLoad(MobRewardVideo rewardVideo) {
        Log.d(TAG, "onAdLoad: 广告加载成功 ");
        this.rewardVideo = rewardVideo;
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
        Toast.makeText(this, " code : " + code + "  msg : " + msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAdClick() {
        Log.d(TAG, "onAdClick: 广告点击");
    }

    @Override
    protected void onDestroy() {
        isAllowShow = false;
        super.onDestroy();
    }
}
