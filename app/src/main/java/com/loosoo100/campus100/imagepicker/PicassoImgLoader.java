package com.loosoo100.campus100.imagepicker;

import java.io.File;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.loosoo100.campus100.R;

public class PicassoImgLoader implements ImgLoader {
    @Override
    public void onPresentImage(ImageView imageView, String imageUri) {
        Glide.with(imageView.getContext())
                .load(new File(imageUri)).asBitmap()
                .centerCrop()
                .dontAnimate()
                .override(200, 200).centerCrop()
                .placeholder(R.drawable.default_img)
                .error(R.drawable.default_img)
                .into(imageView);
//		Glide.with(imageView.getContext()).load(new File(imageUri))
//				.dontAnimate().override(300, 300).centerCrop()
//				.placeholder(R.drawable.default_img).into(imageView);

    }
}
