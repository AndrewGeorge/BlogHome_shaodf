package com.example.bloghome_shaodf.tools;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.example.bloghome_shaodf.PublicData;
import com.example.bloghome_shaodf.R;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class DownloadImg {
	// 软引用
	
	private static final DownloadImg imgload = new DownloadImg();
	
	ExecutorService pool;// = Executors.newFixedThreadPool(4);
	private HashMap<String, SoftReference<Drawable>> imageCache;

	public static synchronized DownloadImg getImgloadInstance(){
		return imgload;
	}	
	
	private DownloadImg() {
		pool = Executors.newFixedThreadPool(4);
		imageCache = new HashMap<String, SoftReference<Drawable>>();
	}

	/**
	 * 下载图片
	 * 
	 * @param imageUrl
	 *            图片的URL地址
	 * @param imageCallback
	 *            回调接口
	 * @return 返回图片的drawable
	 */
	 Drawable loadDrawable(final String imageUrl,
			final ImageCallback imageCallback) {

		;
		// 缓存中 含有此url,则直接调用
		if (imageCache.containsKey(imageUrl)) {

			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			Drawable drawable = softReference.get();
			if (drawable != null) {

				return drawable;
			}
		}
//如果不允许下载图片，返回为空
		if(!PublicData.isDownImg)
			return null;
//允许下载图片
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {

				imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
			}
		};
		// ，如果不包括在缓存中，则开启线程下载图片
		pool.execute(new Thread() {

			@Override
			public void run() {
				// 下载图片返回drawable数据
				Drawable drawable = loadImageFromUrl(imageUrl );

				// 将下载的图片缓存到缓存中
				if (drawable != null) {
					imageCache.put(imageUrl, new SoftReference<Drawable>(
							drawable));
					 
					Message message = handler.obtainMessage(0, drawable);
					handler.sendMessage(message);
				} else {
					Message message = handler.obtainMessage(0, null);
					handler.sendMessage(message);
				}

			}

		});
		
		return null;
	}

	/**
	 * 根据URL下载图片，该drawable
	 */
	  Drawable loadImageFromUrl(String imageURL) {
		URL urll;
		Drawable drawable = null;
		try {
			urll = new URL(imageURL);
			HttpURLConnection conn = (HttpURLConnection) urll.openConnection();
			conn.setDoInput(true);
			conn.connect();

			InputStream in = conn.getInputStream();
			drawable = Drawable.createFromStream(in, imageURL);
			conn.disconnect();
			
		} catch (MalformedURLException e) {
			 
			e.printStackTrace();
		} catch (IOException e) {
			 
			e.printStackTrace();
		}

		return drawable;
	}

	// 回调接口
	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, String imageUrl);
	}
	
	
	public  void getImgDrawable( String imageUrl,
			  final ImageView imageView)
	{		
		 
		Drawable drawable = loadDrawable(imageUrl,
				new  DownloadImg.ImageCallback() {
					@Override
					public void imageLoaded(Drawable imageDrawable,
							String imageUrl) {
						if (imageView != null){
							if(imageDrawable != null)
							imageView.setImageDrawable(imageDrawable);
							else
								imageView.setImageResource(R.drawable.sample_face);
						}
						else
							imageView.setImageResource(R.drawable.sample_face);
					}
				});
		
		 if(drawable == null)
				imageView.setImageResource(R.drawable.sample_face);
		 else
			 imageView.setImageDrawable(drawable);
			 

	}

}
