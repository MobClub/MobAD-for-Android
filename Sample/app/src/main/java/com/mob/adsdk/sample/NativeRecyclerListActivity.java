package com.mob.adsdk.sample;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mob.adsdk.nativ.MobAdPatternType;
import com.mob.adsdk.nativ.feeds.AdInteractionListener;
import com.mob.adsdk.nativ.feeds.AdMediaListener;
import com.mob.adsdk.nativ.feeds.MobNativeAd;
import com.mob.adsdk.nativ.feeds.NativeAdListener;
import com.mob.adsdk.nativ.feeds.NativeAdLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class NativeRecyclerListActivity extends Activity implements NativeAdListener {
	private static final String TAG = "NativeRecyclerListActiv";
	private NativeAdLoader nativeAdLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_native_recycle_list);
		initView();
		String posId = MobConstants.native_feed_id;
		if (getIntent().getExtras()!=null) {
			posId = getIntent().getExtras().getString("posId");
		}
		nativeAdLoader = new NativeAdLoader(this, posId, this,new FrameLayout.LayoutParams(0,0));//信息流
		nativeAdLoader.loadAd();
	}

	private CustomAdapter mAdapter;
	private boolean mIsLoading = true;

	private void initView() {
		RecyclerView recyclerView = findViewById(R.id.recycler_view);
		LinearLayoutManager manager = new LinearLayoutManager(this);
		manager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(manager);
		List<NormalItem> list = new ArrayList<>();
		for (int i = 0; i < 10; ++i) {
			list.add(new NormalItem("No." + i + " Init Data"));
		}
		mAdapter = new CustomAdapter(this, list);
		recyclerView.setAdapter(mAdapter);
		recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged( RecyclerView recyclerView, int newState) {

				if (!mIsLoading && newState == SCROLL_STATE_IDLE && !recyclerView.canScrollVertically(1)) {
					mIsLoading = true;
					NativeRecyclerListActivity.this.nativeAdLoader.loadAd();
				}

			}

			@Override
			public void onScrolled( RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
			}
		});
	}

	private List<MobNativeAd> loadedAdDatas = new ArrayList<>();
	private static final int MSG_REFRESH_LIST = 1;
	private H mHandler = new H();

	@Override
	public void onAdLoaded(List<MobNativeAd> adDatas) {
		System.out.println("onAdLoaded: 广告加载成功");
		this.mIsLoading = false;
		loadedAdDatas.addAll(adDatas);
		Message msg = mHandler.obtainMessage(MSG_REFRESH_LIST, adDatas);
		mHandler.sendMessage(msg);

	}

	@Override
	public void onAdExposure() {
		Log.d(TAG, "onAdExposure: 广告曝光");
	}

	@Override
	public void onAdClosed() {
		Log.d(TAG, "onAdClosed: 广告被关闭");
	}

	@Override
	public void onAdError(int code, String msg) {
		Toast.makeText(this, " code : " + code + "  msg : " + msg, Toast.LENGTH_LONG).show();
		this.mIsLoading = false;
	}

	class NormalItem {
		private String mTitle;

		public NormalItem(int index) {
			this("No." + index + " Normal Data");
		}

		public NormalItem(String title) {
			this.mTitle = title;
		}

		public String getTitle() {
			return mTitle;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (this.loadedAdDatas != null) {
			for (MobNativeAd adData : loadedAdDatas) {
				adData.destroy();
			}
		}
	}

	private static final int TYPE_AD = 1;
	private static final int TYPE_DATA = 0;

	public class CustomAdapter extends RecyclerView.Adapter<CustomHolder> {

		private List<Object> mData;
		private Context mContext;
		private TreeSet mADSet = new TreeSet();

		public CustomAdapter(Context context, List list) {
			mContext = context;
			mData = list;
		}

		public void addNormalItem(NormalItem item) {
			mData.add(item);
		}

		public void addAdToPosition(MobNativeAd nativeAdData, int position) {
			if (position >= 0 && position < mData.size()) {
				mData.add(position, nativeAdData);
				mADSet.add(position);
			}
		}

		@Override
		public int getItemViewType(int position) {
			return mADSet.contains(position) ? TYPE_AD : TYPE_DATA;
		}

		@Override
		public CustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View view;
			switch (viewType) {
				case TYPE_AD:
					view = LayoutInflater.from(mContext).inflate(R.layout.item_ad_unified, null);
					break;

				case TYPE_DATA:
					view = LayoutInflater.from(mContext).inflate(R.layout.item_data, null);
					break;

				default:
					view = null;
			}
			return new CustomHolder(view, viewType);
		}

		@Override
		public void onBindViewHolder(CustomHolder holder, int position) {
			switch (getItemViewType(position)) {
				case TYPE_AD:
					initItemView(position, holder);
					break;
				case TYPE_DATA:
					holder.title.setText(((NormalItem) mData.get(position))
							.getTitle());
					break;
			}
		}

		private void initItemView(int position, final CustomHolder holder) {
			final MobNativeAd ad = (MobNativeAd) mData.get(position);
			if (position%2 == 0) {
				ad.setVideoMute(true);
			}else {
				ad.setVideoMute(false);
			}
			String iconUrl = null;
			if (!TextUtils.isEmpty(ad.getIconUrl())) {
				iconUrl = ad.getIconUrl();
			} else if (ad.getImgUrls() != null && ad.getImgUrls().size() > 0) {
				iconUrl = ad.getImgUrls().get(0);
			}
			if (!TextUtils.isEmpty(iconUrl)) {
				setBitamap(holder.logo,iconUrl);
			}
			holder.name.setText(ad.getTitle());
			holder.desc.setText(ad.getDesc());
			List<View> clickableViews = new ArrayList<>();
			clickableViews.add(holder.download);
			clickableViews.add(holder.adInfoContainer);
			// 视频广告
			if (ad.getAdPatternType() == MobAdPatternType.VIDEO) {
				showVideo(holder);
			} else {
				ArrayList<String> imgs = ad.getImgUrls();
				if (imgs != null && imgs.size() == 1) {
					showPoster(holder);
					setBitamap(holder.poster,ad.getImgUrls().get(0));
				} else if (imgs != null && imgs.size() > 1) {
					hideAll(holder);
					if (imgs.size() > 0 && !TextUtils.isEmpty(imgs.get(0))) {
						holder.img1.setVisibility(View.VISIBLE);
						setBitamap(holder.img1,ad.getImgUrls().get(0));

					}
					if (imgs.size() > 1 && !TextUtils.isEmpty(imgs.get(1))) {
						holder.img2.setVisibility(View.VISIBLE);
						setBitamap(holder.img2,ad.getImgUrls().get(1));
					}
					if (imgs.size() > 2 && !TextUtils.isEmpty(imgs.get(2))) {
						holder.img3.setVisibility(View.VISIBLE);
						setBitamap(holder.img3,ad.getImgUrls().get(2));
					}
				} else {

				}
			}
			ad.bindAdToView(NativeRecyclerListActivity.this, holder.container,
					clickableViews, new AdInteractionListener() {
						@Override
						public void onAdClicked() {
							Log.d(TAG, "onAdClicked: 广告被点击");
						}

					});


			String buttonText;
			if (ad.getInteractionType() == 0) {
				buttonText = "浏览";
			} else {
				buttonText = "下载";
			}
			holder.download.setText(buttonText);

			setAdListener(holder, ad);

		}

		private void setBitamap(ImageView view,String url){
			Glide.with(mContext)
					.load(url)
					.into(view);
		}

		private void setAdListener(final CustomHolder holder, final MobNativeAd ad) {
			// 视频广告
			if (ad.getAdPatternType() == MobAdPatternType.VIDEO) {
				ad.bindMediaView(holder.mediaView, new AdMediaListener() {

					@Override
					public void onVideoLoaded() {
						Log.d(TAG, "onVideoLoaded: 视频加载完成");
					}

					@Override
					public void onVideoStart() {
						Log.d(TAG, "onVideoStart: 视频开始播放");
					}

					@Override
					public void onVideoPause() {
						Log.d(TAG, "onVideoPause: 视频暂停");
					}

					@Override
					public void onVideoResume() {
						Log.d(TAG, "onVideoResume: 视频继续播放");
					}

					@Override
					public void onVideoError() {
						Log.e(TAG, "onVideoError: ", new Exception("视频出错"));
					}

					@Override
					public void onVideoCompleted() {
						Log.e(TAG, "onVideoCompleted: 视频播放完成");
					}
				});
			}
		}

		@Override
		public int getItemCount() {
			return mData.size();
		}
	}

	private void showVideo(CustomHolder holder) {
		holder.poster.setVisibility(View.INVISIBLE);
		holder.img1.setVisibility(View.GONE);
		holder.img2.setVisibility(View.GONE);
		holder.img3.setVisibility(View.GONE);
		holder.mediaView.setVisibility(View.VISIBLE);
	}

	private void showPoster(CustomHolder holder) {
		holder.poster.setVisibility(View.VISIBLE);
		holder.img1.setVisibility(View.GONE);
		holder.img2.setVisibility(View.GONE);
		holder.img3.setVisibility(View.GONE);
		holder.mediaView.setVisibility(View.INVISIBLE);
	}

	private void hideAll(CustomHolder holder) {
		holder.poster.setVisibility(View.INVISIBLE);
		holder.img1.setVisibility(View.GONE);
		holder.img2.setVisibility(View.GONE);
		holder.img3.setVisibility(View.GONE);
		holder.mediaView.setVisibility(View.INVISIBLE);
	}

	class CustomHolder extends RecyclerView.ViewHolder {

		public TextView title;
		public ViewGroup mediaView;
		public RelativeLayout adInfoContainer;
		public TextView name;
		public TextView desc;
		public ImageView logo;
		public ImageView poster;
		public ImageView img1;
		public ImageView img2;
		public ImageView img3;
		public Button download;
		public ViewGroup container;

		public CustomHolder(View itemView, int adType) {
			super(itemView);
			switch (adType) {
				case TYPE_AD:
					mediaView = itemView.findViewById(R.id.api_media_view);
					adInfoContainer = itemView.findViewById(R.id.ad_info_container);
					logo = itemView.findViewById(R.id.img_logo);
					poster = itemView.findViewById(R.id.img_poster);
					img1 = itemView.findViewById(R.id.img_1);
					img2 = itemView.findViewById(R.id.img_2);
					img3 = itemView.findViewById(R.id.img_3);
					name = itemView.findViewById(R.id.text_title);
					desc = itemView.findViewById(R.id.text_desc);
					download = itemView.findViewById(R.id.btn_download);
					container = itemView.findViewById(R.id.native_ad_container);

				case TYPE_DATA:
					title = itemView.findViewById(R.id.title);
					break;

			}
		}
	}

	private static final int ITEM_COUNT = 30;
	private static final int FIRST_AD_POSITION = 5;
	private static final int AD_DISTANCE = 10;

	private class H extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MSG_REFRESH_LIST:

					int count = mAdapter.getItemCount();
					for (int i = 0; i < ITEM_COUNT; i++) {
						mAdapter.addNormalItem(new NormalItem(count + i));
					}

					List<MobNativeAd> ads = (List<MobNativeAd>) msg.obj;
					if (ads != null && ads.size() > 0 && mAdapter != null) {
						for (int i = 0; i < ads.size(); i++) {
							mAdapter.addAdToPosition(ads.get(i), count + i * AD_DISTANCE + FIRST_AD_POSITION);
						}
					}
					mAdapter.notifyDataSetChanged();
					break;

				default:
			}
		}
	}
}
