package com.mob.adsdk.sample;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mob.adsdk.interstitial.InteractionListener;
import com.mob.adsdk.interstitial.InterstitialAd;
import com.mob.adsdk.interstitial.InterstitialAdListener;
import com.mob.adsdk.interstitial.InterstitialAdLoader;
import com.mob.adsdk.interstitial.InterstitialAdMediaListener;
import com.mob.adsdk.interstitial.InterstitialOption;
import com.mob.adsdk.nonstandard.NSAdListener;
import com.mob.adsdk.nonstandard.banner.NSBannerLoader;
import com.mob.adsdk.nonstandard.fframe.NSFloatFrameLoader;
import com.mob.adsdk.nonstandard.interstitial.NSInterstitialLoader;


public class NSAdActivity extends Activity implements View.OnClickListener, NSAdListener {
	private static final String TAG = "NSAdActivity";

	private EditText etBanner, etInterId, etFloatId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ns_ad);
		findViewById(R.id.showBannerAd).setOnClickListener(this);
		findViewById(R.id.showInterstitailAD).setOnClickListener(this);
		findViewById(R.id.showFFrameAD).setOnClickListener(this);
		findViewById(R.id.ivBannerDel).setOnClickListener(this);
		findViewById(R.id.ivInterDel).setOnClickListener(this);
		findViewById(R.id.ivFloatDel).setOnClickListener(this);
		findViewById(R.id.ivLeft).setOnClickListener(this);

		etBanner = findViewById(R.id.etBanner);
		etBanner.setText(MobConstants.ns_banner_id);
		etInterId = findViewById(R.id.etInterId);
		etInterId.setText(MobConstants.ns_interstitial_id);
		etFloatId = findViewById(R.id.etFloatId);
		etFloatId.setText(MobConstants.ns_float_id);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.showBannerAd:
				if (TextUtils.isEmpty(etBanner.getText().toString())) {
					Toast.makeText(this, "posId 为空", Toast.LENGTH_LONG).show();
					return;
				}
				NSBannerLoader loader = new NSBannerLoader(this, etBanner.getText().toString(), this);
				loader.loadAd();
				break;
			case R.id.showInterstitailAD:
				if (TextUtils.isEmpty(etInterId.getText().toString())) {
					Toast.makeText(this, "posId 为空", Toast.LENGTH_LONG).show();
					return;
				}
				new NSInterstitialLoader(this, etInterId.getText().toString(), this).loadAd();
				break;
			case R.id.showFFrameAD:
				if (TextUtils.isEmpty(etFloatId.getText().toString())) {
					Toast.makeText(this, "posId 为空", Toast.LENGTH_LONG).show();
					return;
				}
				new NSFloatFrameLoader(this, etFloatId.getText().toString(), this).loadAd();
				break;
			case R.id.ivBannerDel:
				etBanner.setText("");
				break;
			case R.id.ivInterDel:
				etInterId.setText("");
				break;
			case R.id.ivFloatDel:
				etFloatId.setText("");
				break;
			case R.id.ivLeft:
				finish();
				break;
		}
	}

	@Override
	public void onAdLoaded() {
		Log.d(TAG, "onAdLoaded: 广告加载成功");
	}

	@Override
	public void onAdExposure() {
		Log.d(TAG, "onAdExposure: 广告曝光");
	}

	@Override
	public void onAdClosed() {
		Log.d(TAG, "onAdClosed: 广告关闭");
	}

	@Override
	public void onAdError(int code, String msg) {
		Log.d(TAG, "onAdError: 没有加载到广告");
		Toast.makeText(this, " code : " + code + "  msg : " + msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onAdClick() {
		Log.d(TAG, "onAdClick: 广告点击");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
