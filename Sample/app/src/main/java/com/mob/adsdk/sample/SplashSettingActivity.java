package com.mob.adsdk.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mob.adsdk.splash.SplashAdLoader;

public class SplashSettingActivity extends Activity implements View.OnClickListener {

    private Button btnJumpToSplash;
    private Button btnJumpToSplashForSkip;
    private Button btnPreload;
    private Button btnJumpToSplashForBottom;
    private EditText etPosId;
    private Button btnJumpToSplashForFetchOnly;

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

        etPosId = findViewById(R.id.et_pos_id);
        etPosId.setText(MobConstants.splash_id);
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
                gotoSplash(false);
                break;
            case R.id.btn_jumptosplashforskip:
                gotoSplash(true);
                break;
            case R.id.btn_preload:
                preLoad();
                break;
            case R.id.btn_fetch_only:
                intent.putExtra("customBottom", false);
                intent.putExtra("fetchOnly", true);
                startActivity(intent);
                break;
            case R.id.btn_jumptosplashforbottom:
                intent.putExtra("customSkip", false);
                intent.putExtra("customBottom", true);
                startActivity(intent);
                break;
        }
    }

    private void preLoad() {
        SplashAdLoader splashAD = new SplashAdLoader(this, null,null, etPosId.getText().toString(), null, 0);
        splashAD.preLoad();
    }

    private void gotoSplash(boolean customSkip) {
        Intent intent = new Intent(SplashSettingActivity.this, SplashActivity.class);
        String posId = etPosId.getText().toString();
        intent.putExtra("posId", posId);
        intent.putExtra("customSkip", customSkip);
        startActivity(intent);
    }
}
