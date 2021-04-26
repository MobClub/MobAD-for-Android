package com.mob.adsdk.sample;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.adsdk.MobAdSdk;
import com.mob.adsdk.sample.utils.SPUtils;
import com.mob.adsdk.utils.MobAdLogger;
import com.mob.adsdk.widget.GrantCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {
	public static final String TAG = "MainActivity";
	private TextView tvAd;
	private TextView tvSetting;
	private LinearLayout ll_ad;
	private LinearLayout ll_setting;
	private TextView tvAbout;

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
		findViewById(R.id.ns_btn).setOnClickListener(this);
//		findViewById(R.id.full_screen_btn).setOnClickListener(this);
//		findViewById(R.id.draw_btn).setOnClickListener(this);
		tvAd = findViewById(R.id.tvAd);
		tvSetting = findViewById(R.id.tvSetting);
		ll_ad = findViewById(R.id.ll_ad);
		ll_setting = findViewById(R.id.ll_setting);
		tvAd.setOnClickListener(this);
		tvSetting.setOnClickListener(this);
		tvAbout = findViewById(R.id.tvAbout);
		tvAbout.setOnClickListener(this);

		//此处开关由开发者自行决定
		if (SPUtils.getBoolean(this, SPUtils.PRIVACY_POLICY_KEY, false)) {
			checkAndRequestPermission();
		} else {//若从未授权隐私协议服务，弹窗授权并同步授权信息到MobAdSDK
			privacyDialog();
		}
		MobAdLogger.debug(true);

	}

	//隐私服务授权弹窗
	public void privacyDialog() {
		PrivacyDialog dialog = new PrivacyDialog(this);
		dialog.setCancelListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						submitPrivacyGrantResult(false);
						//finish(); 未同意隐私情况下，应该退出应用，或停止调用MobSDK相关服务接口
						Toast.makeText(MainActivity.this, "服务授权后才能正常使用", Toast.LENGTH_LONG).show();
					}
				})
				.setSubmitListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						submitPrivacyGrantResult(true);//同意授权后同步授权信息到MobAdSDK
					}
				}).show();
	}

	private void submitPrivacyGrantResult(final boolean granted) {
		MobAdSdk.submitPrivacyGrantResult(granted, new GrantCallback<Void>() {
			@Override
			public void onComplete(Void data) {
				Log.d(TAG, "隐私协议授权结果提交：成功");
				if (granted) {
					checkAndRequestPermission();
//				} else {
//					Toast.makeText(MainActivity.this, "隐私协议授权结果提交：成功", Toast.LENGTH_LONG).show();
				}
				SPUtils.putBoolean(MainActivity.this, SPUtils.PRIVACY_POLICY_KEY, granted);
			}

			@Override
			public void onFailure(Throwable t) {
				SPUtils.putBoolean(MainActivity.this, SPUtils.PRIVACY_POLICY_KEY, false);
				Log.d(TAG, "隐私协议授权结果提交：失败");
				Toast.makeText(MainActivity.this, "隐私协议授权结果提交：失败", Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ns_btn:
				startActivity(new Intent(this, NSAdActivity.class));
				break;
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
//			case R.id.full_screen_btn:
//				startActivity(new Intent(this, FullScreenVideoActivity.class));
//				break;
//			case R.id.draw_btn:
//				startActivity(new Intent(this, DrawAdSettingActivity.class));
//				break;
			case R.id.tvAd:
				checkAd(true);
				break;
			case R.id.tvSetting:
				checkAd(false);
				break;
			case R.id.tvAbout:
				startActivity(new Intent(this, AboutActivity.class));
				break;
		}
	}

	private void checkAd(boolean isAd) {
		tvAd.setEnabled(!isAd);
		tvSetting.setEnabled(isAd);
		if (isAd) {
			ll_ad.setVisibility(View.VISIBLE);
			ll_setting.setVisibility(View.GONE);
		} else {
			ll_ad.setVisibility(View.GONE);
			ll_setting.setVisibility(View.VISIBLE);
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
