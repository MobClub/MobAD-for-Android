package com.mob.adsdk.sample;

import android.app.Application;

import com.mob.adsdk.MobAdSdk;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		MobAdSdk.initMobAd();
	}
}
