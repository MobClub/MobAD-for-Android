package com.mob.adsdk.sample;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.adsdk.nativ.express.ExpressAdInteractionListener;
import com.mob.adsdk.nativ.express.ExpressAdMediaListener;
import com.mob.adsdk.nativ.express.ExpressAdPadding;
import com.mob.adsdk.nativ.express.MobADSize;
import com.mob.adsdk.nativ.express.NativeExpressAd;
import com.mob.adsdk.nativ.express.NativeExpressAdListener;
import com.mob.adsdk.nativ.express.NativeExpressAdLoader;
import com.mob.adsdk.nativ.express.NativeExpressStyle;
import com.mob.adsdk.service.DownAppPolicy;


public class NativeExpressAdActivity extends Activity implements View.OnClickListener, NativeExpressAdListener {
    private static final String TAG = "NativeExpressAdActivity";

    private NativeExpressAdLoader expressAdLoader;
    private NativeExpressAd expressAd;
    private EditText etPosId;
    private EditText etPadding;
    private Switch cbMute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express_ad);
        etPadding = findViewById(R.id.et_padding);
        findViewById(R.id.loadAd).setOnClickListener(this);
        etPosId = findViewById(R.id.et_pos_id);
        cbMute = findViewById(R.id.cb_mute);
        etPosId.setText(MobConstants.native_express_id);
        findViewById(R.id.iv_pos_del).setOnClickListener(this);
        findViewById(R.id.iv_pad_del).setOnClickListener(this);
        findViewById(R.id.ivLeft).setOnClickListener(this);
    }

    /**
     * 谨慎微调，调值太大可能不满足曝光条件，注意！！！
     */
    private NativeExpressStyle getNativeExpressStyle() {
        NativeExpressStyle style = new NativeExpressStyle();

        EditText etBgColor = findViewById(R.id.etBgColor);
        Switch cbHideClose = findViewById(R.id.cbHideClose);

        style.bgColor = getColorValue(etBgColor.getText().toString());
        style.hideClose = cbHideClose.isChecked();

        return style;
    }

    /**
     * 宽度限制最小不低于屏幕80% ，高度自适应
     */
    private MobADSize getMobADSize() {
        MobADSize mobADSize = new MobADSize(350, MobADSize.AUTO_HEIGHT);
        EditText etAdWidth = findViewById(R.id.etAdWidth);
        EditText etAdHeight = findViewById(R.id.etAdHeight);
        String w = etAdWidth.getText().toString();
        String h = etAdHeight.getText().toString();
        int width = TextUtils.isEmpty(w) ? 350 : Integer.valueOf(w);
        int height = TextUtils.isEmpty(h) ? MobADSize.AUTO_HEIGHT : Integer.valueOf(h);

        if ((width < 0 && width != MobADSize.FULL_WIDTH) ||( height < 0) && height != MobADSize.AUTO_HEIGHT) {
            return mobADSize;
        }

        mobADSize = new MobADSize(width, height);

        return mobADSize;
    }

    /**
     *
     * @param editColor
     * @return
     */
    private String getColorValue(String editColor) {
        if (!TextUtils.isEmpty(editColor) && (editColor.length() == 6 || editColor.length() == 8) ) {
            return editColor;
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        String posId = etPosId.getText().toString();
        String padding = etPadding.getText().toString();
        switch (v.getId()) {
            case R.id.loadAd:
                if (TextUtils.isEmpty(etPosId.getText().toString())) {
                    Toast.makeText(this, "广告位ID不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(padding)) {
                    if (cbMute.isChecked()){
                        expressAdLoader = new NativeExpressAdLoader(this, getMobADSize(),
                                new ExpressAdPadding(0), posId, this,cbMute.isChecked());
                    }else {
                        expressAdLoader = new NativeExpressAdLoader(this, getMobADSize(),
                                posId, this);
                    }
                } else {
                    expressAdLoader = new NativeExpressAdLoader(this, getMobADSize(),
                            new ExpressAdPadding(Integer.valueOf(padding)), posId, this,cbMute.isChecked());
                }
                //设置下载类型是否二次弹窗确认，默认为非wifi下弹窗
                expressAdLoader.setDownAPPConfirmPolicy(DownAppPolicy.Confirm);
                expressAdLoader.setNativeExpressStyle(getNativeExpressStyle());
                expressAdLoader.loadAd();
                break;
            case R.id.iv_pad_del:
                etPadding.setText("");
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
