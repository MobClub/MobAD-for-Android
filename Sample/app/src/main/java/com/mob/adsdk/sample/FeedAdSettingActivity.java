package com.mob.adsdk.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class FeedAdSettingActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "FeedAdSettingActivity";

    private EditText etPosId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_ad_setting);
        findViewById(R.id.loadAd).setOnClickListener(this);

        etPosId = findViewById(R.id.et_pos_id);
        etPosId.setText(MobConstants.native_feed_id);
        findViewById(R.id.ivLeft).setOnClickListener(this);
        findViewById(R.id.iv_pos_del).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loadAd:
                if (TextUtils.isEmpty(etPosId.getText().toString())) {
                    Toast.makeText(this, "广告位ID不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                String posId = etPosId.getText().toString();
                if (posId.isEmpty()){
                    posId = MobConstants.native_feed_id;
                }
                Intent intent = new Intent(this, FeedRecyclerListActivity.class);
                intent.putExtra("posId", posId);
                startActivity(intent);
                break;
            case R.id.ivLeft:
                finish();
                break;
            case R.id.iv_pos_del:
                etPosId.setText("");
                break;
        }
    }


}
