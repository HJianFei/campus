package com.loosoo100.campus100.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

public class ImageLoader {
	
	private ImageView imageView;
	private String url;
	private LruCache<String, Bitmap>cache;
	
	public ImageLoader(){
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory/4;
		cache = new LruCache<String,Bitmap>(cacheSize){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getByteCount();
			}
		};
	}
	
	//增加到缓存
	public void addBitmapToCache(String url,Bitmap bitmap){
		if (getBitmapFromCache(url)==null) {
			cache.put(url, bitmap);
		}
	}
	
	//从缓存中获取数据
	public Bitmap getBitmapFromCache(String url){
		return cache.get(url);
	}
	
	public void showImageByAsyncTask(ImageView imageView, String url) {
		Bitmap bitmap = getBitmapFromCache(url);
		if (bitmap==null) {
			new MyAsyncTask(imageView,url).execute(url);
		}else {
			imageView.setImageBitmap(bitmap);
		}
		
	}
	
	public void showImageByAsyncTask(String url) {
		new MyAsyncTask(imageView,url).execute(url);
	}
	
	class MyAsyncTask extends AsyncTask<String, Void, Bitmap>{

		private ImageView imageView;
		private String url;
		
		public MyAsyncTask(ImageView imageView,String url){
			this.imageView=imageView;
			this.url=url;
		}
		
		public MyAsyncTask(String url){
			this.url=url;
		}
		
		@Override
		protected Bitmap doInBackground(String... arg0) {
			String url = arg0[0];
			Bitmap bitmap = getBitmap(url);
			if (bitmap!=null) {
				addBitmapToCache(url, bitmap);
			}
			return bitmap;
		}
		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if (imageView.getTag().equals(url)) {
				imageView.setImageBitmap(result);
			}
		}
		
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (imageView.getTag().equals(url)) {
				imageView.setImageBitmap((Bitmap) msg.obj);
			}
		};
	};

	public void showImageByThread(ImageView imageView, final String url) {
		this.imageView=imageView;
		this.url=url;
		new Thread() {
			@Override
			public void run() {
				super.run();
				Bitmap bitmap = getBitmap(url);
				Message message = Message.obtain();
				message.obj=bitmap;
				handler.sendMessage(message);
			}
		}.start();

	}

	public Bitmap getBitmap(String url) {
		Bitmap bitmap = null;
		URLConnection urlConnection;
		InputStream is;
		try {
			urlConnection = new URL(url).openConnection();
			is = urlConnection.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			bitmap = BitmapFactory.decodeStream(bis);
			is.close();
			bis.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bitmap;
	}

}
