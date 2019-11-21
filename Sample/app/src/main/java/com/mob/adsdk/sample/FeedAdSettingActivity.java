package com.mob.adsdk.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



public class FeedAdSettingActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "FeedAdSettingActivity";

    private EditText etPosId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_ad_setting);
        Button btn = findViewById(R.id.loadAd);
        btn.setOnClickListener(this);

        etPosId = findViewById(R.id.et_pos_id);
        etPosId.setText(MobConstants.native_feed_id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loadAd:
                String posId = etPosId.getText().toString();
                if (posId.isEmpty()){
                    posId = MobConstants.native_feed_id;
                }
                Intent intent = new Intent(this, NativeRecyclerListActivity.class);
                intent.putExtra("posId", posId);
                startActivity(intent);
                break;
        }
    }


}
