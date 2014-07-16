package com.normal.ordering.tools;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;

public class AsyncViewTask extends AsyncTask<View, Void, Drawable> {

	private View mView;
	int sdk = android.os.Build.VERSION.SDK_INT;
	private HashMap<String, SoftReference<Drawable>> imageCache;

	public AsyncViewTask() {
		imageCache = new HashMap<String, SoftReference<Drawable>>();
	}

	/**
	 * 耗时操作都放在这里
	 */
	@Override
	protected Drawable doInBackground(View... views) {
		Drawable drawable = null;
		View view = views[0];
		if (view.getTag() != null) {
			if (imageCache.containsKey(view.getTag())) {
				SoftReference<Drawable> cache = imageCache.get(view.getTag()
						.toString());
				drawable = cache.get();
				if (drawable != null) {
					return drawable;
				}
			}
			try {
				if (URLUtil.isHttpUrl(view.getTag().toString())) {// 如果为网络地址。则连接url下载图片
					// 采用 HTTP:// 判断是否为URL
					URL url = new URL(view.getTag().toString());
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setDoInput(true);
					conn.connect();
					InputStream stream = conn.getInputStream();
					drawable = Drawable.createFromStream(stream, "src");
					stream.close();
					// Log.i("adapter",);
				} else {// 如果为本地数据，直接解析
					drawable = Drawable
							.createFromPath(view.getTag().toString());
				}
			} catch (Exception e) {
				Log.v("img", e.getMessage());
				return null;
			}
		} else {// 没有图片
			return null;
		}
		this.mView = view;
		return drawable;
	}

	/**
	 * 更新UI 有点类似Handler setBackground 最低API 16（4.1.2） 
	 * int sdk =android.os.Build.VERSION.SDK_INT; 
	 * if(sdk <android.os.Build.VERSION_CODES.JELLY_BEAN)
	 *  { setBackgroundDrawable(); }
	 * else { setBackground(); }
	 */
	@Override
	@SuppressLint("NewApi")
	protected void onPostExecute(Drawable drawable) {
		if (drawable != null) {
			if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				this.mView.setBackgroundDrawable(drawable);
			} else {
				this.mView.setBackground(drawable);
			}
			
			this.mView = null;
		}
	}
}
