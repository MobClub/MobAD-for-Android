package com.mob.adsdk.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * DrawAd类型暂不开放支持，详情请咨询商务。
 **/
public class DrawAdSettingActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "DrawAdSettingActivity";

    private EditText etPosId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_ad_setting);
        Button btn = findViewById(R.id.loadAd);
        etPosId = findViewById(R.id.et_pos_id);
        etPosId.setText(MobConstants.draw_video_id);
        btn.setText("打开Draw广告");
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loadAd:
                String posId = etPosId.getText().toString();
                if (posId.isEmpty()){
                    posId = MobConstants.draw_video_id;
                }
                Intent intent = new Intent(this, DrawNativeVideoActivity.class);
                intent.putExtra("posId", posId);
                startActivity(intent);
                break;
        }
    }


}
