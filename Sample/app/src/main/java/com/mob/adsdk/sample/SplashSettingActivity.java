package com.mob.adsdk.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.adsdk.splash.SplashAdLoader;

public class SplashSettingActivity extends Activity implements View.OnClickListener {

    private TextView btnJumpToSplash;
    private TextView btnJumpToSplashForSkip;
    private TextView btnPreload;
    private TextView btnJumpToSplashForBottom;
    private EditText etPosId;
    private TextView btnJumpToSplashForFetchOnly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_setting);

        btnJumpToSplash = findViewById(R.id.btn_jumptosplash);
        btnJumpToSplashForSkip = findViewById(R.id.btn_jumptosplashforskip);
        btnPreload = findViewById(R.id.btn_preload);
        btnJumpToSplashForBottom = findViewById(R.id.btn_jumptosplashforbottom);
        btnJumpToSplashForFetchOnly = findViewById(R.id.btn_fetch_only);
        btnJumpToSplashForFetchOnly.setOnClickListener(this);
        btnPreload.setOnClickListener(this);
        btnJumpToSplashForBottom.setOnClickListener(this);
        btnJumpToSplashForSkip.setOnClickListener(this);
        btnJumpToSplash.setOnClickListener(this);
        findViewById(R.id.ivLeft).setOnClickListener(this);

        etPosId = findViewById(R.id.et_pos_id);
        etPosId.setText(MobConstants.splash_id);

        findViewById(R.id.iv_pos_del).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(SplashSettingActivity.this, SplashActivity.class);
        String posId = etPosId.getText().toString();
        intent.putExtra("posId", posId);
        intent.putExtra("customSkip", false);
        intent.putExtra("customBottom", true);
        switch (v.getId()) {
            case R.id.btn_jumptosplash:
                if (posIdAvailable()) {
                    gotoSplash(false);
                }
                break;
            case R.id.btn_jumptosplashforskip:
                if (posIdAvailable()) {
                    gotoSplash(true);
                }
                break;
            case R.id.btn_preload:
                if (posIdAvailable()) {
                    preLoad();
                }
                break;
            case R.id.btn_fetch_only:
                if (posIdAvailable()) {
                    intent.putExtra("customBottom", false);
                    intent.putExtra("fetchOnly", true);
                    startActivity(intent);
                }
                break;
            case R.id.btn_jumptosplashforbottom:
                if (posIdAvailable()) {
                    intent.putExtra("customSkip", false);
                    intent.putExtra("customBottom", true);
                    startActivity(intent);
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

    private boolean posIdAvailable() {
        if (TextUtils.isEmpty(etPosId.getText().toString())) {
            Toast.makeText(this, "广告位ID不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void preLoad() {
        SplashAdLoader splashAD = new SplashAdLoader(this, null,null, etPosId.getText().toString(), null, 0);
        splashAD.preLoad();
        Toast.makeText(this, "已执行预加载", Toast.LENGTH_SHORT).show();
    }

    private void gotoSplash(boolean customSkip) {
        Intent intent = new Intent(SplashSettingActivity.this, SplashActivity.class);
        String posId = etPosId.getText().toString();
        intent.putExtra("posId", posId);
        intent.putExtra("customSkip", customSkip);
        startActivity(intent);
    }
}
