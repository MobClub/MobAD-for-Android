package com.mob.adsdk.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.adsdk.sample.utils.SPUtils;
import com.mob.adsdk.splash.SplashAd;
import com.mob.adsdk.splash.SplashAdListener;
import com.mob.adsdk.splash.SplashAdLoader;
import com.mob.adsdk.splash.SplashInteractionListener;


public class SplashActivity extends Activity implements SplashAdListener , View.OnClickListener {
    private static final String TAG = "SplashActivity";
    private ViewGroup adContainer;
    private TextView tvSkip;
    private TextView tvPld;
    public boolean canJump = false;
    private boolean needStartDemoList;

    /**
     * 为防止无广告时造成视觉上类似于"闪退"的情况，设定无广告时页面跳转根据需要延迟一定时间，demo
     * 给出的延时逻辑是从拉取广告开始算开屏最少持续多久，仅供参考，开发者可自定义延时逻辑，如果开发者采用demo
     * 中给出的延时逻辑，也建议开发者考虑自定义minSplashTimeWhenNoAD的值（单位ms）
     **/
    private int minSplashTimeWhenNoAD = 1000;
    /**
     * 记录拉取广告的时间
     */
    private long fetchSplashADTime = 0;
    private Handler handler = new Handler(Looper.getMainLooper());

    private boolean fetchOnly;

    private Button btnFinish,btnShowAD,btnRefresh;
    private SplashAd splashAd;
    private SplashAdLoader splashAdLoader;
    private TextView tv;
    private ViewGroup btnGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        adContainer = findViewById(R.id.splash_container);
        btnFinish = findViewById(R.id.btn_finish);
        btnGroup = findViewById(R.id.group);
        btnShowAD = findViewById(R.id.btn_showad);
        btnRefresh = findViewById(R.id.btn_refresh);
        tv = findViewById(R.id.tv_ad_status);
        btnFinish.setOnClickListener(this);
        btnShowAD.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        tvPld = findViewById(R.id.tv_pld);
        tvSkip = findViewById(R.id.tv_skip);
        if (getIntent().getExtras()!=null && getIntent().getExtras().getString("posId") != null){
            String posId = getIntent().getExtras().getString("posId");
            boolean customSkip = getIntent().getExtras().getBoolean("customSkip");
            boolean customBottom = getIntent().getExtras().getBoolean("customBottom");
            fetchOnly = getIntent().getExtras().getBoolean("fetchOnly",false);
            if (customBottom){
                tvPld.setVisibility(View.VISIBLE);
            }
            if (customSkip) {
                fetchSplashAD(this, adContainer, tvSkip, posId, this, 3000);
            } else {
                tvSkip.setVisibility(View.GONE);
                fetchSplashAD(this, adContainer, null, posId, this, 3000);
            }
        } else {
            needStartDemoList = true;
            tvSkip.setVisibility(View.GONE);
            fetchSplashAD(this, adContainer, null, MobConstants.splash_id, this, 3000);
        }
    }

    private void fetchSplashAD(Activity activity, ViewGroup adContainer,View skipView, String posId, SplashAdListener adListener, int fetchDelay) {
        if (!SPUtils.getBoolean(this,SPUtils.PRIVACY_POLICY_KEY,false) && getIntent().getExtras() == null) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (needStartDemoList) {
                        SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }
                    SplashActivity.this.finish();
                }
            }, 200);
            return;
        }
        fetchSplashADTime = System.currentTimeMillis();
        splashAdLoader = new SplashAdLoader(activity, adContainer,skipView, posId, adListener, fetchDelay);
        if (fetchOnly){
            tv.setText("广告正在请求");
            splashAdLoader.fetchOnly();
            if (btnGroup!=null){
                btnGroup.setVisibility(View.VISIBLE);
            }
        }else {
            splashAdLoader.loadAd();
        }
    }

    @Override
    public void onLoaded(SplashAd splashAd) {
        this.splashAd = splashAd;
        tv.setText("广告已缓存");
        splashAd.setInteractionListener(new SplashInteractionListener() {
            @Override
            public void onAdClicked() {
                Log.d(TAG, "onAdClicked: 开屏广告被点击");
            }
        });
    }

    @Override
    public void onAdExposure() {
        if (btnGroup!=null){
            btnGroup.setVisibility(View.GONE);
        }
        Log.d(TAG, "onAdExposure: 开屏广告曝光");
    }

    @Override
    public void onAdClosed() {
        Log.d(TAG, "onAdClosed: 开屏广告被关闭");
        Toast.makeText(this, "开屏广告被关闭", Toast.LENGTH_LONG).show();
        next();
    }

    @Override
    public void onAdTick(long millisUntilFinished) {
        tvSkip.setText("跳过|"+Integer.valueOf(""+((millisUntilFinished/1000)+1)));
    }

    @Override
    public void onError(int code, String msg) {
        Log.d(TAG, "onError: 没有加载到广告");
        Toast.makeText(this, " code : " + code + "  msg : " + msg, Toast.LENGTH_LONG).show();
        /**
         * 为防止无广告时造成视觉上类似于"闪退"的情况，设定无广告时页面跳转根据需要延迟一定时间，demo
         * 给出的延时逻辑是从拉取广告开始算开屏最少持续多久，仅供参考，开发者可自定义延时逻辑，如果开发者采用demo
         * 中给出的延时逻辑，也建议开发者考虑自定义minSplashTimeWhenNoAD的值
         **/
        long alreadyDelayMills = System.currentTimeMillis() - fetchSplashADTime;//从拉广告开始到onNoAD已经消耗了多少时间
        long shouldDelayMills = alreadyDelayMills > minSplashTimeWhenNoAD ? 0 : minSplashTimeWhenNoAD
                - alreadyDelayMills;//为防止加载广告失败后立刻跳离开屏可能造成的视觉上类似于"闪退"的情况，根据设置的minSplashTimeWhenNoAD
        // 计算出还需要延时多久
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (needStartDemoList) {
                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                SplashActivity.this.finish();
            }
        }, shouldDelayMills);
    }

    private void next() {
        if (canJump) {
            if (needStartDemoList) {
                this.startActivity(new Intent(this, MainActivity.class));
            }
            this.finish();
        } else {
            canJump = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        canJump = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (canJump) {
            next();
        }
        canJump = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    /** 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_finish:
                finish();
                break;
            case R.id.btn_showad:
                if (splashAd != null){
                    splashAd.showAd();
                }
                break;
            case R.id.btn_refresh:
                if (splashAdLoader != null){
                    tv.setText("广告正在请求");
                    splashAdLoader.fetchOnly();
                }
                break;
            default:
                break;
        }
    }
}
