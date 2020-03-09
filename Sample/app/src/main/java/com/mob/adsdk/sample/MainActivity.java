package com.mob.adsdk.sample;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mob.MobSDK;
import com.mob.OperationCallback;
import com.mob.PrivacyPolicy;
import com.mob.adsdk.MobAdSdk;
import com.mob.adsdk.sample.utils.SPUtils;
import com.mob.adsdk.utils.MobAdLogger;
import com.mob.tools.utils.UIHandler;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {
	public static final String TAG = "MainActivity";

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
//		findViewById(R.id.full_screen_btn).setOnClickListener(this);
//		findViewById(R.id.draw_btn).setOnClickListener(this);

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
		final AlertDialog.Builder b = new AlertDialog.Builder(this,
				android.R.style.Theme_Material_Light_Dialog);
		b.setTitle("服务授权");
		b.setMessage(Html.fromHtml("《MobTech开发者应用合规指南》： <a href=\"https://mp.weixin.qq.com/s/OCJXcvop16sHpB1GpDTJYQ\">https://mp.weixin.qq.com/s/OCJXcvop16sHpB1GpDTJYQ</a>\n" +
				" 《MobTech隐私政策》：<a href=\"http://www.mob.com/about/policy\">http://www.mob.com/about/policy</a>"));
		b.setPositiveButton("同意", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				submitPrivacyGrantResult(true);//同意授权后同步授权信息到MobSDK
				SPUtils.putBoolean(MainActivity.this, SPUtils.PRIVACY_POLICY_KEY, true);
			}
		});
		b.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				SPUtils.putBoolean(MainActivity.this, SPUtils.PRIVACY_POLICY_KEY, false);
				submitPrivacyGrantResult(false);
//				finish(); 未同意隐私情况下，应该退出应用，或停止调用MobSDK相关服务接口
				Toast.makeText(MainActivity.this, "服务授权后才能正常使用", Toast.LENGTH_LONG).show();
			}
		});
		UIHandler.sendEmptyMessage(0, new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				b.create();
				b.show();
				return false;
			}
		});
	}

	private void submitPrivacyGrantResult(final boolean granted) {
		MobAdSdk.submitPrivacyGrantResult(granted, new OperationCallback<Void>() {
			@Override
			public void onComplete(Void data) {
				Log.d(TAG, "隐私协议授权结果提交：成功");
				UIHandler.sendEmptyMessage(0, new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {
						if (granted) {
							checkAndRequestPermission();
						} else {
							Toast.makeText(MainActivity.this, "服务授权后才能正常使用", Toast.LENGTH_LONG).show();
						}
						return false;
					}
				});
			}

			@Override
			public void onFailure(Throwable t) {
				Log.d(TAG, "隐私协议授权结果提交：失败");
				UIHandler.sendEmptyMessage(0, new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {
						Toast.makeText(MainActivity.this, "服务授权后才能正常使用", Toast.LENGTH_LONG).show();
						return false;
					}
				});
				Toast.makeText(MainActivity.this, "服务授权后才能正常使用", Toast.LENGTH_LONG).show();
			}
		});
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
//			case R.id.full_screen_btn:
//				startActivity(new Intent(this, FullScreenVideoActivity.class));
//				break;
//			case R.id.draw_btn:
//				startActivity(new Intent(this, DrawAdSettingActivity.class));
////				startActivity(new Intent(this, DrawNativeVideoActivity.class));
//				break;
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
