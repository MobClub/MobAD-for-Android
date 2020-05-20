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


public class InterstitialAdActivity extends Activity implements View.OnClickListener, InterstitialAdListener {
	private static final String TAG = "InterstitialADActivity";

	private EditText etPosId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interstitial_ad);
		findViewById(R.id.showInterstitailAD).setOnClickListener(this);
		findViewById(R.id.loadFullVideoAD).setOnClickListener(this);
		findViewById(R.id.showFullVideoAD).setOnClickListener(this);
		findViewById(R.id.iv_pos_del).setOnClickListener(this);
		findViewById(R.id.ivLeft).setOnClickListener(this);


		etPosId = findViewById(R.id.et_pos_id);
		etPosId.setText(MobConstants.interstitial_id);
	}

	private InterstitialAdLoader interstitialAD;

	@Override
	public void onClick(View v) {

		String posId = etPosId.getText().toString();
		if (TextUtils.isEmpty(posId)) {
			Toast.makeText(this, "posId 为空", Toast.LENGTH_LONG).show();
			return;
		}

		switch (v.getId()) {
			case R.id.showInterstitailAD:
				if(posIdAvailable()) {
					initLoader(posId);
					interstitialAD.loadAd();
				}
				break;
			case R.id.loadFullVideoAD:
				if(posIdAvailable()) {
					initLoader(posId);
					interstitialAD.loadFullScreenAD();
				}
				break;
			case R.id.showFullVideoAD:
				if(posIdAvailable()) {
					interstitialAD.showFullScreenAD(this);
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

	private void initLoader(String posId) {

		InterstitialOption interstitialOption = new InterstitialOption.Builder()
				.setAutoPlayMuted(true)
				.setAutoPlayPolicy(InterstitialOption.AutoPlayPolicy.ALWAYS)
				.setMaxVideoDuration(30).build();
		interstitialAD = new InterstitialAdLoader(this, posId, this, interstitialOption, new InterstitialAdMediaListener() {

			@Override
			public void onVideoLoading() {
				Log.d(TAG, "onVideoLoading: 视频加载中");
			}

			@Override
			public void onVideoReady(long var1) {
				Log.d(TAG, "onVideoReady: 视频初始化完成");
			}

			@Override
			public void onVideoStart() {
				Log.d(TAG, "onVideoStart: 视频开始");
			}

			@Override
			public void onVideoPause() {
				Log.d(TAG, "onVideoPause: 视频暂停");
			}

			@Override
			public void onVideoComplete() {
				Log.d(TAG, "onVideoComplete: 视频播放完成");
			}

			@Override
			public void onVideoError(int code, String msg) {
				Log.e(TAG, "onVideoError: ", new Exception("视频出错"));
			}
		});
	}

	@Override
	public void onAdLoaded(InterstitialAd interstitialAd) {
		Log.d(TAG, "onAdLoaded: ");
		Toast.makeText(this, "广告加载成功",Toast.LENGTH_LONG).show();
		interstitialAd.setInteractionListener(new InteractionListener() {
			@Override
			public void onAdClicked() {
				Log.d(TAG, "onAdClicked: 广告被点击");
			}
		});
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
	protected void onDestroy() {
		super.onDestroy();
		if (interstitialAD != null) {
			interstitialAD.destroy();
		}
	}
}
