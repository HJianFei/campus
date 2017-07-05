package com.loosoo100.campus100.imagepicker;

import java.io.File;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.loosoo100.campus100.R;

public class GlideImgLoader implements ImgLoader {
    @Override
    public void onPresentImage(ImageView imageView, String imageUri) {
        Glide.with(imageView.getContext())
                .load(new File(imageUri))
                .centerCrop()
                .dontAnimate()
                .override(200, 200).centerCrop()
                .placeholder(R.drawable.default_img)
                .error(R.drawable.default_img)
                .into(imageView);

    }

}
