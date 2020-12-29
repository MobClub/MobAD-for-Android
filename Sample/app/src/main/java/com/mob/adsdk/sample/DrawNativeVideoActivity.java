package com.mob.adsdk.sample;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.dingmouren.layoutmanagergroup.viewpager.OnViewPagerListener;
import com.dingmouren.layoutmanagergroup.viewpager.ViewPagerLayoutManager;
import com.mob.adsdk.draw.AdInteractionListener;
import com.mob.adsdk.draw.DrawAdListener;
import com.mob.adsdk.draw.DrawAdLoader;
import com.mob.adsdk.draw.DrawFeedAd;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * 竖版原生视频
 */
public class DrawNativeVideoActivity extends Activity implements DrawAdListener {

    private static final String TAG = "DrawNativeVideoActivity";
    private static final int TYPE_COMMON_ITEM = 1;
    private static final int TYPE_AD_ITEM = 2;
    private RecyclerView mRecyclerView;
    private LinearLayout mBottomLayout;
    private RelativeLayout mTopLayout;
    private MyAdapter mAdapter;
    private ViewPagerLayoutManager mLayoutManager;
    private int[] imgs = {R.mipmap.video10, R.mipmap.video11, R.mipmap.video12, R.mipmap.video13, R.mipmap.video14};
    private int[] videos = {R.raw.video10, R.raw.video11, R.raw.video12, R.raw.video13, R.raw.video14};
    private Context mContext;
    private ArrayList<Item> datas = new ArrayList<>();
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_native_video);
        initView();
        initListener();
        mContext = this;

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadDrawNativeAd();
            }
        }, 500);
    }

    private void loadDrawNativeAd() {
        String posId = MobConstants.draw_video_id;
        if (getIntent().getExtras()!=null) {
            posId = getIntent().getExtras().getString("posId");
        }
        DrawAdLoader drawAdLoader = new DrawAdLoader(this, posId, this);
        drawAdLoader.loadAd();
    }



    private void initView() {
        datas.add(new Item(TYPE_COMMON_ITEM, null, videos[0], imgs[0]));
        mRecyclerView = findViewById(R.id.recycler);
        mBottomLayout = findViewById(R.id.bottom);
        mTopLayout = findViewById(R.id.top);
        mLayoutManager = new ViewPagerLayoutManager(this, OrientationHelper.VERTICAL);
        mAdapter = new MyAdapter(datas);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private View getView() {
        FullScreenVideoView videoView = new FullScreenVideoView(this);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        videoView.setLayoutParams(layoutParams);
        return videoView;
    }


    private void initListener() {
        mLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {
            @Override
            public void onInitComplete() {
                onLayoutComplete();
            }

            @Override
            public void onPageRelease(boolean isNext, int position) {
                Log.e(TAG, "释放位置:" + position + " 下一页:" + isNext);
                int index = 0;
                if (isNext) {
                    index = 0;
                } else {
                    index = 1;
                }
                if (datas.get(position).type == TYPE_COMMON_ITEM)
                    releaseVideo(index);
            }

            @Override
            public void onPageSelected(int position, boolean isBottom) {
                Log.e(TAG, "选中位置:" + position + "  是否是滑动到底部:" + isBottom);
                if (datas.get(position).type == TYPE_COMMON_ITEM) {
                    playVideo(0);
                    changeBottomTopLayoutVisibility(true);
                } else if (datas.get(position).type == TYPE_AD_ITEM) {
                    changeBottomTopLayoutVisibility(false);
                }
            }

            private void onLayoutComplete() {
                if (datas.get(0).type == TYPE_COMMON_ITEM) {
                    playVideo(0);
                    changeBottomTopLayoutVisibility(true);
                } else if (datas.get(0).type == TYPE_AD_ITEM) {
                    changeBottomTopLayoutVisibility(false);
                }
            }

        });
    }

    private void changeBottomTopLayoutVisibility(boolean visibility) {
        mBottomLayout.setVisibility(visibility ? View.VISIBLE : View.GONE);
        mTopLayout.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    private void playVideo(int position) {
        if (isFinishing()) {
            return;
        }

        View itemView = mRecyclerView.getChildAt(0);
        if (itemView == null) {
            return;
        }
        final FrameLayout videoLayout = itemView.findViewById(R.id.video_layout);
        final VideoView videoView = (VideoView) videoLayout.getChildAt(0);
        final ImageView imgPlay = itemView.findViewById(R.id.img_play);
        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        final MediaPlayer[] mediaPlayer = new MediaPlayer[1];
        videoView.start();
        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                mediaPlayer[0] = mp;
                Log.e(TAG, "onInfo");
                mp.setLooping(true);
                imgThumb.animate().alpha(0).setDuration(200).start();
                return false;
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.e(TAG, "onPrepared");

            }
        });


        imgPlay.setOnClickListener(new View.OnClickListener() {
            boolean isPlaying = true;

            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    Log.e(TAG, "isPlaying:" + videoView.isPlaying());
                    imgPlay.animate().alpha(1f).start();
                    videoView.pause();
                    isPlaying = false;
                } else {
                    Log.e(TAG, "isPlaying:" + videoView.isPlaying());
                    imgPlay.animate().alpha(0f).start();
                    videoView.start();
                    isPlaying = true;
                }
            }
        });
    }

    private void releaseVideo(int index) {
        if (isFinishing()) {
            return;
        }

        View itemView = mRecyclerView.getChildAt(index);
        if (itemView != null) {
            final FrameLayout videoLayout = itemView.findViewById(R.id.video_layout);
            if (videoLayout == null) return;
            View view = videoLayout.getChildAt(0);
            if (view instanceof VideoView) {
                final VideoView videoView = (VideoView) videoLayout.getChildAt(0);
                final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
                final ImageView imgPlay = itemView.findViewById(R.id.img_play);
                videoView.stopPlayback();
                imgThumb.animate().alpha(1).start();
                imgPlay.animate().alpha(0f).start();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLayoutManager != null) {
            mLayoutManager.setOnViewPagerListener(null);
        }
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onDrawFeedAdLoad(List<DrawFeedAd> ads) {
        if (ads == null || ads.isEmpty()) {
            Log.e(TAG, " ad is null!");
            return;
        }

        for (int i = 0; i < 5; i++) {
            int random = (int) (Math.random() * 100);
            int index = random % videos.length;
            datas.add(new Item(TYPE_COMMON_ITEM, null, videos[index], imgs[index]));
        }

        for (DrawFeedAd ad : ads) {
            ad.setActivityForDownloadApp(DrawNativeVideoActivity.this);
            //点击监听器必须在getAdView之前调
            ad.setDrawVideoListener(new DrawFeedAd.DrawVideoListener() {
                @Override
                public void onClickRetry() {
                    Log.d(TAG, "onClickRetry!");
                }

                @Override
                public void onClick() {
                    Log.d("drawss", "onClick download or view detail page ! !");
                }
            });
            ad.setCanInterruptVideoPlay(true);
            ad.setPauseIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.dislike_icon), 60);
            int random = (int) (Math.random() * 100);
            int index = random % videos.length;
            datas.add(index, new Item(TYPE_AD_ITEM, ad, -1, -1));
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError(int code, String msg) {
        Toast.makeText(this, " code : " + code + "  msg : " + msg, Toast.LENGTH_LONG).show();
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        ArrayList<Item> datas;

        public MyAdapter(ArrayList<Item> datas) {
            this.datas = datas;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_pager, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            View view = new View(mContext);
            Item item = null;
            if (datas != null) {
                item = datas.get(position);
                if (item.type == TYPE_COMMON_ITEM) {
                    holder.img_thumb.setImageResource(item.ImgId);
                    view = getView();
                    ((VideoView) view).setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + item.videoId));
                } else if (item.type == TYPE_AD_ITEM && item.ad != null) {
                    view = item.ad.getAdView();
                    if (item.ad.getIconImageUrl() != null) {
                        //todo
                        Uri uri = Uri.parse(item.ad.getIconImageUrl());
                        holder.img_head_icon.setImageURI(uri);
                    } else {
                        holder.img_head_icon.setImageBitmap(item.ad.getAdLogo());
                    }
                }
            }
            holder.videoLayout.removeAllViews();
            holder.videoLayout.addView(view);
            if (item != null && item.type == TYPE_AD_ITEM) {
                initAdViewAndAction(item.ad, holder.videoLayout);
            }
            if (item != null) {
                changeUIVisibility(holder, item.type);
            }
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public int getItemViewType(int position) {
            Log.d(TAG, "getItemViewType--" + position);

            return datas.get(position).type;
        }

        @Override
        public void onViewAttachedToWindow( ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
//            changeUIVisibility(holder,);
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img_thumb;
            CircleImageView img_head_icon;
            ImageView img_play;
            RelativeLayout rootView;
            FrameLayout videoLayout;
            LinearLayout verticalIconLauout;

            public ViewHolder(View itemView) {
                super(itemView);
                img_thumb = itemView.findViewById(R.id.img_thumb);
                videoLayout = itemView.findViewById(R.id.video_layout);
                img_play = itemView.findViewById(R.id.img_play);
                rootView = itemView.findViewById(R.id.root_view);
                verticalIconLauout = itemView.findViewById(R.id.vertical_icon);
                img_head_icon = itemView.findViewById(R.id.head_icon);

            }
        }
    }

    private void changeUIVisibility(MyAdapter.ViewHolder holder, int type) {
        boolean visibilable = true;
        if (type == TYPE_AD_ITEM) {
            visibilable = false;
        }
        Log.d(TAG, "是否展示：visibilable=" + visibilable);
        holder.img_play.setVisibility(visibilable ? View.VISIBLE : View.GONE);
        holder.img_thumb.setVisibility(visibilable ? View.VISIBLE : View.GONE);

    }

    private void initAdViewAndAction(DrawFeedAd ad, FrameLayout container) {
        Button action = new Button(this);
        action.setText(ad.getButtonText());
        Button btTitle = new Button(this);
        btTitle.setText(ad.getTitle());

        int height = (int) dip2Px(this, 50);
        int margin = (int) dip2Px(this, 10);
        //noinspection SuspiciousNameCombination
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(height * 3, height);
        lp.gravity = Gravity.END | Gravity.BOTTOM;
        lp.rightMargin = margin;
        lp.bottomMargin = margin;
        container.addView(action, lp);

        FrameLayout.LayoutParams lp1 = new FrameLayout.LayoutParams(height * 3, height);
        lp1.gravity = Gravity.START | Gravity.BOTTOM;
        lp1.rightMargin = margin;
        lp1.bottomMargin = margin;
        container.addView(btTitle, lp1);

        List<View> clickViews = new ArrayList<>();
        clickViews.add(btTitle);
        List<View> creativeViews = new ArrayList<>();
        creativeViews.add(action);
        ad.registerViewForInteraction(container, clickViews, creativeViews, new AdInteractionListener() {
            @Override
            public void onAdClicked(View view) {
                showToast("onAdClicked");
            }

            @Override
            public void onAdCreativeClick(View view) {
                showToast("onAdCreativeClick");
            }

            @Override
            public void onAdShow() {
                showToast("onAdShow");
            }
        });


    }

    private float dip2Px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dipValue * scale + 0.5f;
    }

    private void showToast(String msg) {
        Log.e(TAG, msg);
    }

    private static class Item {
        public int type = 0;
        public DrawFeedAd ad;
        public int videoId;
        public int ImgId;

        public Item(int type, DrawFeedAd ad, int videoId, int imgId) {
            this.type = type;
            this.ad = ad;
            this.videoId = videoId;
            ImgId = imgId;
        }
    }


}
