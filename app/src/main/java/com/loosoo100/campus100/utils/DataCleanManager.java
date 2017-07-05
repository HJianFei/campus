package com.loosoo100.campus100.utils;

import android.content.Context;

import com.loosoo100.campus100.chat.utils.ToastUtil;

import java.io.File;

/** * 本应用数据清除管理器 */
public class DataCleanManager {

	public static void cleanApplicationData(Context context) {
		File filesDir = context.getFilesDir();// /data/data/package_name/files
		File cacheDir = context.getCacheDir();// /data/data/package_name/cache
		deleteDir(filesDir);
		deleteDir(cacheDir);
		ToastUtil.showToast(context,"清除成功");
	}

	/** * 删除某个目录下的全部文件 */
	private static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	public static String getCacheSize(Context context) {
		// 计算缓存大小
		long fileSize = 0;
		String cacheSize = "0KB";
		File filesDir = context.getFilesDir();// /data/data/package_name/files
		File cacheDir = context.getCacheDir();// /data/data/package_name/cache

		fileSize += getDirSize(filesDir);
		fileSize += getDirSize(cacheDir);

		if (fileSize > 0)
			cacheSize = formatFileSize(fileSize);
		return cacheSize;
	}

	public static long getDirSize(File dir) {
		if (dir == null) {
			return 0;
		}
		if (!dir.isDirectory()) {
			return 0;
		}
		long dirSize = 0;
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				dirSize += file.length();
			} else if (file.isDirectory()) {
				dirSize += file.length();
				dirSize += getDirSize(file); // 递归调用继续统计
			}
		}

		return dirSize;
	}

	public static String formatFileSize(long fileS) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

}