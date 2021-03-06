/*
 *
 *  * Copyright (C) 2015 Eason.Lai (easonline7@gmail.com)
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.loosoo100.campus100.imagepicker.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.imagepicker.AndroidImagePicker;
import com.loosoo100.campus100.imagepicker.bean.ImageItem;
import com.loosoo100.campus100.imagepicker.ui.ImagesGridFragment;

public class ImagesGridActivity extends FragmentActivity implements
		View.OnClickListener, AndroidImagePicker.OnImageSelectedChangeListener {

	private TextView mBtnOk;

	ImagesGridFragment mFragment;
	AndroidImagePicker androidImagePicker;
	String imagePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_images_grid);

		int limit = getIntent().getExtras().getInt("limit");

		androidImagePicker = AndroidImagePicker.getInstance();
		androidImagePicker.clearSelectedImages();// most of the time you need to
													// clear the last selected
													// images or you can comment
													// out this line

		//设置最多选择多少张
		androidImagePicker.setSelectLimit(limit);
		//显示全部图片
		androidImagePicker.setCurrentSelectedImageSetPosition(0);
		mBtnOk = (TextView) findViewById(R.id.btn_ok);
		mBtnOk.setOnClickListener(this);

		if (androidImagePicker.getSelectMode() == AndroidImagePicker.Select_Mode.MODE_SINGLE) {
			mBtnOk.setVisibility(View.GONE);
		} else {
			mBtnOk.setVisibility(View.VISIBLE);
		}

		findViewById(R.id.btn_backpress).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});

		// final boolean isCrop = getIntent().getBooleanExtra("isCrop",false);
		final boolean isCrop = androidImagePicker.cropMode;
		imagePath = getIntent().getStringExtra(AndroidImagePicker.KEY_PIC_PATH);
		mFragment = new ImagesGridFragment();
		/*
		 * Bundle data = new Bundle();
		 * data.putString(AndroidImagePicker.KEY_PIC_PATH,imagePath);
		 * mFragment.setArguments(data);
		 */

		// mFragment
		// .setOnImageItemClickListener(new AdapterView.OnItemClickListener() {
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		//
		// position = androidImagePicker.isShouldShowCamera() ? position - 1
		// : position;
		//
		// if (androidImagePicker.getSelectMode() ==
		// AndroidImagePicker.Select_Mode.MODE_MULTI) {
		// go2Preview(position);
		// } else if (androidImagePicker.getSelectMode() ==
		// AndroidImagePicker.Select_Mode.MODE_SINGLE) {
		// if (isCrop) {
		// // Intent intent = new Intent();
		// // intent.setClass(ImagesGridActivity.this,ImageCropActivity.class);
		// //
		// intent.putExtra(AndroidImagePicker.KEY_PIC_PATH,androidImagePicker.getImageItemsOfCurrentImageSet().get(position).path);
		// // startActivity(intent);
		// } else {
		// androidImagePicker.clearSelectedImages();
		// androidImagePicker
		// .addSelectedImageItem(
		// position,
		// androidImagePicker
		// .getImageItemsOfCurrentImageSet()
		// .get(position));
		// setResult(RESULT_OK);
		//
		// finish();
		// androidImagePicker.notifyOnImagePickComplete();
		// }
		//
		// }
		//
		// }
		// });

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, mFragment).commit();

		androidImagePicker.addOnImageSelectedChangeListener(this);

		int selectedCount = androidImagePicker.getSelectImageCount();
		onImageSelectChange(0, null, selectedCount,
				androidImagePicker.getSelectLimit());

	}

	/**
	 * 预览页面
	 * 
	 * @param position
	 */
	private void go2Preview(int position) {
		// Intent intent = new Intent();
		// intent.putExtra(AndroidImagePicker.KEY_PIC_SELECTED_POSITION,
		// position);
		// intent.setClass(ImagesGridActivity.this, ImagePreviewActivity.class);
		// startActivityForResult(intent, AndroidImagePicker.REQ_PREVIEW);
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btn_ok) {
			Intent intent = new Intent();
			intent.putStringArrayListExtra("picList",
					androidImagePicker.getSelectedImages());
			setResult(RESULT_OK, intent);
			androidImagePicker.notifyOnImagePickComplete();
			finish();
			// }else if(v.getId() == R.id.btn_pic_rechoose){
			// finish();
		}

	}

	@Override
	public void onImageSelectChange(int position, ImageItem item,
			int selectedItemsCount, int maxSelectLimit) {
		if (selectedItemsCount > 0) {
//			mBtnOk.setEnabled(true);
			mBtnOk.setText("完成(" + selectedItemsCount + "/" + maxSelectLimit
					+ ")");
		} else {
			mBtnOk.setText("完成");
//			mBtnOk.setEnabled(false);
		}
	}

	@Override
	protected void onDestroy() {
		androidImagePicker.removeOnImageItemSelectedChangeListener(this);
		androidImagePicker.clearImageSets();
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {

			if (requestCode == AndroidImagePicker.REQ_PREVIEW) {
				setResult(RESULT_OK);
				finish();
				androidImagePicker.notifyOnImagePickComplete();
			}

		}

	}

}
