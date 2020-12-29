package com.mob.adsdk.sample;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.mob.MobSDK;
import com.mob.PrivacyPolicy;

public class PrivacyDialog extends Dialog implements View.OnClickListener {
	private TextView tvContent;
	private TextView tvSubmit;
	private TextView tvCancel;
	private View.OnClickListener cancelListener;
	private View.OnClickListener submitListener;

	public PrivacyDialog(Context context) {
		super(context);
		initView();
	}

	private void initView() {
		setContentView(R.layout.layout_privacy);

		tvContent = (TextView) findViewById(R.id.tvContent);
		tvSubmit = (TextView) findViewById(R.id.tvSubmit);
		tvCancel = (TextView) findViewById(R.id.tvCancel);

		tvSubmit.setOnClickListener(this);
		tvCancel.setOnClickListener(this);

		setCanceledOnTouchOutside(false);//外部点击取消

		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


		new Thread(new Runnable() {
			@Override
			public void run() {
				PrivacyPolicy privacyPolicy = MobSDK.getPrivacyPolicy(MobSDK.POLICY_TYPE_TXT);
				final String text = (null == privacyPolicy) ? "" : privacyPolicy.getContent();
				tvContent.post(new Runnable() {
					@Override
					public void run() {
						tvContent.setText(Html.fromHtml(text));
					}
				});
			}
		}).start();

		setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
					return true;
				} else {
					return false;
				}
			}
		});
	}

	public PrivacyDialog setCancelListener(View.OnClickListener listener) {
		this.cancelListener = listener;
		return this;
	}

	public PrivacyDialog setSubmitListener(View.OnClickListener listener) {
		this.submitListener = listener;
		return this;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tvCancel:
				dismiss();
				if (null != cancelListener) {
					cancelListener.onClick(v);
				}
				break;
			case R.id.tvSubmit:
				dismiss();
				if (null != submitListener) {
					submitListener.onClick(v);
				}
				break;
		}
	}
}
