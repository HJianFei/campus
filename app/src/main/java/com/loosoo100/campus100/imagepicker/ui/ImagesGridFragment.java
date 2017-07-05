package com.loosoo100.campus100.imagepicker.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.ImagePreviewActivity;
import com.loosoo100.campus100.imagepicker.AndroidImagePicker;
import com.loosoo100.campus100.imagepicker.ImgLoader;
import com.loosoo100.campus100.imagepicker.PicassoImgLoader;
import com.loosoo100.campus100.imagepicker.bean.ImageItem;
import com.loosoo100.campus100.imagepicker.bean.ImageSet;
import com.loosoo100.campus100.imagepicker.data.DataSource;
import com.loosoo100.campus100.imagepicker.data.OnImagesLoadedListener;
import com.loosoo100.campus100.imagepicker.data.impl.LocalDataSource;
import com.loosoo100.campus100.imagepicker.widget.SuperCheckBox;

public class ImagesGridFragment extends Fragment implements
		OnImagesLoadedListener,
		AndroidImagePicker.OnImageSelectedChangeListener,
		AndroidImagePicker.OnImageCropCompleteListener {

	Activity mContext;

	GridView mGridView;
	ImageGridAdapter mAdapter;

	Button btnDir;
	private ListPopupWindow mFolderPopupWindow;
	private ImageSetAdapter mImageSetAdapter;
	List<ImageSet> mImageSetList;

	ImgLoader mImagePresenter;
	AndroidImagePicker androidImagePicker;
	private View mFooterView;

	private static final int ITEM_TYPE_CAMERA = 0;// the first Item may be
													// Camera
	private static final int ITEM_TYPE_NORMAL = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();

		androidImagePicker = AndroidImagePicker.getInstance();
		// androidImagePicker.clear();

		androidImagePicker.addOnImageSelectedChangeListener(this);

		if (androidImagePicker.cropMode) {
			androidImagePicker.addOnImageCropCompleteListener(this);
		}

		// androidImagePicker.clearSelectedImages();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater
				.inflate(R.layout.fragment_images_grid, null);
		mFooterView = contentView.findViewById(R.id.footer_panel);
		btnDir = (Button) contentView.findViewById(R.id.btn_dir);
		mGridView = (GridView) contentView.findViewById(R.id.gridview);

		mImagePresenter = new PicassoImgLoader();

		DataSource dataSource = new LocalDataSource(mContext);
		dataSource.provideMediaItems(this);

		final int width = mContext.getResources().getDisplayMetrics().widthPixels;
		final int height = mContext.getResources().getDisplayMetrics().heightPixels;

		mImageSetAdapter = new ImageSetAdapter(mContext);
		mImageSetAdapter.refreshData(mImageSetList);

		btnDir.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mFolderPopupWindow == null) {
					createPopupFolderList(width, height);
				}
				backgroundAlpha(0.3f);
				mImageSetAdapter.refreshData(mImageSetList);
				mFolderPopupWindow.setAdapter(mImageSetAdapter);
				if (mFolderPopupWindow.isShowing()) {
					mFolderPopupWindow.dismiss();
				} else {
					mFolderPopupWindow.show();
					int index = mImageSetAdapter.getSelectIndex();
					index = index == 0 ? index : index - 1;
					mFolderPopupWindow.getListView().setSelection(index);
				}

			}
		});

		return contentView;

	}

	@Override
	public void onImageSelectChange(int position, ImageItem item,
			int selectedItemsCount, int maxSelectLimit) {
		mAdapter.refreshData(AndroidImagePicker.getInstance()
				.getImageItemsOfCurrentImageSet());
	}

	@Override
	public void onImageCropComplete(Bitmap bmp, float ratio) {
		getActivity().finish();
	}

	/**
	 * Adapter of image GridView
	 */
	class ImageGridAdapter extends BaseAdapter {
		List<ImageItem> images;
		Context mContext;

		public ImageGridAdapter(Context ctx, List<ImageItem> images) {
			this.images = images;
			this.mContext = ctx;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public int getItemViewType(int position) {
			if (shouldShowCamera()) {
				return position == 0 ? ITEM_TYPE_CAMERA : ITEM_TYPE_NORMAL;
			}
			return ITEM_TYPE_NORMAL;
		}

		@Override
		public int getCount() {
//			return shouldShowCamera() ? images.size() + 1 : images.size();
			 return images.size();
		}

		@Override
		public ImageItem getItem(int position) {
//			if (shouldShowCamera()) {
//				if (position == 0) {
//					return null;
//				}
//				return images.get(position - 1);
//			} else {
				return images.get(position);
//			}

		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

//			int itemViewType = getItemViewType(position);
//			if (itemViewType == ITEM_TYPE_CAMERA) {
//				convertView = LayoutInflater.from(mContext).inflate(
//						R.layout.grid_item_camera, parent, false);
//				convertView.setTag(null);
//				convertView.setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View v) {
						// try {
						// androidImagePicker.takePicture(
						// ImagesGridFragment.this,
						// AndroidImagePicker.REQ_CAMERA);
						// } catch (IOException e) {
						// e.printStackTrace();
						// }
//					}
//				});
//			} else {
				final ViewHolder holder;
				if (convertView == null) {
					convertView = LayoutInflater.from(mContext).inflate(
							R.layout.image_grid_item, null);
					holder = new ViewHolder();
					holder.ivPic = (ImageView) convertView
							.findViewById(R.id.iv_thumb);
					holder.cbSelected = (SuperCheckBox) convertView
							.findViewById(R.id.iv_thumb_check);
					holder.cbPanel = convertView
							.findViewById(R.id.thumb_check_panel);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				if (shouldSelectMulti()) {// Multi Select mode will show a
											// CheckBox at the Top Right corner
					holder.cbSelected.setVisibility(View.VISIBLE);
				} else {
					holder.cbSelected.setVisibility(View.GONE);
				}

				final ImageItem item = getItem(position);

				holder.cbSelected
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								if (androidImagePicker.getSelectImageCount() > androidImagePicker
										.getSelectLimit()) {
									if (holder.cbSelected.isChecked()) {
										// had better use ImageView instead of
										// CheckBox
										holder.cbSelected.toggle();// do this
																	// because
																	// CheckBox
																	// will auto
																	// toggle
																	// when
																	// clicking,must
																	// inverse
										Toast.makeText(
												mContext,
												"最多选择"
														+ androidImagePicker
																.getSelectLimit()
														+ "张图片",
												Toast.LENGTH_SHORT).show();
									}
								}
							}
						});

				holder.cbSelected.setOnCheckedChangeListener(null);// first set
																	// null or
																	// will have
																	// a bug
																	// when
																	// Recycling
																	// the view
				if (androidImagePicker.isSelect(position, item)) {
					holder.cbSelected.setChecked(true);
					holder.ivPic.setSelected(true);
				} else {
					holder.cbSelected.setChecked(false);
				}

				holder.ivPic.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
//						if (!androidImagePicker.isSelect(position, item)) {
//							if (androidImagePicker.getSelectImageCount() >= androidImagePicker
//									.getSelectLimit()) {
//								Toast.makeText(
//										mContext,
//										"最多选择"
//												+ androidImagePicker
//														.getSelectLimit()
//												+ "张图片", Toast.LENGTH_SHORT)
//										.show();
//							} else {
//								androidImagePicker.addSelectedImageItem(
//										position, item);
//							}
//						} else {
//							androidImagePicker.deleteSelectedImageItem(
//									position, item);
//						}
						Intent intent = new Intent(getActivity(),ImagePreviewActivity.class);
						intent.putExtra("picUrl", item.path);
						startActivity(intent);
					}
				});

				holder.cbSelected
						.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {

								if (isChecked) {
									androidImagePicker.addSelectedImageItem(
											position, item);
								} else {
									androidImagePicker.deleteSelectedImageItem(
											position, item);
								}
							}
						});

				// load the image to ImageView
				mImagePresenter.onPresentImage(holder.ivPic,
						getItem(position).path);
//			}
			return convertView;

		}

		class ViewHolder {
			ImageView ivPic;
			SuperCheckBox cbSelected;
			View cbPanel;
		}

		public void refreshData(List<ImageItem> items) {
			if (items != null && items.size() > 0) {
				images = items;
			}
			notifyDataSetChanged();
		}

	}

	private boolean shouldSelectMulti() {
		return androidImagePicker.getSelectMode() == AndroidImagePicker.Select_Mode.MODE_MULTI;
	}

	private boolean shouldShowCamera() {
		return androidImagePicker.isShouldShowCamera();
	}

	@Override
	public void onImagesLoaded(List<ImageSet> imageSetList) {

		mImageSetList = imageSetList;

		btnDir.setText(imageSetList.get(0).name);
		mAdapter = new ImageGridAdapter(mContext,
				imageSetList.get(0).imageItems);
		mGridView.setAdapter(mAdapter);

	}

	/**
	 * 创建弹出的ListView
	 */
	private void createPopupFolderList(int width, int height) {
		mFolderPopupWindow = new ListPopupWindow(mContext);
		// mFolderPopupWindow.setBackgroundDrawable(new
		// ColorDrawable(Color.TRANSPARENT));
		mFolderPopupWindow.setAdapter(mImageSetAdapter);
		mFolderPopupWindow.setContentWidth(width);
		mFolderPopupWindow.setWidth(width);
		mFolderPopupWindow.setHeight(height * 5 / 8);
		mFolderPopupWindow.setAnchorView(mFooterView);
		mFolderPopupWindow.setModal(true);

		mFolderPopupWindow
				.setOnDismissListener(new PopupWindow.OnDismissListener() {

					@Override
					public void onDismiss() {
						backgroundAlpha(1f);
					}
				});

		mFolderPopupWindow.setAnimationStyle(R.style.popupwindow_anim_style);

		mFolderPopupWindow
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapterView,
							View view, int i, long l) {

						mImageSetAdapter.setSelectIndex(i);
						androidImagePicker
								.setCurrentSelectedImageSetPosition(i);

						final int index = i;
						final AdapterView tempAdapterView = adapterView;

						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								mFolderPopupWindow.dismiss();

								ImageSet imageSet = (ImageSet) tempAdapterView
										.getAdapter().getItem(index);
								if (null != imageSet) {
									mAdapter.refreshData(imageSet.imageItems);
									btnDir.setText(imageSet.name);

								}
								// scroll to the top
								mGridView.smoothScrollToPosition(0);

							}
						}, 100);

					}
				});

	}

	// 设置屏幕透明度
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0~1.0
		mContext.getWindow().setAttributes(lp);
	}

	/**
	 * ImageSet adapter
	 */
	class ImageSetAdapter extends BaseAdapter {
		private Context mContext;
		private LayoutInflater mInflater;

		private List<ImageSet> mImageSets = new ArrayList<>();

		int mImageSize;

		int lastSelected = 0;

		public ImageSetAdapter(Context context) {
			mContext = context;
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mImageSize = mContext.getResources().getDimensionPixelOffset(
					R.dimen.image_cover_size);
		}

		public void refreshData(List<ImageSet> folders) {
			if (folders != null && folders.size() > 0) {
				mImageSets = folders;
			} else {
				mImageSets.clear();
			}
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mImageSets.size();
		}

		@Override
		public ImageSet getItem(int i) {
			return mImageSets.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			ViewHolder holder;
			if (view == null) {
				view = mInflater.inflate(R.layout.list_item_folder, viewGroup,
						false);
				holder = new ViewHolder(view);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			holder.bindData(getItem(i));

			if (lastSelected == i) {
				holder.indicator.setVisibility(View.VISIBLE);
			} else {
				holder.indicator.setVisibility(View.INVISIBLE);
			}

			return view;
		}

		public void setSelectIndex(int i) {
			if (lastSelected == i) {
				return;
			}
			lastSelected = i;
			notifyDataSetChanged();
		}

		public int getSelectIndex() {
			return lastSelected;
		}

		class ViewHolder {
			ImageView cover;
			TextView name;
			TextView size;
			ImageView indicator;

			ViewHolder(View view) {
				cover = (ImageView) view.findViewById(R.id.cover);
				name = (TextView) view.findViewById(R.id.name);
				size = (TextView) view.findViewById(R.id.size);
				indicator = (ImageView) view.findViewById(R.id.indicator);
				view.setTag(this);
			}

			void bindData(ImageSet data) {
				name.setText(data.name);
				size.setText(data.imageItems.size() + "张");
				mImagePresenter.onPresentImage(cover, data.cover.path);
			}

		}

	}

	@Override
	public void onDestroy() {
		androidImagePicker.removeOnImageItemSelectedChangeListener(this);
		if (androidImagePicker.cropMode) {
			androidImagePicker.removeOnImageCropCompleteListener(this);
		}
		super.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == AndroidImagePicker.REQ_CAMERA
				&& resultCode == Activity.RESULT_OK) {
			if (!TextUtils.isEmpty(androidImagePicker.getCurrentPhotoPath())) {
				AndroidImagePicker.galleryAddPic(mContext,
						androidImagePicker.getCurrentPhotoPath());
				getActivity().finish();
				// androidImagePicker.notifyPictureTaken();

				if (androidImagePicker.cropMode) {// 裁图模式
					// Intent intent = new Intent();
					// intent.setClass(mContext, ImageCropActivity.class);
					// intent.putExtra(AndroidImagePicker.KEY_PIC_PATH,
					// androidImagePicker.getCurrentPhotoPath());
					// startActivityForResult(intent,
					// AndroidImagePicker.REQ_CAMERA);
				} else {
					ImageItem item = new ImageItem(
							androidImagePicker.getCurrentPhotoPath(), "", -1);
					androidImagePicker.clearSelectedImages();
					androidImagePicker.addSelectedImageItem(-1, item);
					androidImagePicker.notifyOnImagePickComplete();
				}
			}
		}
	}

}
