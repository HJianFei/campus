package com.loosoo100.campus100.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.loosoo100.campus100.config.MyConfig;

public class MyUtils {

	/**
	 * 安装app
	 * 
	 * @param fileName
	 * @param mContext
	 */
	public static void installAPK(Context mContext, String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			return;
		}
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		// "file://"+file.toString()下载的app的路径
		intent.setDataAndType(Uri.parse("file://" + file.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(intent);
	}

	/**
	 * 半角转换为全角
	 * 
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	/**
	 * 获取内置SD卡路径
	 * 
	 * @return
	 */
	public static String getInnerSDCardPath() {
		return Environment.getExternalStorageDirectory().getPath();
	}

	/**
	 * 获取外置SD卡路径
	 * 
	 * @return 应该就一条记录或空
	 */
	public static List<String> getExtSDCardPath() {
		List<String> lResult = new ArrayList<String>();
		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec("mount");
			InputStream is = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("extSdCard")) {
					String[] arr = line.split(" ");
					String path = arr[1];
					File file = new File(path);
					if (file.isDirectory()) {
						lResult.add(path);
					}
				}
			}
			isr.close();
		} catch (Exception e) {
		}
		return lResult;
	}

	/**
	 * 获取IMEI码
	 * 
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	/**
	 * 将图片路径转为bitmap
	 *
	 * @return
	 * @throws IOException
	 */
	public static Bitmap getBitMap(String path) throws IOException {
		if (path.equals("")) {
			return null;
		}
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(30000);
		InputStream is = conn.getInputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;

		while ((len = is.read(buffer)) != -1) {
			baos.write(buffer, 0, len);
		}
		byte[] result = baos.toByteArray();
		return BitmapFactory.decodeByteArray(result, 0, result.length);
	}

	/**
	 * 转化为2位小数
	 */
	public static DecimalFormat decimalFormat = new DecimalFormat("#.##");

	/**
	 * 获取格式化后的日期(yyyy-MM-dd)
	 * 
	 * @param time
	 * @return
	 */
	public static String getDateYMD(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(time);
		return format.format(date);
	}
	/**
	 * 获取格式化后的日期(yyyy-MM-dd HH:mm)
	 * 
	 * @param time
	 * @return
	 */
	public static String getDateYMDHM(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date(time);
		return format.format(date);
	}
	
	/**
	 * 获取格式化后的日期(HH:mm)
	 * 
	 * @param sqlTime
	 * @return
	 */
	public static String getDate(long sqlTime) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		Date date = new Date(sqlTime);
		return format.format(date);
	}
	/**
	 * 获取数据库格式化后的日期(yyyy-MM-dd)
	 * 
	 * @param sqlTime
	 * @return
	 */
	public static String getSqlDate(long sqlTime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(sqlTime * 1000);
		return format.format(date);
	}
	/**
	 * 获取数据库格式化后的日期(yyyy.MM/dd HH:mm)
	 * 
	 * @param sqlTime
	 * @return
	 */
	public static String getSqlDateYMD(long sqlTime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM/dd HH:mm");
		Date date = new Date(sqlTime * 1000);
		return format.format(date);
	}

	/**
	 * 获取数据库格式化后的日期(MM-dd)
	 * 
	 * @param sqlTime
	 * @return
	 */
	public static String getSqlDateM(long sqlTime) {
		SimpleDateFormat format = new SimpleDateFormat("MM-dd");
		Date date = new Date(sqlTime * 1000);
		return format.format(date);
	}

	/**
	 * 获取数据库格式化后的日期(MM-dd HH:mm)
	 * 
	 * @param sqlTime
	 * @return
	 */
	public static String getSqlDateMH(long sqlTime) {
		SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
		Date date = new Date(sqlTime * 1000);
		return format.format(date);
	}

	/**
	 * 获取数据库格式化后的日期(HH:mm)
	 * 
	 * @param sqlTime
	 * @return
	 */
	public static String getSqlDateH(long sqlTime) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		Date date = new Date(sqlTime * 1000);
		return format.format(date);
	}

	/**
	 * 获取数据库格式化后的日期(yyyy-MM-dd HH:mm)
	 * 
	 * @param sqlTime
	 * @return
	 */
	public static String getSqlDateLong(long sqlTime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date(sqlTime * 1000);
		return format.format(date);
	}

	/**
	 * 获取数据库格式化后的日期(MM月dd日 HH:mm)
	 * 
	 * @param sqlTime
	 * @return
	 */
	public static String getSqlDateMM(long sqlTime) {
		SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH:mm");
		Date date = new Date(sqlTime * 1000);
		return format.format(date);
	}

	/**
	 * 获取数据库格式化后的日期(MM月dd日)
	 * 
	 * @param sqlTime
	 * @return
	 */
	public static String getSqlDateMD(long sqlTime) {
		SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
		Date date = new Date(sqlTime * 1000);
		return format.format(date);
	}

	/**
	 * 质量压缩 并返回Bitmap
	 * 
	 * @param image
	 *            要压缩的图片
	 * @return 压缩后的图片
	 */
	public static Bitmap compressImage(Bitmap image, int kb) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > kb) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			System.out.println("baos.toByteArray().length / 1024-------"
					+ (baos.toByteArray().length / 1024));
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		image.recycle();
		return bitmap;
	}

	/**
	 * 设置bitmap大小
	 * 
	 * @param bitmap
	 * @param newWidth
	 *            新的图片宽度
	 * @return
	 */
	public static Bitmap ResizeBitmap(Bitmap bitmap, int newWidth) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float temp = ((float) height) / ((float) width);
		int newHeight = (int) ((newWidth) * temp);
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		bitmap.recycle();
		return resizedBitmap;
	}

	public static Bitmap ResizeBitmapNoRecycle(Bitmap bitmap, int newWidth) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int mNewHeight;
		int mNewWidth;
		float temp = 1;
		if (width < height) {
			temp = ((float) newWidth) / ((float) width);
			mNewWidth = newWidth;
			mNewHeight = (int) ((height) * temp);
		} else {
			temp = ((float) newWidth) / ((float) height);
			mNewHeight = newWidth;
			mNewWidth = (int) ((width) * temp);
		}

		float scaleWidth = ((float) mNewWidth) / width;
		float scaleHeight = ((float) mNewHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		return resizedBitmap;
	}

	/**
	 * 获取系统图库图片的SD卡路径
	 * 
	 * @param uriString
	 * @return
	 */
	public static String getPhoneImage(Context context, String uriString) {
		Uri selectedImage = Uri.parse(uriString);
		String[] filePathColumns = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(selectedImage,
				filePathColumns, null, null, null);
		cursor.moveToFirst();
		int columnIndex = cursor.getColumnIndex(filePathColumns[0]);
		String fileName = cursor.getString(columnIndex);
		cursor.close();
		return fileName;
	}

	/**
	 * 根据Bundle获取图片在sd卡的路径
	 * 
	 * @param bundle
	 * @return
	 */
	public static String getCameraImage(Bundle bundle) {
		String strState = Environment.getExternalStorageState();
		if (!strState.equals(Environment.MEDIA_MOUNTED)) {
			System.out.println("SD卡不存在");
		}

		String fileName = System.currentTimeMillis() + ".jpg"; // 此处可以改为时间
		// Bundle bundle = data.getExtras();
		Bitmap bitmap = (Bitmap) bundle.get("data");
		File file = new File(MyConfig.FILE_URI);
		if (!file.exists()) {
			file.mkdirs();
		}
		fileName = MyConfig.FILE_URI + fileName;
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(fileName);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stream != null) {
					stream.flush();
					stream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileName;
	}

	/**
	 * 图片转化为字节流
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static byte[] readFileImage(String filename) throws IOException {
		BufferedInputStream bufferedInputStream = new BufferedInputStream(
				new FileInputStream(filename));
		int len = bufferedInputStream.available();
		byte[] bytes = new byte[len];
		int r = bufferedInputStream.read(bytes);
		if (len != r) {
			bytes = null;
			throw new IOException("读取文件不正确");
		}
		bufferedInputStream.close();
		return bytes;
	}

	/**
	 * 设置listview的高度
	 * item外层布局必须是LinearLayout
	 * @param listView
	 */
	public static void setListViewHeight(ListView listView, int addHeight) {
		if (listView == null)
			return;
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		totalHeight = totalHeight + addHeight;
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * 设置gridview的高度，传入列数
	 * item外层布局必须是LinearLayout
	 * @param gridview
	 */
	public static void setGridViewHeight(GridView gridview, int spaceHeight,
			int numColumns) {
		if (gridview == null)
			return;
		ListAdapter listAdapter = gridview.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		int count = listAdapter.getCount();
		if (count % numColumns == 0) {
			count = count / numColumns;
		} else {
			count = count / numColumns + 1;
		}
		for (int i = 0; i < count; i++) {
			View listItem = listAdapter.getView(i, null, gridview);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		totalHeight = totalHeight + count * spaceHeight;
		ViewGroup.LayoutParams params = gridview.getLayoutParams();
		params.height = totalHeight;
		gridview.setLayoutParams(params);
	}

	/**
	 * 设置gridview的高度(列数为3)
	 * item外层布局必须是LinearLayout
	 * @param gridview
	 */
	public static void setGridViewHeight(GridView gridview, int spaceHeight) {
		if (gridview == null)
			return;
		ListAdapter listAdapter = gridview.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		int count = listAdapter.getCount();
		if (count % 3 == 0) {
			count = count / 3;
		} else {
			count = count / 3 + 1;
		}
		for (int i = 0; i < count; i++) {
			View listItem = listAdapter.getView(i, null, gridview);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		totalHeight = totalHeight + count * spaceHeight;
		ViewGroup.LayoutParams params = gridview.getLayoutParams();
		params.height = totalHeight;
		gridview.setLayoutParams(params);
	}

	/**
	 * 设置gridview的高度(头像，最多显示2行)
	 * item外层布局必须是LinearLayout
	 * @param gridview
	 */
	public static void setGridViewHeightHeadShot(GridView gridview,
			int spaceHeight) {
		if (gridview == null)
			return;
		ListAdapter listAdapter = gridview.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		int count = listAdapter.getCount();
		if (count < 8) {
			count = 1;
		} else {
			count = 2;
		}
		for (int i = 0; i < count; i++) {
			View listItem = listAdapter.getView(i, null, gridview);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		totalHeight = totalHeight + count * spaceHeight;
		ViewGroup.LayoutParams params = gridview.getLayoutParams();
		params.height = totalHeight;
		gridview.setLayoutParams(params);
	}

	/**
	 * 获得圆角图片的方法
	 * 
	 * @param bitmap
	 * @param roundPx
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 获取手机屏幕宽度
	 */
	public static int getWidth(Context context) {
		WindowManager systemService = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		return systemService.getDefaultDisplay().getWidth();
	}

	/**
	 * 获取手机状态栏高度
	 */
	public static int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return statusBarHeight;
	}

	/**
	 * 设置状态栏高度
	 *
	 * @param view
	 */
	public static void setStatusBarHeight(Activity activity, View view) {
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
			activity.getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
			// activity.getWindow().addFlags(
			// WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			// 设置空view的高度为状态栏高度
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
					MyUtils.getStatusBarHeight(activity));
			view.setLayoutParams(params);
		}
	}

	/**
	 * 设置小米手机状态栏字体颜色
	 * 
	 */
	public static boolean setMiuiStatusBarDarkMode(Activity activity,
			boolean darkmode) {
		Class<? extends Window> clazz = activity.getWindow().getClass();
		try {
			int darkModeFlag = 0;
			Class<?> layoutParams = Class
					.forName("android.view.MiuiWindowManager$LayoutParams");
			Field field = layoutParams
					.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
			darkModeFlag = field.getInt(layoutParams);
			Method extraFlagField = clazz.getMethod("setExtraFlags", int.class,
					int.class);
			extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag
					: 0, darkModeFlag);
			return true;
		} catch (Exception e) {
			
		}
		return false;
	}

	/**
	 * 分享功能
	 * 
	 * @param context
	 * @param activityTitle
	 * @param msgTitle
	 * @param msgText
	 * @param imgPath
	 */
	public static void shareMsg(Context context, String activityTitle,
			String msgTitle, String msgText, String imgPath) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		if (imgPath == null || imgPath.equals("")) {
			intent.setType("text/plain"); // 纯文本
		} else {
			File f = new File(imgPath);
			if (f != null && f.exists() && f.isFile()) {
				intent.setType("image/png");
				Uri u = Uri.fromFile(f);
				intent.putExtra(Intent.EXTRA_STREAM, u);
			}
		}
		intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
		intent.putExtra(Intent.EXTRA_TEXT, msgText);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(Intent.createChooser(intent, activityTitle));
	}

	/**
	 * uri转bitmap
	 * 
	 * @param context
	 * @param uri
	 * @return
	 */
	public static Bitmap decodeUriAsBitmap(Context context, Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(context.getContentResolver()
					.openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	/**
	 * 显示软键盘
	 * 
	 * @param context
	 * @param editText
	 */
	public static void showSoftInput(final Context context,
			final EditText editText) {
		editText.requestFocus();
		Timer timer = new Timer(); // 设置定时器
		timer.schedule(new TimerTask() {
			@Override
			public void run() { // 弹出软键盘的代码
				InputMethodManager imm = (InputMethodManager) context
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
						InputMethodManager.HIDE_IMPLICIT_ONLY);
			}
		}, 200); // 设置200毫秒的时长
	}

	/**
	 * 隐藏软键盘
	 * 
	 * @param context
	 * @param editText
	 */
	public static void hideSoftInput(final Context context,
			final EditText editText) {
		// 隐藏软键盘
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}

	/**
	 *  将图片内容解析成字节数组
	 * @param inStream
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;

	}

	/**
	 *  将字节数组转换为ImageView可调用的Bitmap对象
	 * @param bytes
	 * @param opts
	 * @return Bitmap
	 */
	public static Bitmap getPicFromBytes(byte[] bytes,
			BitmapFactory.Options opts) {
		if (bytes != null)
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}

	/**
	 *  图片缩放
	 * @param bitmap
	 *            对象
	 * @param w
	 *            要缩放的宽度
	 * @param h
	 *            要缩放的高度
	 * @return newBmp 新 Bitmap对象
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) w / width);
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newBmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		return newBmp;
	}

	/**
	 * 把Bitmap转Byte
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * 把字节数组保存为一个文件
	 */
	public static File getFileFromBytes(byte[] b, String outputFile) {
		BufferedOutputStream stream = null;
		File file = null;
		try {
			file = new File(outputFile);
			FileOutputStream fstream = new FileOutputStream(file);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return file;
	}
}
