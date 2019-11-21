package com.mob.adsdk.sample;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.mob.adsdk.utils.MobAdLogger;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.splash_btn).setOnClickListener(this);
		findViewById(R.id.banner_btn).setOnClickListener(this);
		findViewById(R.id.interstitial_btn).setOnClickListener(this);
		findViewById(R.id.feed_btn).setOnClickListener(this);
		findViewById(R.id.native_express_ad).setOnClickListener(this);
		findViewById(R.id.rewardvideo_btn).setOnClickListener(this);
		findViewById(R.id.full_screen_btn).setOnClickListener(this);
		findViewById(R.id.draw_btn).setOnClickListener(this);

		checkAndRequestPermission();

		MobAdLogger.debug(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.splash_btn:
				startActivity(new Intent(this, SplashSettingActivity.class));
				break;
			case R.id.banner_btn:
				startActivity(new Intent(this, BannerAdActivity.class));
				break;
			case R.id.interstitial_btn:
				startActivity(new Intent(this, InterstitialAdActivity.class));
				break;
			case R.id.native_express_ad:
				startActivity(new Intent(this, NativeExpressAdActivity.class));
				break;
			case R.id.feed_btn:
//				startActivity(new Intent(this, NativeRecyclerListActivity.class));
				startActivity(new Intent(this, FeedAdSettingActivity.class));
				break;
			case R.id.rewardvideo_btn:
				startActivity(new Intent(this, RewardVideoAdActivity.class));
				break;
			case R.id.full_screen_btn:
				startActivity(new Intent(this, FullScreenVideoActivity.class));
				break;
			case R.id.draw_btn:
				startActivity(new Intent(this, DrawAdSettingActivity.class));
//				startActivity(new Intent(this, DrawNativeVideoActivity.class));
				break;
		}
	}

	private void checkAndRequestPermission() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			List<String> lackedPermission = new ArrayList<String>();
			if (!(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
				lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
			}

			if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
				lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
			}

			if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
				lackedPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
			}

			// 权限都已经有了，那么直接调用SDK
			if (lackedPermission.size() > 0) {
				// 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
				String[] requestPermissions = new String[lackedPermission.size()];
				lackedPermission.toArray(requestPermissions);
				requestPermissions(requestPermissions, 1024);
			}
		}
	}
}
