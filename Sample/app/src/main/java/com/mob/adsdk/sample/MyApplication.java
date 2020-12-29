package com.mob.adsdk.sample;


import androidx.multidex.MultiDexApplication;

import com.mob.adsdk.MobAdSdk;

public class MyApplication extends MultiDexApplication {

	@Override
	public void onCreate() {
		super.onCreate();
		//广告初始化
		MobAdSdk.initMobAd(this, "2d7ce93a26944", "234b0028a97be15e1650550f338c4a50");
	}
}
